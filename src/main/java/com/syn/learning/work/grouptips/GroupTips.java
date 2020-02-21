package com.syn.learning.work.grouptips;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2020/2/20 10:24
 **/
public class GroupTips {

    private static class Tip {
        int id;
        double[] point;
    }

    private static class Block {
        int id;
        // 四个值，分别是左下角坐标和右上角坐标
        double[] range;
        int tipsCount;
        //在网格中的行、列数。寻找四邻域时用到了
        int row;
        int col;
        // 如果该属性为null，说明该block没有落在该订单圈内。
        Geometry geometry;
    }

    private static class GroupBlock {
        int id;
        List<Block> blocks = new ArrayList<>();
        int tipsCount;
        Geometry geometry;
    }

    private static GeometryFactory factory = new GeometryFactory();
    private static WKTWriter wktWriter = new WKTWriter();
    private static Polygon borderPolygon;
    private static List<double[]> adminBorder = new ArrayList<>();
    private static List<Tip> tips = new ArrayList<>();
    /**
     * 生成的网格，n*m大小，每个元素存4个值，分别是该网格的左下角坐标和右上角坐标
     * blocks[0][0]是经度最小并且纬度也最小的网格，即地图上的左下角点
     */
    private static Block[][] blocks;
    private static List<GroupBlock> groupBlocks = new ArrayList<>();
    private static int emptyThreshold;
    private static final double BLOCK_SIZE = 0.002;
    private static final int MAX_TIPS_SIZE = 1800;
    private static final int MIN_TIPS_SIZE = 1000;

    private static int calCount = 0;

    public static void main(String[] args) throws Exception {
        loadBorder("C:\\work\\工单分组\\Tips抓取\\提供研发\\bj.txt");
        createBorderPolygon();
        loadTips("C:\\work\\工单分组\\Tips抓取\\提供研发\\实验\\北京市北京市城区_5058_cluster.txt");

        long s = System.currentTimeMillis(), s2 = s;
        generateBlocks();
        System.out.println("生成block：" + (System.currentTimeMillis() - s));

        s = System.currentTimeMillis();
//        createBlockPolygon();
        createBlockPolygon2(0, 0, blocks.length - 1, blocks[0].length - 1);
        System.out.println(calCount + " 计算block的geometry：" + (System.currentTimeMillis() - s));

        s = System.currentTimeMillis();
        mergeBlock();
        System.out.println("合并block：" + (System.currentTimeMillis() - s));

        s = System.currentTimeMillis();
        mergeSmallGroup();
        System.out.println("合并smallGroup：" + (System.currentTimeMillis() - s));

        saveGroup("C:\\work\\工单分组\\Tips抓取\\提供研发\\block.txt");
        System.out.println("总耗时：" + (System.currentTimeMillis() - s2));
    }

