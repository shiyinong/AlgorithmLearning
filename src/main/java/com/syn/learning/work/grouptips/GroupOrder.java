package com.syn.learning.work.grouptips;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2020/2/19 10:13
 **/
public class GroupOrder {

    private static class Triangle {
        int id;
        double[][] points;
        //之所以是double的，是因为一个tips点可能属于多个三角形，因此会出现小数
        double tipsCount;
        int[] pointsId;
        boolean used;
        double[] center;
    }

    private static Map<Integer, double[]> points = new HashMap<>();
    private static Set<String> relation = new HashSet<>();
    private static List<Triangle> triangles = new ArrayList<>();
    private static int[] weight;
    private static int threshold = 1000;
    private static Map<Integer, List<Triangle>> triangleGroup = new HashMap<>();

    public static void main(String[] args) throws Exception {
        loadData();
        mergeTriangle();
        saveData("C:\\work\\工单分组\\Tips抓取\\提供研发\\group.txt");
    }

    private static void saveData(String path) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        for (Map.Entry<Integer, List<Triangle>> entry : triangleGroup.entrySet()) {
            for (Triangle triangle : entry.getValue()) {
                bw.write(entry.getKey() + "\t");
                StringBuilder wkt = new StringBuilder("POLYGON((");
                for (double[] p : triangle.points) {
                    wkt.append(p[0])
                            .append(" ")
                            .append(p[1])
                            .append(",");
                }
                wkt.append(triangle.points[0][0])
                        .append(" ")
                        .append(triangle.points[0][1])
                        .append("))\n");
                bw.write(wkt.toString());
            }
        }
        bw.close();
    }

    private static void mergeTriangle() {
        int groupId = 0;
        while (!triangles.isEmpty()) {
            triangleGroup.put(groupId, new ArrayList<>());
            Triangle corner = getCorner();
            triangles.sort((o1, o2) -> (Double.compare(getDis(corner, o1), getDis(corner, o2))));
            double count = 0;
            int i = 0;
            while (count < threshold && i < triangles.size()) {
                count += triangles.get(i).tipsCount;
                triangleGroup.get(groupId).add(triangles.get(i++));
            }
            triangles = triangles.subList(i, triangles.size());
            groupId++;
        }
    }

    private static double getDis(Triangle t1, Triangle t2) {
        double[] c1 = t1.center;
        double[] c2 = t2.center;
        return (c1[0] - c2[0]) * (c1[0] - c2[0]) + (c1[1] - c2[1]) * (c1[1] - c2[1]);
    }

    /**
     * 寻找经度最小的点作为起始点，若存在相同的经度，则选取纬度最小的点
     *
     * @return 该点的id
     */
    private static Triangle getCorner() {
        double[] corner = triangles.get(0).center;
        Triangle res = triangles.get(0);
        for (Triangle triangle : triangles) {
            double[] center = triangle.center;
            if (center[0] < corner[0] || (center[0] == corner[0] && center[1] < corner[1])) {
                res = triangle;
                corner = center;
            }
        }
        return res;
    }

    private static void loadData() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("C:\\work\\工单分组\\Tips抓取\\提供研发\\点.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] ss = line.split("\t");
            int k = Integer.parseInt(ss[0]);
            String[] ps = ss[1].split(",");
            double[] p = new double[]{Double.parseDouble(ps[0]), Double.parseDouble(ps[1])};
            points.put(k, p);
        }
        weight = new int[points.size()];
        int id = 0;
        BufferedReader br2 = new BufferedReader(new FileReader("C:\\work\\工单分组\\Tips抓取\\提供研发\\三角网.txt"));
        while ((line = br2.readLine()) != null) {
            String[] ss = line.split(",");
            int n1 = Integer.parseInt(ss[0]);
            int n2 = Integer.parseInt(ss[1]);
            int n3 = Integer.parseInt(ss[2]);
            relation.add("" + Math.min(n1, n2) + "_" + Math.max(n1, n2));
            relation.add("" + Math.min(n1, n3) + "_" + Math.max(n1, n3));
            relation.add("" + Math.min(n3, n2) + "_" + Math.max(n3, n2));
            double[] p1 = points.get(n1);
            double[] p2 = points.get(n2);
            double[] p3 = points.get(n3);
            Triangle triangle = new Triangle();
            triangle.pointsId = new int[]{n1, n2, n3};
            triangle.id = id++;
            triangle.points = new double[][]{p1, p2, p3};
            weight[n1] += isBorderPoint(n1) ? 0 : 1;
            weight[n2] += isBorderPoint(n2) ? 0 : 1;
            weight[n3] += isBorderPoint(n3) ? 0 : 1;
            triangle.center = new double[]{(p1[0] + p2[0] + p3[0]) / 3, (p1[1] + p2[1] + p3[1]) / 3};
            triangles.add(triangle);
        }

        for (Triangle triangle : triangles) {
            int[] pointsId = triangle.pointsId;
            triangle.tipsCount = (weight[pointsId[0]] == 0 ? 0 : 1d / weight[pointsId[0]])
                    + (weight[pointsId[1]] == 0 ? 0 : 1d / weight[pointsId[1]])
                    + (weight[pointsId[2]] == 0 ? 0 : 1d / weight[pointsId[2]]);
        }
    }

    private static boolean isBorderPoint(int id) {
        return id >= 20829;
    }
}
