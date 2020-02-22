package com.syn.learning.work.grouptips.model;

import java.util.Arrays;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2020/2/22 10:36
 **/
public class Edge {
    private Integer id;
    /**
     * a、b两点的方向：
     * 1.对于横边，a在右，b在左
     * 2.对于竖边：a在下，b在上
     * 这个顺序必须保证
     */
    private double[] a;
    private double[] b;
    /**
     * 该边是一个横边还是竖边
     */
    private Boolean vertical;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double[] getA() {
        return a;
    }

    public void setA(double[] a) {
        this.a = a;
    }

    public double[] getB() {
        return b;
    }

    public void setB(double[] b) {
        this.b = b;
    }

    public Boolean getVertical() {
        return vertical;
    }

    public void setVertical(Boolean vertical) {
        this.vertical = vertical;
    }

    public Edge(int id, double[] a, double[] b, Boolean vertical) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.vertical = vertical;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[]{a[0], a[1], b[0], b[1]});
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            Edge edge = (Edge) obj;
            return edge.a[0] == this.a[0]
                    && edge.a[1] == this.a[1]
                    && edge.b[0] == this.b[0]
                    && edge.b[1] == this.b[1];
        }
        return false;
    }
}
