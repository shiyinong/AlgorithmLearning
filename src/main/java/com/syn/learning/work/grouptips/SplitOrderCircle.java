package com.syn.learning.work.grouptips;


import com.syn.learning.work.grouptips.model.Block;
import com.syn.learning.work.grouptips.model.Edge;
import com.syn.learning.work.grouptips.model.GroupBlock;
import com.syn.learning.work.grouptips.model.Tip;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2020/2/20 10:24
 **/
public class SplitOrderCircle {
    private Geometry borderGeometry;
    private List<double[]> adminBorder;
    private List<Tip> tips;
    /**
     * 生成的网格，n*m大小,blocks[0][0]是经度最小并且纬度也最小的网格，即地图上的左下角点
     */
    private Block[][] blocks;
    private List<GroupBlock> groupBlocks = new ArrayList<>();
    private GeometryFactory factory = new GeometryFactory();
    private WKTWriter wktWriter = new WKTWriter();
    private WKTReader wktReader = new WKTReader();
    private double blockSize;
    private int maxTipsSize;
    private int minTipsSize;
    private int calculateCount;
    private static Logger logger = LoggerFactory.getLogger(SplitOrderCircle.class);

    public SplitOrderCircle(double blockSize, int maxTipsSize, int minTipsSize) {
        this.blockSize = blockSize;
        this.maxTipsSize = maxTipsSize;
        this.minTipsSize = minTipsSize;
    }

    /**
     * 主函数
     *
     * @param tips      该订单圈内的tips
     * @param borderWkt 该订单圈的边界，wkt格式
     * @return 分割后的工单圈，wkt格式
     */
    public List<String[]> split(List<Tip> tips, String borderWkt) {
        List<String[]> res = new ArrayList<>();
        if (tips.size() <= minTipsSize) {
            res.add(new String[]{"0", "" + tips.size(), borderWkt});
            return res;
        }
        calculateCount = 0;
        long l = System.currentTimeMillis();

        //加载数据
        load(tips, borderWkt);

        //根据border生成所有的block
        generateBlocks();
        logger.info("生成block耗时：{}", (System.currentTimeMillis() - l));

        //计算每一个block的geometry
        l = System.currentTimeMillis();
        createBlockPolygon();
        logger.info("计算block的geometry耗时：{}", (System.currentTimeMillis() - l));
        logger.info("block总个数：{}, 空间计算次数：{}", blocks.length * blocks[0].length, calculateCount);

        //计算每一个tip落在哪个block中
        allocateTips();

        //checkBlockGeometry
        checkBlockGeometry();

        //合并block，合并后得到若干个groupBlock
        l = System.currentTimeMillis();
        mergeBlock();
        logger.info("合并block：{}", (System.currentTimeMillis() - l));

        //将tips不足阈值的group合并到与之相邻的大的group中
        mergeSmallGroup();

        //输出wkt格式
        res = convert2Wkt();

        // release
        release();
        return res;
    }

    private void load(List<Tip> tips, String borderWkt) {
        this.tips = tips;
        try {
            this.borderGeometry = wktReader.read(borderWkt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.adminBorder = new ArrayList<>();
        assert borderGeometry != null;
        Coordinate[] coordinates = borderGeometry.getCoordinates();
        for (Coordinate c : coordinates) {
            this.adminBorder.add(new double[]{c.x, c.y});
        }
    }

    /**
     * 生成网格。单位均是度，不是米或千米
     */
    private void generateBlocks() {
        // 下上左右四个边界
        double[] corner = searchCorner();
        double height = corner[1] - corner[0];
        double width = corner[3] - corner[2];
        int rows = (int) (height / blockSize);
        int cols = (int) (width / blockSize);
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
                block.setLbPoint(new double[]{lb[0] + j * dx, lb[1] + i * dy});
                block.setLtPoint(new double[]{lb[0] + j * dx, lb[1] + (i + 1) * dy});
                block.setRbPoint(new double[]{lb[0] + (j + 1) * dx, lb[1] + i * dy});
                block.setRtPoint(new double[]{lb[0] + (j + 1) * dx, lb[1] + (i + 1) * dy});
                block.setTipsCount(0);
                blocks[i][j] = block;
            }
        }
    }

