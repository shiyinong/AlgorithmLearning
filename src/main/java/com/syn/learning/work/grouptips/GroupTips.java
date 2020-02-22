package com.syn.learning.work.grouptips;


import com.syn.learning.work.grouptips.model.Block;
import com.syn.learning.work.grouptips.model.Edge;
import com.syn.learning.work.grouptips.model.GroupBlock;
import com.syn.learning.work.grouptips.model.Tip;
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
    private static GeometryFactory factory = new GeometryFactory();
    private static WKTWriter wktWriter = new WKTWriter();
    private static Polygon borderPolygon;
    private static List<Double[]> adminBorder = new ArrayList<>();
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
    private static final int MIN_TIPS_SIZE = 800;

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
                adminBorder.add(new Double[]{Double.parseDouble(ss[0]), Double.parseDouble(ss[1])});
            }
        }
    }

    private static void createBorderPolygon() {
        borderPolygon = getPolygonFromPoints(adminBorder);
    }

    private static void loadTips(String path) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            String[] ss = line.split("\t");
            String[] p = ss[2].substring(6, ss[2].length() - 1).split(" ");
            Tip tip = new Tip();
            tip.setId(Integer.parseInt(ss[0]));
            tip.setLon(Double.parseDouble(p[0]));
            tip.setLat(Double.parseDouble(p[1]));
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
        double[] lb = new double[]{corner[2], corner[0]};
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Block block = new Block();
                block.setId(i * cols + j);
                block.setRow(i);
                block.setCol(j);
                block.setLbPoint(new Double[]{lb[0] + j * dx, lb[1] + i * dy});
                block.setLtPoint(new Double[]{lb[0] + j * dx, lb[1] + (i + 1) * dy});
                block.setRbPoint(new Double[]{lb[0] + (j + 1) * dx, lb[1] + i * dy});
                block.setRtPoint(new Double[]{lb[0] + (j + 1) * dx, lb[1] + (i + 1) * dy});
                block.setTipsCount(0);
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
        for (Double[] point : adminBorder) {
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
                Block midBlock = blocks[mid][0];
                if (midBlock.getLbPoint()[1] <= tip.getLat()
                        && midBlock.getLtPoint()[1] >= tip.getLat()) {
                    row = mid;
                    break;
                } else if (midBlock.getLbPoint()[1] > tip.getLat()) {
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
                Block midBlock = blocks[0][mid];
                if (midBlock.getLbPoint()[0] <= tip.getLon()
                        && midBlock.getRbPoint()[0] >= tip.getLon()) {
                    col = mid;
                    break;
                } else if (midBlock.getLbPoint()[0] > tip.getLon()) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            if (row >= 0 && col >= 0) {
                blocks[row][col].addTipsCount(1);
            }
        }
    }

    private static void calculateEmptyThreshold() {
        int allEmpty = 0;
        for (Block[] row : blocks) {
            for (Block block : row) {
                allEmpty += block.getTipsCount() == 0 ? 1 : 0;
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
                (o1.getCol().equals(o2.getCol()) ?
                        Integer.compare(o1.getRow(), o2.getRow()) :
                        Integer.compare(o1.getCol(), o2.getCol())));
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
                    && (visited.contains(pq.peek().getId()) || pq.peek().getGeometry() == null)) {
                pq.poll();
            }
            if (!pq.isEmpty()) {
                Block start = pq.poll();
                visited.add(start.getId());
                //开始bfs搜索
                Queue<Block> que = new LinkedList<>();
                GroupBlock groupBlock = new GroupBlock();
                groupBlock.setId(id++);
                groupBlock.getBlocks().add(start);
                groupBlock.setTipsCount(start.getTipsCount());
                groupBlock.setGeometry(start.getGeometry());
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
                                Block next = blocks[cur.getRow() + d[0]][cur.getCol() + d[1]];
                                if (groupBlock.getTipsCount() >= MIN_TIPS_SIZE) {
                                    flag = false;
                                    break;
                                }
                                que.offer(next);
                                visited.add(next.getId());
                                groupBlock.getBlocks().add(next);
                                groupBlock.addTipsCount(next.getTipsCount());
                            }
                        }
                    }
                }
                groupBlock.setGeometry(createGroupGeometry2(groupBlock));
