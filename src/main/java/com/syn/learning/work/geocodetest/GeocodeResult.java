package com.syn.learning.work.geocodetest;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/7/12 17:54
 **/
public class GeocodeResult {
    private String name;
    private Double longitude;
    private Double latitude;

    public GeocodeResult() {
    }

    public GeocodeResult(String name, Double longitude, Double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "GeocodeResult{" +
                "name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
