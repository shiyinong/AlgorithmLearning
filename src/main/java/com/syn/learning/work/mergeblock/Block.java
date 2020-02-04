package com.syn.learning.work.mergeblock;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/8/29 15:08
 **/
public class Block {
    private String id;
    private Integer count;
    private List<Block> adjacent = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Block> getAdjacent() {
        return adjacent;
    }

    public void setAdjacent(List<Block> adjacent) {
        this.adjacent = adjacent;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Block) {
            return this.id.equals(((Block) obj).id);
        }
        return false;
    }
}