//                groupBlock.geometry = createGroupGeometry(groupBlock);
                groupBlocks.add(groupBlock);
            }
        }
    }

    private static boolean hasNextBlock(GroupBlock groupBlock, Block cur, Set<Integer> visited, int[] diff) {
        int nr = cur.getRow() + diff[0];
        int nc = cur.getCol() + diff[1];
        if (nr >= 0 && nr < blocks.length && nc >= 0 && nc < blocks[0].length) {
            Block next = blocks[nr][nc];
            return !visited.contains(next.getId())
                    && next.getGeometry() != null
                    && groupBlock.getTipsCount() + next.getTipsCount() < MAX_TIPS_SIZE;
        }
        return false;
    }

    private static void saveGroup(String path) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        for (GroupBlock gb : groupBlocks) {
            bw.write(gb.getId() + "\t"
                    + gb.getTipsCount() + "\t"
                    + wktWriter.write(gb.getGeometry()) + "\n");
        }
        bw.close();
    }

    /**
     * 这里是把group中所有的block的geometry进行合并，合并为一个大的多边形
     * 这个很耗时，因为要做n次空间运算
     *
     * @param groupBlock 一个group
     * @return 这个group的最终的geometry
     */
    private static Geometry createGroupGeometry(GroupBlock groupBlock) {
        List<Block> blocks = groupBlock.getBlocks();
        Geometry geometry = blocks.get(0).getGeometry();
        for (int i = 1; i < blocks.size(); i++) {
            geometry = geometry.union(blocks.get(i).getGeometry());
        }
        return geometry;
    }

    /**
     * 这里给出一个优化过的合并block.geometry的算法
     *
     * @param groupBlock group
     * @return geometry
     */
    private static Geometry createGroupGeometry2(GroupBlock groupBlock) {
        //key：edge；value：该edge的数量
        Map<Edge, Integer> edges = new HashMap<>();
        //key：点的坐标；val：该点所属的个数为1的edge
        Map<String, List<Edge>> pointEdges = new HashMap<>();
        int id = 0;
        for (Block block : groupBlock.getBlocks()) {
            // 一个block有4条边
            Edge edge1 = new Edge(id++, block.getLbPoint(), block.getLtPoint(), true); //左边
            Edge edge2 = new Edge(id++, block.getRtPoint(), block.getLtPoint(), false); //上边
            Edge edge3 = new Edge(id++, block.getRbPoint(), block.getRtPoint(), true); //右边
            Edge edge4 = new Edge(id++, block.getRbPoint(), block.getLbPoint(), false); //下边
            edges.put(edge1, edges.getOrDefault(edge1, 0) + 1);
            edges.put(edge2, edges.getOrDefault(edge2, 0) + 1);
            edges.put(edge3, edges.getOrDefault(edge3, 0) + 1);
            edges.put(edge4, edges.getOrDefault(edge4, 0) + 1);
        }
        Double[] curPoint;
        Edge curEdge = null;
        // 所有只出现了一次的edge，就是最终多边形的envelope的一部分。现在要做的就是将这些边组成一个多边形。
        for (Map.Entry<Edge, Integer> entry : edges.entrySet()) {
            if (entry.getValue() == 1) {
                Edge edge = entry.getKey();
                String key = "" + edge.getA()[0] + "_" + edge.getA()[1];
                pointEdges.computeIfAbsent(key, k -> new ArrayList<>());
                pointEdges.get(key).add(edge);
                key = "" + edge.getB()[0] + "_" + edge.getB()[1];
                pointEdges.computeIfAbsent(key, k -> new ArrayList<>());
                pointEdges.get(key).add(edge);
                if (curEdge == null) {
                    curEdge = edge;
                }
            }
        }
        List<Double[]> polygonPoints = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        assert curEdge != null;
        visited.add(curEdge.getId());
        polygonPoints.add(curEdge.getB());
        polygonPoints.add(curPoint = curEdge.getA());
        while (true) {
            List<Edge> es = pointEdges.get("" + curPoint[0] + "_" + curPoint[1]);
            assert es.size() == 2;
            Edge nextEdge = visited.contains(es.get(0).getId()) ? es.get(1) : es.get(0);
            if (visited.contains(nextEdge.getId())) { //说明已经多边形已经闭合了
                break;
            }
            Double[] nextPoint = (nextEdge.getA()[0].equals(curPoint[0])
                    && nextEdge.getA()[1].equals(curPoint[1])) ?
                    nextEdge.getB() :
                    nextEdge.getA();
            //这里判断下当前edge和下一条edge是否是平行的
            if (curEdge.getVertical() == nextEdge.getVertical()) {
                polygonPoints.remove(polygonPoints.size() - 1);
            }
            polygonPoints.add(nextPoint);
            visited.add(nextEdge.getId());
            curEdge = nextEdge;
            curPoint = nextPoint;
        }
        return getPolygonFromPoints(polygonPoints).intersection(borderPolygon);
    }

    private static Polygon getPolygonFromPoints(List<Double[]> points) {
        Coordinate[] coordinates = new Coordinate[points.size() + 1];
        for (int i = 0; i < points.size(); i++) {
            coordinates[i] = getCoordinate(points.get(i));
        }
        coordinates[points.size()] = coordinates[0];
        return factory.createPolygon(coordinates);
    }


    private static Coordinate[] getCoordinates(Block block) {
        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = getCoordinate(block.getLbPoint());
        coordinates[1] = getCoordinate(block.getLtPoint());
        coordinates[2] = getCoordinate(block.getRtPoint());
        coordinates[3] = getCoordinate(block.getRbPoint());
        coordinates[4] = coordinates[0];
        return coordinates;
    }

    private static Coordinate getCoordinate(Double[] point) {
        return new Coordinate(point[0], point[1]);
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
                    block.setGeometry(borderPolygon.intersection(tmp));
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
        Block lbBlock = blocks[lbr][lbc];
        Block ltBlock = blocks[rtr][lbc];
        Block rtBlock = blocks[rtr][rtc];
        Block rbBlock = blocks[lbr][rtc];
        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = getCoordinate(lbBlock.getLbPoint()); //左下角
        coordinates[1] = getCoordinate(ltBlock.getLtPoint()); //左上角
        coordinates[2] = getCoordinate(rtBlock.getRtPoint()); //右上角
        coordinates[3] = getCoordinate(rbBlock.getRbPoint()); //右下角
        coordinates[4] = coordinates[0]; //左下角
        Polygon polygon = factory.createPolygon(coordinates);
        if (borderPolygon.contains(polygon)) { //被包含
            for (int i = lbr; i <= rtr; i++) {
                for (int j = lbc; j <= rtc; j++) {
                    blocks[i][j].setGeometry(factory.createPolygon(getCoordinates(blocks[i][j])));
                }
            }
        } else if (borderPolygon.intersects(polygon)) { //相交
            if (lbr == rtr && lbc == rtc) { //如果该block组只有一个block
                blocks[lbr][lbc].setGeometry(factory.createPolygon(getCoordinates(blocks[lbr][lbc])));
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
            if (gb.getTipsCount() <= smallCount) {
                smallGroup.add(gb);
            } else {
                bigGroup.add(gb);
            }
        }
        //将小的group合并到大的group中
        bigGroup.sort((o1, o2) -> (Integer.compare(o1.getTipsCount(), o2.getTipsCount())));
        for (GroupBlock small : smallGroup) {
            for (GroupBlock big : bigGroup) {
                if (big.getGeometry().touches(small.getGeometry())) {
                    big.setGeometry(big.getGeometry().union(small.getGeometry()));
                    break;
                }
            }
        }
        groupBlocks = bigGroup;
    }
}
