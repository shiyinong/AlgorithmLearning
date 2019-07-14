package com.syn.learning;

import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

import java.lang.reflect.Array;
import java.security.cert.TrustAnchor;
import java.util.*;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName com.syn.Main
 * @Description 测试算法
 * @Date 2019/3/15 10:19
 **/
public class MainTest {
    public static <T> void printArr(T[] arr) {
        for (T t : arr) {
            System.out.print(t + "\t");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        List<List<String>> people = new ArrayList<>();
        List<String> l1 = new ArrayList<>();
        l1.add("java");
        List<String> l2 = new ArrayList<>();
        l2.add("nodejs");
        List<String> l3 = new ArrayList<>();
        l3.add("nodejs");
        l3.add("reactjs");
        people.add(l1);
        people.add(l2);
        people.add(l3);
    }

}


