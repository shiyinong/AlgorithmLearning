package com.syn.learning.work.geocodetest;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NavGeocoderTestTest {

    @Test
    public void navGeocoderTest() throws IOException {
        String path = "./data/电子眼.txt";
        String savePath = "./data/电子眼结果6.txt";
        BufferedReader br = new BufferedReader(new FileReader(path));
        BufferedWriter bw = new BufferedWriter(new FileWriter(savePath));
        String line = br.readLine();
        int i = 0;
        long s = 0;
        while ((line = br.readLine()) != null) {
            String[] strs = line.split("\t");
            Double lon = Double.parseDouble(strs[5]);
            Double lat = Double.parseDouble(strs[6]);
            GeocodeAddress geocodeAddress = new GeocodeAddress();
            geocodeAddress.setCityCode(Integer.parseInt(strs[3]));
            strs[4] = strs[4].replace('|', ' ');
            geocodeAddress.setName(strs[4] + " " + strs[2]);
            long l = System.currentTimeMillis();
            GeocodeResult geocodeResult = NavGeocoderTest.navGeocoderTest(geocodeAddress);
            s += System.currentTimeMillis() - l;
            System.out.println(i + "\t" + (s / ++i) + " ---  " + (System.currentTimeMillis() - l));
            double dis = Math.sqrt(Math.pow(geocodeResult.getLongitude() - lon, 2)
                    + Math.pow(geocodeResult.getLatitude() - lat, 2));
            dis = (int) (dis * 100000);
            bw.write(strs[4] + "\t" + strs[2] + "\t" +
                    lon.toString() + "\t" +
                    lat.toString() + "\t"
                    + geocodeResult.getName() + "\t"
                    + geocodeResult.getConfidence() + "\t"
                    + dis + "\n");
        }

        bw.close();
        br.close();
    }

    @Test
    public void navGeocoderTest2() throws IOException {
        String path = "./data/2019-7-18/北京违章热点.txt";
        String savePath = "./data/2019-7-18/北京违章热点结果6.txt";
        BufferedReader br = new BufferedReader(new FileReader(path));
        BufferedWriter bw = new BufferedWriter(new FileWriter(savePath));
        String line = br.readLine();
        int i = 1;
        long s = 0;
        while ((line = br.readLine()) != null) {
            String[] strs = line.split("\t");
            Double lon = Double.parseDouble(strs[0]);
            Double lat = Double.parseDouble(strs[1]);
            GeocodeAddress geocodeAddress = new GeocodeAddress();
            geocodeAddress.setName(strs[2]);
            long l = System.currentTimeMillis();
            GeocodeResult geocodeResult = NavGeocoderTest.navGeocoderTest(geocodeAddress);
            s += System.currentTimeMillis() - l;
            System.out.println(i + "\t" + (s / ++i) + " ---  " + (System.currentTimeMillis() - l) + "\t" + geocodeAddress.getName());
            double dis = Math.sqrt(Math.pow(geocodeResult.getLongitude() - lon, 2)
                    + Math.pow(geocodeResult.getLatitude() - lat, 2));
            dis = (int) (dis * 100000);
            bw.write(strs[2] + "\t"
                    + geocodeResult.getName() + "\t"
                    + geocodeResult.getLongitude() + "\t"
                    + geocodeResult.getLatitude() + "\t"
                    + geocodeResult.getConfidence() + "\t"
                    + dis + "\n");
            //System.out.println(i++ + "  " + dis);
        }

        bw.close();
        br.close();
    }

    @Test
    public void test3() throws IOException{
        String path="./data/青岛市即墨区.txt",savePath="./data/青岛市即墨区结果.txt";
        BufferedReader br=new BufferedReader(new FileReader(path));
        BufferedWriter bw = new BufferedWriter(new FileWriter(savePath));
        String line=br.readLine();
        while((line=br.readLine())!=null){
            String[] strs=line.split("\t");
            GeocodeAddress geocodeAddress = new GeocodeAddress();
            geocodeAddress.setName(strs[1]);
            geocodeAddress.setCityCode(370215);
            Double lon = Double.parseDouble(strs[3]);
            Double lat = Double.parseDouble(strs[4]);
            long l = System.currentTimeMillis();
            GeocodeResult geocodeResult = NavGeocoderTest.navGeocoderTest(geocodeAddress);
            long t=System.currentTimeMillis()-l;
            double dis = Math.sqrt(Math.pow(geocodeResult.getLongitude() - lon, 2)
                    + Math.pow(geocodeResult.getLatitude() - lat, 2));
            dis = (int) (dis * 100000);
            System.out.println(dis + " ---  " + geocodeAddress.getName()+"  ---  "+geocodeResult.getName());
            bw.write(geocodeAddress.getName() + "\t"
                    + geocodeResult.getName() + "\t"
                    + geocodeResult.getLongitude() + "\t"
                    + geocodeResult.getLatitude() + "\t"
                    + geocodeResult.getConfidence() + "\t"
                    + dis + "\n");
        }
        bw.close();
        br.close();
    }
}