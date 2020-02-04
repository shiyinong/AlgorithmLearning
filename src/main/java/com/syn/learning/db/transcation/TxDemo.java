package com.syn.learning.db.transcation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/12/23 15:34
 **/
public class TxDemo {
    public static void main(String[] args) throws Exception{
        Class.forName("");
        Connection connection = DriverManager.getConnection("");
        PreparedStatement preparedStatement = connection.prepareStatement("");
        boolean execute = preparedStatement.execute();
    }
}
