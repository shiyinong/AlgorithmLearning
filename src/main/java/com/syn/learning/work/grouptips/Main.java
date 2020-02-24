package com.syn.learning.work.grouptips;

import com.syn.learning.work.grouptips.model.Tip;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2020/2/22 17:48
 **/
public class Main {

    private static Map<String, String> borderMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        String borderPath = "C:\\work\\工单分组\\实验数据\\admin_border.txt";
        String tipsDir = "C:\\work\\工单分组\\实验数据\\tips\\";
        String resDir = "C:\\work\\工单分组\\实验数据\\结果\\";
        getBorders(borderPath);
        SplitOrderCircle splitOrderCircle = new SplitOrderCircle(0.00025, 1200, 800);
        File[] files = new File(tipsDir).listFiles();
        List<String[]> all = new ArrayList<>();
        long s = 0;
        for (File file : files) {
            String name = file.getName();
            if (file.isFile() && name.endsWith("cluster.txt")) {
                List<Tip> tips = getTips(file);
                String region = name.substring(0, name.indexOf("_"));
                String border = borderMap.get(region);
                long l = System.currentTimeMillis();
                List<String[]> res = splitOrderCircle.split(tips, border);
                long t = System.currentTimeMillis() - l;
                System.out.println(region + "\t耗时：" + t + " ms\t总耗时：" + (s += t) + " ms");
                saveData(resDir + region + ".txt", res);
                all.addAll(res);
                res.clear();
            }
        }
        saveData(resDir + "all.txt", all);
    }

    private static void saveData(String path, List<String[]> data) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        for (String[] row : data) {
            StringBuilder sb = new StringBuilder();
            for (String str : row) {
                sb.append(str).append("\t");
            }
            sb.replace(sb.length() - 1, sb.length(), "\n");
            bw.write(sb.toString());
        }
        bw.close();
    }

    private static List<Tip> getTips(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        List<Tip> tips = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String[] ss = line.split("\t");
            String[] p = ss[2].substring(6, ss[2].length() - 1).split(" ");
            Tip tip = new Tip();
            tip.setId(Integer.parseInt(ss[0]));
            tip.setLon(Double.parseDouble(p[0]));
            tip.setLat(Double.parseDouble(p[1]));
            tips.add(tip);
        }
        return tips;
    }

    private static void getBorders(String path) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] ss = line.split("\t");
            borderMap.put(ss[2], ss[0]);
        }
    }
}