    private static void loadBorder(String path) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            String[] ps = line.substring(10, line.length() - 2).split(",");
            for (String p : ps) {
                String[] ss = p.split(" ");
                adminBorder.add(new double[]{Double.parseDouble(ss[0]), Double.parseDouble(ss[1])});
            }
        }
    }

    private static void createBorderPolygon() {
        Coordinate[] coordinates = new Coordinate[adminBorder.size() + 1];
        for (int i = 0; i < adminBorder.size(); i++) {
            coordinates[i] = new Coordinate(adminBorder.get(i)[0], adminBorder.get(i)[1]);
        }
        coordinates[adminBorder.size()] = new Coordinate(adminBorder.get(0)[0], adminBorder.get(0)[1]);
        borderPolygon = factory.createPolygon(coordinates);
    }

    private static void loadTips(String path) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            String[] ss = line.split("\t");
            String[] p = ss[2].substring(6, ss[2].length() - 1).split(" ");
            Tip tip = new Tip();
            tip.id = Integer.parseInt(ss[0]);
            tip.point = new double[]{Double.parseDouble(p[0]), Double.parseDouble(p[1])};
            tips.add(tip);
        }
    }

    /**
     * 生成网格。单位均是度，不是米或千米
     */
    private static void generateBlocks() {
        // 下上左右四个边界
        double[] corner = searchCorner();
        double height = corner[1] - corner[0];
        double width = corner[3] - corner[2];
        int rows = (int) ((height) / BLOCK_SIZE);
        int cols = (int) ((width) / BLOCK_SIZE);
        double dx = width / cols;
        double dy = height / rows;
        blocks = new Block[rows][cols];
        // 左下角网格，经纬度均最小
        double[] lb = new double[]{corner[2], corner[0], corner[3] + dx, corner[1] + dy};
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Block block = new Block();
                block.id = i * cols + j;
                block.row = i;
                block.col = j;
                block.range = new double[]{
                        lb[0] + j * dx,
                        lb[1] + i * dy,
                        lb[0] + (j + 1) * dx,
                        lb[1] + (i + 1) * dy};
                blocks[i][j] = block;
            }
        }
        //接下来给每一个tip计算他所在的网格，填充网格的tipCount属性
        allocateTips();
        //接下来计算每个group中可以包含的tips个数为0的网格的个数
        calculateEmptyThreshold();
    }

    private static double[] searchCorner() {
        /*
        下上左右四个范围，分别是：纬度最低、纬度最高、经度最小、经度最大
         */
        double[] corners = new double[]{91, -1, 181, -1};
        for (double[] point : adminBorder) {
            corners[0] = Math.min(corners[0], point[1]);
            corners[1] = Math.max(corners[1], point[1]);
            corners[2] = Math.min(corners[2], point[0]);
            corners[3] = Math.max(corners[3], point[0]);
        }
        return corners;
    }

    private static void allocateTips() {
        for (Tip tip : tips) {
            int row = -1, col = -1;
            //二分搜索，通过纬度来确定行
            int left = 0, right = blocks.length;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                double[] range = blocks[mid][0].range;
                if (range[1] <= tip.point[1] && range[3] >= tip.point[1]) {
                    row = mid;
                    break;
                } else if (range[1] > tip.point[1]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            //二分搜索，通过经度确定列
            left = 0;
            right = blocks[0].length;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                double[] range = blocks[0][mid].range;
                if (range[0] <= tip.point[0] && range[2] >= tip.point[0]) {
                    col = mid;
                    break;
                } else if (range[0] > tip.point[0]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            if (row >= 0 && col >= 0) {
                blocks[row][col].tipsCount++;
            }
        }
    }

    private static void calculateEmptyThreshold() {
        int allEmpty = 0;
        for (Block[] row : blocks) {
            for (Block block : row) {
                allEmpty += block.tipsCount == 0 ? 1 : 0;
            }
        }
        // 这是一个平均值、近似值，肯定不是最终生成的group个数
        int groupSize = tips.size() / MAX_TIPS_SIZE;
        emptyThreshold = allEmpty * 2 / groupSize;
    }

    private static void mergeBlock() {
        Set<Integer> visited = new HashSet<>();
        // 经度小的在前；经度相等，纬度小的在前
        PriorityQueue<Block> pq = new PriorityQueue<>((o1, o2) ->
                (o1.col == o2.col ?
                        Integer.compare(o1.row, o2.row) :
                        Integer.compare(o1.col, o2.col)));
        int id = 0;
        for (Block[] rows : blocks) {
            Collections.addAll(pq, rows);
        }
        //八邻域
        int[][] ds = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        //四邻域
//        int[][] ds = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        while (!pq.isEmpty()) {
            while (!pq.isEmpty()
                    && (visited.contains(pq.peek().id) || pq.peek().geometry == null)) {
                pq.poll();
            }
            if (!pq.isEmpty()) {
                Block start = pq.poll();
                visited.add(start.id);
                //开始bfs搜索
                Queue<Block> que = new LinkedList<>();
                GroupBlock groupBlock = new GroupBlock();
                groupBlock.id = id++;
                groupBlock.blocks.add(start);
                groupBlock.tipsCount = start.tipsCount;
                que.offer(start);
                boolean flag = true;
                while (!que.isEmpty() && flag) {
                    int size = que.size();
                    for (int i = 0; i < size && flag; i++) {
                        // 取4邻域的4个block
                        Block cur = que.poll();
                        for (int[] d : ds) {
                            assert cur != null;
                            if (hasNextBlock(groupBlock, cur, visited, d)) {
                                Block next = blocks[cur.row + d[0]][cur.col + d[1]];
                                if (groupBlock.tipsCount >= MIN_TIPS_SIZE) {
                                    flag = false;
                                    break;
                                }
                                que.offer(next);
                                visited.add(next.id);
                                groupBlock.blocks.add(next);
                                groupBlock.tipsCount += next.tipsCount;
                            }
                        }
                    }
                }
                groupBlock.geometry = createGroupGeometry(groupBlock);
                groupBlocks.add(groupBlock);
            }
        }
    }

    private static boolean hasNextBlock(GroupBlock groupBlock, Block cur, Set<Integer> visited, int[] diff) {
        int nr = cur.row + diff[0], nc = cur.col + diff[1];
        if (nr >= 0 && nr < blocks.length && nc >= 0 && nc < blocks[0].length) {
            Block next = blocks[nr][nc];
            return !visited.contains(next.id)
                    && next.geometry != null
                    && groupBlock.tipsCount + next.tipsCount < MAX_TIPS_SIZE;
        }
        return false;
    }

    private static void saveGroup(String path) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        for (GroupBlock gb : groupBlocks) {
            bw.write(gb.id + "\t"
                    + gb.tipsCount + "\t"
                    + wktWriter.write(gb.geometry) + "\n");
        }
        bw.close();
    }

    private static Geometry createGroupGeometry(GroupBlock groupBlock) {
        List<Block> blocks = groupBlock.blocks;
        Geometry geometry = blocks.get(0).geometry;
        for (int i = 1; i < blocks.size(); i++) {
            geometry = geometry.union(blocks.get(i).geometry);
        }
        return geometry;
    }

    private static Coordinate[] getCoordinates(Block block) {
        double[] range = block.range;
        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = new Coordinate(range[0], range[1]);
        coordinates[1] = new Coordinate(range[2], range[1]);
        coordinates[2] = new Coordinate(range[2], range[3]);
        coordinates[3] = new Coordinate(range[0], range[3]);
        coordinates[4] = new Coordinate(range[0], range[1]);
        return coordinates;
    }

    /**
     * 这里计算每一个block是否在订单圈内，如果刚好在订单圈的边界上
     * 那么该block的geometry要与订单圈求交集。
     * 这个算法太暴力了如果block的个数太多，那么会相当慢。
     */
    private static void createBlockPolygon() {
        for (Block[] row : blocks) {
            for (Block block : row) {
                Polygon tmp = factory.createPolygon(getCoordinates(block));
                if (borderPolygon.intersects(tmp)) {
                    block.geometry = borderPolygon.intersection(tmp);
                }// else 该block不在订单圈内，因此polygon属性为null
            }
        }
    }

    /**
     * 这里通过四分法来计算每一个block的geometry。
     * 其实之前算法的瓶颈就是每个block都要与border做空间计算，现在优化这一步，
     * 优化方式就是将所有的block看作一个大的block，和border做空间计算：
     * 1.如果与border不想交，那么这个大的block的所有小的block都和border不相交；
     * 2.如果被border包含在内，那么所有小的block都被border包含在内，block的geometry就是自身坐标；
     * 3.如果与border相交，那么将这个大的block分成四等份，每一份都重复上面的运算。
     *
     * @param lbr 左下角的行索引
     * @param lbc 左下角的列索引
     * @param rtr 右上角的行索引
     * @param rtc 右上角的列索引
     */
    private static void createBlockPolygon2(int lbr, int lbc, int rtr, int rtc) {
        if (lbr > rtr || lbc > rtc) {
            return;
        }
        calCount++;
        //拿到block组的四个角的坐标，组成一个大的geometry，与border做空间运算
        double[] lbBlockRange = blocks[lbr][lbc].range;
        double[] rtBlockRange = blocks[rtr][rtc].range;
        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = new Coordinate(lbBlockRange[0], lbBlockRange[1]); //左下角
        coordinates[1] = new Coordinate(lbBlockRange[0], rtBlockRange[3]); //左上角
        coordinates[2] = new Coordinate(rtBlockRange[2], lbBlockRange[1]); //右下角
        coordinates[3] = new Coordinate(rtBlockRange[2], rtBlockRange[3]); //右上角
        coordinates[4] = new Coordinate(lbBlockRange[0], lbBlockRange[1]); //左下角
        Polygon polygon = factory.createPolygon(coordinates);
        if (borderPolygon.contains(polygon)) { //被包含
            for (int i = lbr; i <= rtr; i++) {
                for (int j = lbc; j <= rtc; j++) {
                    blocks[i][j].geometry = factory.createPolygon(getCoordinates(blocks[i][j]));
                }
            }
        } else if (borderPolygon.intersects(polygon)) { //相交
            if (lbr == rtr && lbc == rtc) { //如果该block组只有一个block
                Geometry geometry = factory.createPolygon(getCoordinates(blocks[lbr][lbc]));
                blocks[lbr][lbc].geometry = borderPolygon.intersection(geometry);
            } else { //有多个，需要四分
                int mr = (lbr + rtr) / 2, mc = (lbc + rtc) / 2;
                createBlockPolygon2(lbr, lbc, mr, mc); //左下部分
                createBlockPolygon2(mr + 1, lbc, rtr, mc); //左上部分
                createBlockPolygon2(lbr, mc + 1, mr, rtc); //右下部分
                createBlockPolygon2(mr + 1, mc + 1, rtr, rtc); //右上部分
            }
        } //else 不相交，那么所有的block均为null即可
    }

    private static void mergeSmallGroup() {
        List<GroupBlock> smallGroup = new ArrayList<>();
        List<GroupBlock> bigGroup = new ArrayList<>();
        int smallCount = MIN_TIPS_SIZE / 2;
        for (GroupBlock gb : groupBlocks) {
            if (gb.tipsCount <= smallCount) {
                smallGroup.add(gb);
            } else {
                bigGroup.add(gb);
            }
        }
        //将小的group合并到大的group中
        bigGroup.sort((o1, o2) -> (Integer.compare(o1.tipsCount, o2.tipsCount)));
        for (GroupBlock small : smallGroup) {
            for (GroupBlock big : bigGroup) {
                if (big.geometry.touches(small.geometry)) {
                    big.geometry = big.geometry.union(small.geometry);
                    break;
                }
            }
        }
        groupBlocks = bigGroup;
    }
}
