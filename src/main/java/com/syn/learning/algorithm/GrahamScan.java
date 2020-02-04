package com.syn.learning.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Graham-Scan算法，用于生成凸包，时间复杂度O(N*logN)
 *
 * @author shiyinong
 * @version 1.0
 * @date 2020/2/3 10:38
 **/
public class GrahamScan {

    /**
     * 凸包生成算法
     *
     * @param points 待生成凸包的点
     * @return 凸包
     */
    public static List<double[]> grahamScan(List<double[]> points) {
        if (points.size() < 3) {
            return new ArrayList<>();
        }
        points = sortPoints(points);
        Stack<double[]> stack = new Stack<>();
        stack.add(points.get(0));
        stack.add(points.get(1));
        for (int i = 2; i < points.size(); i++) {
            double[] c = points.get(i);
            while (stack.size() > 1) {
                double[] b = stack.pop();
                double[] a = stack.peek();
                // 如果角度小于180度
                if (checkAngle(a, b, c)) {
                    stack.add(b);
                    break;
                }// else 去掉点b
            }
            stack.add(c);
        }
        List<double[]> res = new ArrayList<>(stack);
        // 以此形成一个闭环
        res.add(res.get(0));
        return res;
    }

    /**
     * 给所有的点排个序，选取最左边的点作为极坐标的极点，然后根据极角和极距升序排序
     *
     * @param points 所有点
     */
    private static List<double[]> sortPoints(List<double[]> points) {
        class Point {
            private double lon;
            private double lat;
            private double polarAngle;
            private double polarDis;

            private Point(double lon, double lat, double polarAngle, double polarDis) {
                this.lon = lon;
                this.lat = lat;
                this.polarAngle = polarAngle;
                this.polarDis = polarDis;
            }
        }
        // 选取极点
        double[] btPoint = points.get(0);
        for (double[] p : points) {
            if (p[0] < btPoint[0] || (p[0] == btPoint[0] && p[1] < btPoint[1])) {
                btPoint = p;
            }
        }
        List<Point> points2 = new ArrayList<>();
        double[] top = new double[]{btPoint[0], btPoint[1] + 100};
        for (double[] p : points) {
            // 计算极角和极距
            points2.add(new Point(p[0], p[1], calculateAngle(btPoint, top, p), calculateDis(btPoint, p)));
        }
        points2.sort((o1, o2) -> (o1.polarAngle == o2.polarAngle ?
                Double.compare(o1.polarDis, o2.polarDis) : Double.compare(o1.polarAngle, o2.polarAngle)));
        List<double[]> res = new ArrayList<>();
        for (Point p : points2) {
            res.add(new double[]{p.lon, p.lat});
        }
        return res;
    }

    /**
     * 计算向量ab和向量ac的夹角
     *
     * @param a 点a
     * @param b 点b
     * @param c 点c
     * @return 向量ab和向量ac的夹角
     */
    private static double calculateAngle(double[] a, double[] b, double[] c) {
        double[] vab = new double[]{b[0] - a[0], b[1] - a[1]};
        double[] vac = new double[]{c[0] - a[0], c[1] - a[1]};
        double inner = vab[0] * vac[0] + vab[1] * vac[1];
        double mod = Math.sqrt(vab[0] * vab[0] + vab[1] * vab[1]);
        double mod2 = Math.sqrt(vac[0] * vac[0] + vac[1] * vac[1]);
        double cos = inner / (mod * mod2);
        cos = Math.max(cos, -1);
        cos = Math.min(cos, 1);
        return Math.acos(cos) * 180 / Math.PI;
    }

    /**
     * 计算点a和点b的欧氏距离
     *
     * @param a 点a
     * @param b 点b
     * @return 欧氏距离
     */
    private static double calculateDis(double[] a, double[] b) {
        double dx = a[0] - b[0];
        double dy = a[1] - b[1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 检查向量ba和向量bc的夹角是否小于180度，这个角度是向量ba逆时针转到向量bc的角度
     *
     * @param a 点a
     * @param b 点b
     * @param c 点c
     * @return 向量ba和向量bc的夹角
     */
    private static boolean checkAngle(double[] a, double[] b, double[] c) {
        double[] vba = new double[]{a[0] - b[0], a[1] - b[1]};
        double[] vbc = new double[]{c[0] - b[0], c[1] - b[1]};
        return (vba[0] * vbc[1] - vba[1] * vbc[0]) > 0;
    }
}
