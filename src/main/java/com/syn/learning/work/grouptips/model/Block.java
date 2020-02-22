package com.syn.learning.work.grouptips.model;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2020/2/22 10:28
 **/
public class Block {
    private Integer id;
    private Integer row;
    private Integer col;
    private Integer tipsCount;
    private Geometry geometry;
    private Double[] lbPoint; //左下角坐标
    private Double[] ltPoint; //左上角坐标
    private Double[] rbPoint; //右下角坐标
    private Double[] rtPoint; //右上角坐标

    public Block() {
        this.tipsCount = 0;
    }

    public void addTipsCount(int cnt) {
        this.tipsCount += cnt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getTipsCount() {
        return tipsCount;
    }

    public void setTipsCount(Integer tipsCount) {
        this.tipsCount = tipsCount;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Double[] getLbPoint() {
        return lbPoint;
    }

    public void setLbPoint(Double[] lbPoint) {
        this.lbPoint = lbPoint;
    }

    public Double[] getLtPoint() {
        return ltPoint;
    }

    public void setLtPoint(Double[] ltPoint) {
        this.ltPoint = ltPoint;
    }

    public Double[] getRbPoint() {
        return rbPoint;
    }

    public void setRbPoint(Double[] rbPoint) {
        this.rbPoint = rbPoint;
    }

    public Double[] getRtPoint() {
        return rtPoint;
    }

    public void setRtPoint(Double[] rtPoint) {
        this.rtPoint = rtPoint;
    }


}
