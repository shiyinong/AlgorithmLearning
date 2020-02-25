package com.syn.learning.work.grouptips;

import com.syn.learning.work.grouptips.model.Tip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger logger = LoggerFactory.getLogger(SplitOrderCircle.class);


    public static void main(String[] args) throws Exception {
        String borderPath = "C:\\work\\工单分组\\实验数据\\admin_border.txt";
        String tipsDir = "C:\\work\\工单分组\\实验数据\\tips\\";
        String resDir = "C:\\work\\工单分组\\实验数据\\结果\\";
        getBorders(borderPath);
        double size = 0.0005;
        SplitOrderCircle splitOrderCircle = new SplitOrderCircle(size, 1200, 800);
        File[] files = new File(tipsDir).listFiles();
        List<String[]> all = new ArrayList<>();
        long s = System.currentTimeMillis();
        logger.info("********************************网格精度为{}时***************************************", size);
        for (File file : files) {
            String name = file.getName();
            if (file.isFile() && name.endsWith("cluster.txt")) {
                List<Tip> tips = getTips(file);
                String region = name.substring(0, name.indexOf("_"));
                logger.info(region);
                String border = borderMap.get(region);
                long l = System.currentTimeMillis();
                List<String[]> res = splitOrderCircle.split(tips, border);
                logger.info("------------{}耗时：{}---------------", region, (System.currentTimeMillis() - l));
                logger.info(" ");
                saveData(resDir + region + ".txt", res);
                all.addAll(res);
                res.clear();
            }
        }
        logger.info("********************************总耗时：{}***************************************\n",
                (System.currentTimeMillis() - s));
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
