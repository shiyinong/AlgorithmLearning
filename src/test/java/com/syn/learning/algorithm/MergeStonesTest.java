package com.syn.learning.algorithm;

import org.junit.Test;


public class MergeStonesTest {

    @Test
    public void mergeStones1() {
        int[] stones=new int[]{5,4,6,7,5,4,3,5,9};
        System.out.println(MergeStones.mergeStones1(stones));
    }

    @Test
    public void mergeStones2() {
        int[] stones=new int[]{33,4,3};
        System.out.println(MergeStones.mergeStones2(stones));
    }
}