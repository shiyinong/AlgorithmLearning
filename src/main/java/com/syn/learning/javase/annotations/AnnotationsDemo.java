package com.syn.learning.javase.annotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName AnnotationsDemo
 * @Description TODO
 * @Date 2019/6/16 15:32
 **/
public class AnnotationsDemo {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        String[][] data = {{"aa1", "bb1", "cc1", "dd1"}
                , {"aa2", "bb2", "cc2", "dd2"}, {"aa3", "bb3", "cc3", "dd3"}
                , {"aa4", "bb4", "cc4", "dd4"}, {"aa5", "bb5", "cc5", "dd5"}};
        List<ExcelData> list = new ArrayList<>();
        List<ExcelData> res = readData(data, ExcelDataImpl.class);
        for (ExcelData excelData : res) {
            System.out.println(excelData);
        }
    }
    private static List<ExcelData> readData(String[][] data,Class clazz) throws IllegalAccessException, InstantiationException {
        List<ExcelData> res = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        Map<Field, Integer> map = new HashMap<>();
        for (Field field : fields) {
            ExcelField annotation = field.getAnnotation(ExcelField.class);
            if (annotation != null) {
                field.setAccessible(true);
                map.put(field, annotation.value());
            }
        }
        for (String[] str : data) {
            ExcelData excelData = (ExcelData) clazz.newInstance();
            for (Map.Entry<Field, Integer> entry : map.entrySet()) {
                entry.getKey().set(excelData, str[entry.getValue()]);
            }
            res.add(excelData);
        }
        return res;
    }

}
