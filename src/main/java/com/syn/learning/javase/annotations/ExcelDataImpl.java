package com.syn.learning.javase.annotations;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName ExcelDataImpl
 * @Description TODO
 * @Date 2019/6/16 15:35
 **/
public class ExcelDataImpl implements ExcelData {
    @ExcelField(3)
    private String name;
    @ExcelField(2)
    private String password;
    private int id;

    @Override
    public String toString() {
        return name+"   "+password;
    }
}
