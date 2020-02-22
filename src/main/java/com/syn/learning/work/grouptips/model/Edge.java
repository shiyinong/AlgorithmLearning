package com.syn.learning.work.grouptips.model;

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
    private Double[] a;
    private Double[] b;
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

    public Double[] getA() {
        return a;
    }

    public void setA(Double[] a) {
        this.a = a;
    }

    public Double[] getB() {
        return b;
    }

    public void setB(Double[] b) {
        this.b = b;
    }

    public Boolean getVertical() {
        return vertical;
    }

    public void setVertical(Boolean vertical) {
        this.vertical = vertical;
    }

    public Edge(int id, Double[] a, Double[] b, Boolean vertical) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.vertical = vertical;
    }

    @Override
    public int hashCode() {
        return ("" + this.a[0] + "_"
                + this.a[1] + "_"
                + this.b[0] + "_"
                + this.b[1]).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            Edge edge = (Edge) obj;
            return edge.a[0].equals(this.a[0])
                    && edge.a[1].equals(this.a[1])
                    && edge.b[0].equals(this.b[0])
                    && edge.b[1].equals(this.b[1]);
        }
        return false;
    }
}
