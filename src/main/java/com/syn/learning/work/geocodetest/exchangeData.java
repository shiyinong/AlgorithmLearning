package com.syn.learning.work.geocodetest;

import com.syn.learning.utils.JDBCUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * geocoding旧算法数据转换
 *
 * @author shiyinong
 * @version 1.0
 * @date 2019/7/15 11:01
 **/
public class exchangeData {

    public static void main(String[] args) throws Exception {
        mergeData(args[0],args[1]);
    }

    private static void mergeData(String path,String dir) throws Exception {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(path),"GBK"));
        File[] files = new File(dir).listFiles();
        Map<Integer, BufferedWriter> map = new HashMap<>();
        for (File file : files) {
            String name = file.getName();
            if (name.charAt(0) == 'g') {
                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(file.getName(),true), "GBK"));
                map.put(Integer.parseInt(name.substring(13, 15)), bw);
            }
        }
        String line;
        while ((line = br.readLine()) != null) {
            String[] strs = line.split("\t");
            int key = Integer.parseInt(strs[2].substring(1, 3));
            if (map.containsKey(key)) {
                map.get(key).write(line+"\n");
            }
        }
        for (BufferedWriter bw : map.values()) {
            bw.close();
        }
    }

    private static void exchange(String path1,String path2) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(path1));
        BufferedWriter bw = new BufferedWriter(new FileWriter(path2));
        String line = br.readLine();
        String sql = "select admin_id \n" +
                "from admin_face \n" +
                "where st_coveredby(st_makeenvelope(?,?,?,?, 4326),geometry)";
        Connection conn = JDBCUtil.getConn();
        PreparedStatement pstmt = JDBCUtil.getPstmt(conn, sql);
        int i = 0;
        while ((line = br.readLine()) != null) {
            String[] strs = line.split(",");
            String address = strs[5].substring(1, strs[5].length() - 1)
                    + (int)Double.parseDouble(strs[2].substring(1, strs[2].length() - 1))
                    + "公里处";
            String kindCode = "AC09";
            double lon = Double.parseDouble(strs[14].substring(1, strs[14].length() - 1));
            double lat = Double.parseDouble(strs[15].substring(1, strs[15].length() - 1));
            pstmt.setDouble(1, lon);
            pstmt.setDouble(2, lat);
            pstmt.setDouble(3, lon);
            pstmt.setDouble(4, lat);
            ResultSet resultSet = pstmt.executeQuery();
            int admin = -1;
            while (resultSet.next()) {
                admin = resultSet.getInt("admin_id");
            }
            if (admin != -1) {
                String wkt = "POINT (" + lon + " " + lat + ")";
                bw.write("\"" + address + "\"\t\"" + kindCode + "\"\t\"" + admin + "\"\t\"" + wkt + "\"\n");
            }
            System.out.println(i++);
        }
        br.close();
        bw.close();
    }
}
