package com.syn.learning.algorithm;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GrahamScanTest {

    @Test
    public void grahamScan() throws Exception {
        String region = "北京市北京市城区_5058";
        String p = "C:\\work\\工单分组\\Tips抓取\\提供研发\\实验\\" + region + "_cluster.txt";
        String p2 = "C:\\work\\工单分组\\Tips抓取\\提供研发\\实验\\" + region + "_java_convex.txt";
        BufferedReader br = new BufferedReader(new FileReader(p));
        String line;
        Map<Integer, List<double[]>> map = new HashMap<>();
        while ((line = br.readLine()) != null) {
            String[] strs = line.split("\t");
            String[] ps = strs[2].substring(6, strs[2].length() - 1).split(" ");
            double[] point = new double[]{Double.parseDouble(ps[0]), Double.parseDouble(ps[1])};
            int key = Integer.parseInt(strs[1]);
            List<double[]> val = map.getOrDefault(key, new ArrayList<>());
            val.add(point);
            map.put(key, val);
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(p2));
        for (Map.Entry<Integer, List<double[]>> entry : map.entrySet()) {
            List<double[]> res = GrahamScan.grahamScan(entry.getValue());
            StringBuilder sb = new StringBuilder("" + entry.getKey() + "\tLINESTRING(");
            for (double[] point : res) {
                sb.append(point[0])
                        .append(" ")
                        .append(point[1])
                        .append(",");
            }
            sb.replace(sb.length() - 1, sb.length(), ")\n");
            bw.write(sb.toString());
            System.out.println(entry.getValue().size());
        }
        bw.close();

    }
}