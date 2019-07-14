package com.syn.learning.work;


import java.util.ArrayList;
import java.util.List;

/**
 * 车道线平移算法
 *
 * @author shiyinong
 * @version 1.0
 * @date 2019/7/10 10:23
 **/
public class LineTranslation {

    /**
     * 最小距离阈值
     */
    private static final double MIN_DIS = 1e-10;

    /**
     * 车道线平移函数
     *
     * @param wktLine  输入线wkt格式
     * @param wktPoint 输入点wkt格式
     * @return 结果
     */
    public static String lineTranslation(String wktLine, String wktPoint) {
        List<double[]> coordinates = wkt2coordinates(wktLine);
        double[][][] lines = coordinates2Lines(coordinates);
        double[] point = wkt2coordinates(wktPoint).get(0);
        double minDis = Double.MAX_VALUE;
        int minIdx = -1;
        // 求点与所有线段的距离，选择距离最短的一个线段
        for (int i = 0; i < lines.length; i++) {
            double tmpDis = disBetweenPointAndLine(lines[i][0], lines[i][1], point);
            if (tmpDis < minDis) {
                minDis = tmpDis;
                minIdx = i;
            }
        }
        int side = getSideOfPointAndLine(lines[minIdx][0], lines[minIdx][1], point);
        System.out.println(minDis+"   "+side);
        // 平移后的线段
        double[][][] newLines = new double[lines.length][][];
        minDis /= 2;
        for (int i = 0; i < lines.length; i++) {
            double[] a = translation(lines[i][0], lines[i][1], minDis, side);
            double[] b = translation(lines[i][1], lines[i][0], minDis, -side);
            newLines[i] = new double[][]{a, b};
        }
        return connect(newLines);
    }

    /**
     * wkt转成坐标形式，使用数组存储
     *
     * @param wkt 输入
     * @return 坐标
     */
    private static List<double[]> wkt2coordinates(String wkt) {
        List<double[]> coordinates = new ArrayList<>();
        wkt = wkt.substring(wkt.indexOf("(") + 1, wkt.indexOf(")"));
        String[] strs = wkt.split(",");
        for (String str : strs) {
            String[] coordinateStr = str.split(" ");
            double[] coordinate = new double[3];
            coordinate[0] = Double.parseDouble(coordinateStr[0].trim());
            coordinate[1] = Double.parseDouble(coordinateStr[1].trim());
            if (coordinateStr.length > 2) {
                coordinate[2] = Double.parseDouble(coordinateStr[2].trim());
            }
            coordinates.add(coordinate);
        }
        return coordinates;
    }

    /**
     * 将相邻的两个坐标点，作为一条线段存储
     * 假如有三个点:a b c，那么能组成2个线段:ab bc，以此类推
     *
     * @param coordinates 所有坐标点
     * @return 所有线段
     */
    private static double[][][] coordinates2Lines(List<double[]> coordinates) {
        double[][][] lines = new double[coordinates.size() - 1][2][];
        for (int i = 0; i < lines.length; i++) {
            lines[i][0] = new double[]{coordinates.get(i)[0]
                    , coordinates.get(i)[1], coordinates.get(i)[2]};
            lines[i][1] = new double[]{coordinates.get(i + 1)[0]
                    , coordinates.get(i + 1)[1], coordinates.get(i + 1)[2]};
        }
        return lines;
    }