    private double[] searchCorner() {
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

    private void createBlockPolygon() {
        createBlockPolygonHelper(0, 0, blocks.length - 1, blocks[0].length - 1);
    }

    /**
     * 这里计算每一个block是否在订单圈内，如果刚好在订单圈的边界上
     * 那么该block的geometry要与订单圈求交集。
     * 这个算法太暴力了，如果block的个数太多，那么会相当慢。
     * 这里通过四分法来计算每一个block的geometry。
     * 其实之前算法的瓶颈就是每个block都要与border做空间计算，现在优化这一步，
     * 优化方式就是将所有的block看作一个大的block，和border做空间计算：
     * 1.如果与border不相交，那么这个大的block中所有小的block都和border不相交；
     * 2.如果被border包含在内，那么所有小的block都被border包含在内，block的geometry就是自身坐标；
     * 3.如果与border相交，那么将这个大的block分成四等份，每一份都重复上面的运算。
     *
     * @param lbr 左下角的行索引
     * @param lbc 左下角的列索引
     * @param rtr 右上角的行索引
     * @param rtc 右上角的列索引
     */
    private void createBlockPolygonHelper(int lbr, int lbc, int rtr, int rtc) {
        if (lbr > rtr || lbc > rtc) {
            return;
        }
        calculateCount++;
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
        if (borderGeometry.contains(polygon)) { //被包含
            for (int i = lbr; i <= rtr; i++) {
                for (int j = lbc; j <= rtc; j++) {
                    blocks[i][j].setValid(true);
                }
            }
        } else if (borderGeometry.intersects(polygon)) { //相交
            if (lbr == rtr && lbc == rtc) { //如果该block组只有一个block
                blocks[lbr][lbc].setValid(true);
            } else { //有多个，需要四分
                int mr = (lbr + rtr) / 2, mc = (lbc + rtc) / 2;
                createBlockPolygonHelper(lbr, lbc, mr, mc); //左下部分
                createBlockPolygonHelper(mr + 1, lbc, rtr, mc); //左上部分
                createBlockPolygonHelper(lbr, mc + 1, mr, rtc); //右下部分
                createBlockPolygonHelper(mr + 1, mc + 1, rtr, rtc); //右上部分
            }
        } //else 不相交，那么所有的block均为null即可
    }

    private void allocateTips() {
        for (Tip tip : tips) {
            int row = -1, col = -1;
            //二分搜索，通过纬度来确定行
            int left = 0, right = blocks.length - 1;
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
            right = blocks[0].length - 1;
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

    private void checkBlockGeometry() {
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                // 不能出现中空现象,当前限制考虑一个空格
                if (!blocks[i][j].getValid()
                        && i > 0 && blocks[i - 1][j].getValid()
                        && j > 0 && blocks[i][j - 1].getValid()
                        && i < blocks.length - 1 && blocks[i + 1][j].getValid()
                        && j < blocks[0].length - 1 && blocks[i][j + 1].getValid()) {
                    blocks[i][j].setValid(true);
                }
            }
        }
    }

    private void mergeBlock() {
        Set<Integer> visited = new HashSet<>();
        // 经度小的在前；经度相等，纬度小的在前
        PriorityQueue<Block> pq = new PriorityQueue<>((o1, o2) ->
                (o1.getCol().equals(o2.getCol()) ?
                        Integer.compare(o1.getRow(), o2.getRow()) :
                        Integer.compare(o1.getCol(), o2.getCol())));
        int groupId = 0;
        for (Block[] rows : blocks) {
            Collections.addAll(pq, rows);
        }
        //八邻域，顺序分别是：下，左，上，右，左下，左上，右上，右下。顺序不能变，因为后面有用到
        int[][] ds = new int[][]{{-1, 0}, {0, -1}, {1, 0}, {0, 1}, {-1, -1}, {1, -1}, {1, 1}, {-1, 1}};
        while (!pq.isEmpty()) {
            Block start = pq.poll();
            if (!visited.contains(start.getId()) && start.getValid()) {
                visited.add(start.getId());
                searchBlock(start, visited, ds, groupId++);
            }
        }
    }

    private void searchBlock(Block start, Set<Integer> visited, int[][] ds, int groupId) {
        Queue<Block> que = new LinkedList<>();
        GroupBlock groupBlock = new GroupBlock();
        groupBlock.setId(groupId);
        groupBlock.getBlocks().add(start);
        groupBlock.setTipsCount(start.getTipsCount());
        que.offer(start);
        boolean flag = true;
        //开始bfs搜索
        while (!que.isEmpty() && flag) {
            int size = que.size();
            for (int i = 0; i < size && flag; i++) {
                // 取8邻域的8个block
                Block cur = que.poll();
                boolean[] hasAdd = new boolean[8];
                for (int j = 0; j < 8; j++) {
                    int[] d = ds[j];
                    assert cur != null;
                    if (hasNextBlock(groupBlock, cur, visited, d)) {
                        Block next = blocks[cur.getRow() + d[0]][cur.getCol() + d[1]];
                        if (groupBlock.getTipsCount() >= minTipsSize) {
                            flag = false;
                            break;
                        } else if (j > 3 && !hasAdd[j - 4] && !hasAdd[(j - 3) % 4]) {
                            /*
                             八邻域中，四个边角。如果想加入边角，那么必须得先加入四邻域中与之相邻的格子
                             这样是为了防止对角线现象出现
                            */
                            continue;
                        }
                        hasAdd[j] = true;
                        que.offer(next);
                        visited.add(next.getId());
                        groupBlock.getBlocks().add(next);
                        groupBlock.addTipsCount(next.getTipsCount());
                    }
                }
            }
        }
        groupBlock.setGeometry(createGroupGeometry(groupBlock));
        groupBlocks.add(groupBlock);
    }

    private boolean hasNextBlock(GroupBlock groupBlock, Block cur, Set<Integer> visited, int[] diff) {
        int nr = cur.getRow() + diff[0];
        int nc = cur.getCol() + diff[1];
        if (nr >= 0 && nr < blocks.length && nc >= 0 && nc < blocks[0].length) {
            Block next = blocks[nr][nc];
            return !visited.contains(next.getId())
                    && next.getValid()
                    && groupBlock.getTipsCount() + next.getTipsCount() < maxTipsSize;
        }
        return false;
    }

    /**
     * 把group中所有的block的geometry进行合并，合并为一个大的多边形，很耗时，因为要做n次空间运算
     * 这里给出一个优化过的合并block.geometry的算法
     *
     * @param groupBlock group
     * @return geometry
     */
    private Geometry createGroupGeometry(GroupBlock groupBlock) {
        //key：edge；value：该edge的数量
        Map<Edge, Integer> edges = getEdgeFromGroupBlock(groupBlock);
        //key：点的坐标hashCode；val：该点所属的个数为1的edge
        Map<Integer, List<Edge>> pointEdges = new HashMap<>();
        double[] curPoint;
        Edge curEdge = null;
        // 所有只出现了一次的edge，就是最终多边形的envelope的一部分。现在要做的就是将这些边组成一个多边形。
        for (Map.Entry<Edge, Integer> entry : edges.entrySet()) {
            if (entry.getValue() == 1) {
                Edge edge = entry.getKey();
                int key = Arrays.hashCode(edge.getA());
                pointEdges.computeIfAbsent(key, k -> new ArrayList<>());
                pointEdges.get(key).add(edge);
                key = Arrays.hashCode(edge.getB());
                pointEdges.computeIfAbsent(key, k -> new ArrayList<>());
                pointEdges.get(key).add(edge);
                if (curEdge == null) {
                    curEdge = edge;
                }
            }
        }
        List<double[]> polygonPoints = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        assert curEdge != null;
        visited.add(curEdge.getId());
        polygonPoints.add(curEdge.getB());
        polygonPoints.add(curPoint = curEdge.getA());
        while (true) {
            List<Edge> es = pointEdges.get(Arrays.hashCode(curPoint));
            assert es.size() == 2;
            Edge nextEdge = visited.contains(es.get(0).getId()) ? es.get(1) : es.get(0);
            if (visited.contains(nextEdge.getId())) { //说明已经多边形已经闭合了
                break;
            }
            double[] nextPoint = (nextEdge.getA()[0] == curPoint[0]
                    && nextEdge.getA()[1] == curPoint[1]) ?
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
        return getPolygonFromPoints(polygonPoints).intersection(borderGeometry);
//        Geometry res = null;
//        try {
//            res = getPolygonFromPoints(polygonPoints).intersection(borderGeometry);
//        } catch (TopologyException e) {
//            String ss = wktWriter.write(createGroupGeometry(groupBlock));
//            String s = wktWriter.write(getPolygonFromPoints(polygonPoints));
//            e.printStackTrace();
//        }
    }

    private Map<Edge, Integer> getEdgeFromGroupBlock(GroupBlock groupBlock) {
        int id = 0;
        Map<Edge, Integer> edges = new HashMap<>();
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
        return edges;
    }

    private Polygon getPolygonFromPoints(List<double[]> points) {
        Coordinate[] coordinates = new Coordinate[points.size() + 1];
        for (int i = 0; i < points.size(); i++) {
            coordinates[i] = getCoordinate(points.get(i));
        }
        coordinates[points.size()] = coordinates[0];
        return factory.createPolygon(coordinates);
    }

    private Coordinate getCoordinate(double[] point) {
        return new Coordinate(point[0], point[1]);
    }

    private void mergeSmallGroup() {
        List<GroupBlock> smallGroup = new ArrayList<>();
        List<GroupBlock> bigGroup = new ArrayList<>();
        int smallCount = minTipsSize / 2;
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

    private List<String[]> convert2Wkt() {
        List<String[]> res = new ArrayList<>();
        for (GroupBlock gb : groupBlocks) {
            String[] ss = new String[3];
            ss[0] = "" + gb.getId();
            ss[1] = "" + gb.getTipsCount();
            ss[2] = wktWriter.write(gb.getGeometry());
            res.add(ss);
        }
        return res;
    }

    private void release() {
        groupBlocks.clear();
        blocks = null;
        borderGeometry = null;
        adminBorder.clear();
        tips.clear();
    }
}
