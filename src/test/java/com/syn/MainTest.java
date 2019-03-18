package com.syn;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName com.syn.Main
 * @Description ”√”⁄≤‚ ‘À„∑®
 * @Date 2019/3/15 10:19
 **/
public class MainTest {
    public static <T> void printArr(T[] arr) {
        if (arr.length == 0) return;
        for (T t : arr) {
            System.out.print(t + "\t");
        }
        System.out.println();
    }
    public static void printArr(int[] arr){
        if (arr.length == 0) return;
        for (int t : arr) {
            System.out.print(t + "\t");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr = {5,3,7,6,8,8,7,6,5,6,7,8,109,209,87,5,32,4,5,5,3,2,5,7,7,5,3,23,45,67,654,34,567,6,543,5,67,6543,45,67,654};
        int[] arr1={5,7,3,1,8,6};
        Sorts.popSort(arr);
        printArr(arr);
    }
}
