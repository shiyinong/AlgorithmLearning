package com.syn.learning.work.grouptips.model;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2020/2/22 10:26
 **/
public class Tip {
    private Integer id;
    private Double lon;
    private Double lat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