    /**
     * 求点与线段的距离
     *
     * @param a 直线ab的端点
     * @param b 直线ab的端点
     * @param c 直线ab外的点
     * @return 距离
     */
    private static double disBetweenPointAndLine(double[] a, double[] b, double[] c) {
        if ((a[0] == c[0] && a[1] == c[1]) || (b[0] == c[0] && b[1] == c[1])) return 0;
        // 判断∠cab是否为锐角，通过向量ac和向量ab的内积来判断
        double[] vac = new double[]{c[0] - a[0], c[1] - a[1]};
        double[] vab = new double[]{b[0] - a[0], b[1] - a[1]};
        double inner = vac[0] * vab[0] + vac[1] * vab[1];
        // 如果内积<=0，说明∠cab不是锐角，返回ca的距离即可
        if (inner <= 0) {
            return Math.sqrt(vac[0] * vac[0] + vac[1] * vac[1]);
        }

        //判断∠cba是否为钝角，通过向量bc和向量ba的内积来判断
        double[] vbc = new double[]{c[0] - b[0], c[1] - b[1]};
        double[] vba = new double[]{a[0] - b[0], a[1] - b[1]};
        inner = vbc[0] * vba[0] + vbc[1] * vba[1];
        // 如果内积<=0，说明∠cba不是锐角，返回cb的距离即可
        if (inner <= 0) {
            return Math.sqrt(vbc[0] * vbc[0] + vbc[1] * vbc[1]);
        }

        //如果两个角度都是锐角，那么说明c对ab做垂线后垂足在ab上，通过向量外积来求垂距
        double outer = vac[0] * vab[1] - vac[1] * vab[0];
        return Math.abs(outer) / Math.sqrt(vab[0] * vab[0] + vab[1] * vab[1]);
    }

    /**
     * 判断点c在线段ab的哪一侧，这里的线段是有方向的: a->b
     * 左侧返回-1，右侧返回1，c在ab上返回0
     *
     * @param a 直线ab端点a
     * @param b 直线ab端点b
     * @param c 直线外的点c
     * @return c在ab的哪一侧
     */
    private static int getSideOfPointAndLine(double[] a, double[] b, double[] c) {
        double s = (a[0] - c[0]) * (b[1] - c[1]) - (a[1] - c[1]) * (b[0] - c[0]);
        return s == 0 ? 0 : (s > 0 ? -1 : 1);
    }

    /**
     * 线段ab沿着法线和side方向，移动dis单位之后的线段a端点的坐标
     *
     * @param a    直线ab的端点
     * @param b    直线ab的端点
     * @param dis  移动的单位
     * @param side 移动的方向
     * @return 移动后的a点坐标
     */
    private static double[] translation(double[] a, double[] b, double dis, int side) {
        double[] vab = new double[]{b[0] - a[0], b[1] - a[1]};
        if (Math.abs(vab[0]) <= MIN_DIS) {
            return new double[]{a[0] + (vab[1] > 0 ? 1 : -1) * side * dis, a[1]};
        } else if (Math.abs(vab[1]) <= MIN_DIS) {
            return new double[]{a[0], a[1] + (vab[0] > 0 ? -1 : 1) * side * dis};
        } else {
            double k = -1 / (vab[1] / vab[0]);
            // 求x时，要通过side以及b点所处的象限，来判断x正负
            double x = Math.sqrt(dis * dis / (k * k + 1));
            x *= vab[1] > 0 ? side : -side;
            double y = x * k;
            return new double[]{x + a[0], y + a[1], a[2]};
        }
    }

    /**
     * 把所有的线段连成一条线
     *
     * @param lines 所有平移后的线段
     * @return 最终结果
     */
    private static String connect(double[][][] lines) {
        int len = lines.length;
        StringBuilder wkt = new StringBuilder("LINESTRING(");
        // 添加第一条线段的第一个端点
        wkt.append(lines[0][0][0]);
        wkt.append(" ");
        wkt.append(lines[0][0][1]);
        wkt.append(" ");
        wkt.append(lines[0][0][2]);
        wkt.append(",");
        for (int i = 0; i < len - 1; i++) {
            double[] p1 = lines[i][1];
            double[] p2 = lines[i + 1][0];
            wkt.append((p1[0] + p2[0]) / 2);
            wkt.append(" ");
            wkt.append((p1[1] + p2[1]) / 2);
            wkt.append(" ");
            wkt.append((p1[2] + p2[2]) / 2);
            wkt.append(",");
        }
        // 添加最右一条线段的第二个端点
        wkt.append(lines[len - 1][1][0]);
        wkt.append(" ");
        wkt.append(lines[len - 1][1][1]);
        wkt.append(" ");
        wkt.append(lines[len - 1][1][2]);
        wkt.append(",");
        wkt.setCharAt(wkt.length() - 1, ')');
        return wkt.toString();
    }
}
