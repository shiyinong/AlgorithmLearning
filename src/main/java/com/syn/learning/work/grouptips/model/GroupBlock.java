package com.syn.learning.work.grouptips.model;

import com.vividsolutions.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2020/2/22 10:33
 **/
public class GroupBlock {
    private Integer id;
    private Integer tipsCount;
    private List<Block> blocks;
    private Geometry geometry;

    public GroupBlock() {
        blocks = new ArrayList<>();
        tipsCount = 0;
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

    public Integer getTipsCount() {
        return tipsCount;
    }

    public void setTipsCount(Integer tipsCount) {
        this.tipsCount = tipsCount;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
