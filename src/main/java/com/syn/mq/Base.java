package com.syn.mq;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName Base
 * @Description TODO
 * @Date 2019/5/10 15:34
 **/
public abstract class Base {
    final static int capacity=15;
    static int numOfRemain=1000;
    static Queue<Integer> que=new LinkedList<>();
}
