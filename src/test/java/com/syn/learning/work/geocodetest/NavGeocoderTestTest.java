package com.syn.learning.work.geocodetest;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NavGeocoderTestTest {

    private String path = "./data/电子眼.txt";
    private String savePath = "./data/电子眼结果.txt";

    @Test
    public void navGeocoderTest() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        BufferedWriter bw = new BufferedWriter(new FileWriter(savePath));
        List<GeocodeAddress> addresses = new ArrayList<>();
        String line = br.readLine();
        int i=0;
        while ((line = br.readLine()) != null) {
            String[] strs = line.split("\t");
            Double lon = Double.parseDouble(strs[5]);
            Double lat = Double.parseDouble(strs[6]);
            GeocodeAddress geocodeAddress = new GeocodeAddress();
            geocodeAddress.setCityCode(Integer.parseInt(strs[3]));
            geocodeAddress.setName(strs[4] + strs[2]);
            GeocodeResult geocodeResult = NavGeocoderTest.navGeocoderTest(geocodeAddress);
            double dis = Math.sqrt(Math.pow(geocodeResult.getLongitude() - lon, 2)
                    + Math.pow(geocodeResult.getLatitude() - lat, 2));
            bw.write(geocodeAddress.getName() + "\t" +
                    lon.toString() + "\t" +
                    lat.toString() + "\t"
                    + geocodeResult.getName() + "\t"
                    + geocodeResult.getLongitude() + "\t"
                    + geocodeResult.getLatitude() + "\n");
            System.out.println(i++ +"  "+geocodeAddress.getName()+"  "+geocodeResult.toString());
        }

        bw.close();
        br.close();
    }
}