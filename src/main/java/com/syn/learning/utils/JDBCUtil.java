package com.syn.learning.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * @author shiyinong
 * @version 1.0
 * @ClassName JDBCUtil
 * @Description jdbc工具类
 * @Date 2019/5/30 9:51
 **/
public class JDBCUtil {

    static private String url;
    static private String userName;
    static private String password;

    static {
        InputStream ins=ClassLoader.getSystemResourceAsStream("jdbc.properties");
        Properties properties=new Properties();
        try {
            properties.load(ins);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String driver=properties.getProperty("jdbc.driver");
        url=properties.getProperty("jdbc.url");
        userName=properties.getProperty("jdbc.username");
        password=properties.getProperty("jdbc.password");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 获取连接
     * @date 10:21 2019/5/30
     * @Param []
     * @return java.sql.Connection
    **/
    public static Connection getConn(){
        Connection conn=null;
        try {
            conn= DriverManager.getConnection(url,userName,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * @description 获取pstmt
     * @date 17:05 2019/6/3
     * @Param [conn, sql]
     * @return java.sql.PreparedStatement
    **/
    public static PreparedStatement getPstmt(Connection conn,String sql){
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * @description 更新，插入，删除
     * @date 10:33 2019/5/30
     * @Param [conn, sql, params]
     * @return int -1表示更新失败
    **/
    public static int update(PreparedStatement pstmt,Object... params){
        for(int i=1;i<=params.length;i++){
            try {
                pstmt.setObject(i,params[i-1]);
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        }
        int cnt;
        try {
            cnt = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return cnt;
    }

//    /**
//     * @description 查询函数
//     * @date 13:48 2019/5/30
//     * @Param [pstmt, T, params]
//     * @return java.util.List<T>
//    **/
//    public static <T extends ResultSetHandler> List<T> query(PreparedStatement pstmt, Class clazz, Object... params){
//        List<T> res=new ArrayList<>();
//        for(int i=1;i<=params.length;i++){
//            try {
//                pstmt.setObject(i,params[i-1]);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        ResultSet rs=null;
//        try {
//            rs = pstmt.executeQuery();
//            while(rs.next()){
//                T t = (T)clazz.newInstance();
//                t.handle(rs);
//                res.add(t);
//            }
//        }catch (SQLException|InstantiationException|IllegalAccessException e) {
//            e.printStackTrace();
//        }finally {
//            JDBCUtil.closeResultSet(rs);
//        }
//        return res;
//    }

    /**
     * @description 关闭conn, stmt
     * @date 9:58 2019/5/30
     * @Param [conn, stmt]
     * @return void
    **/
    public static void closeAll(Connection conn,Statement stmt){
        closeConn(conn);
        closeStatement(stmt);
    }

    /**
     * @description TODO关闭conn, stmt, rs
     * @date 9:59 2019/5/30
     * @Param [conn, stmt, rs]
     * @return void
    **/
    public static void closeAll(Connection conn,Statement stmt,ResultSet rs){
        closeAll(conn,stmt);
        closeResultSet(rs);
    }

    /**
     * @description 关闭conn
     * @date 9:55 2019/5/30
     * @Param [conn]
     * @return void
    **/
    private static void closeConn(Connection conn){
        if(null!=conn){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @description 关闭stmt
     * @date 9:56 2019/5/30
     * @Param [stmt]
     * @return void
    **/
    private static void closeStatement(Statement stmt){
        if(null!=stmt){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @description 关闭rs
     * @date 9:57 2019/5/30
     * @Param [rs]
     * @return void
    **/
    private static void closeResultSet(ResultSet rs){
        if(null!=rs){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
