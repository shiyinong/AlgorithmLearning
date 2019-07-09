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
        System.out.println("1000003".compareTo("111"));
    }

}


