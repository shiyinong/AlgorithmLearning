package com.syn.learning;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}

class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int x) {
        val = x;
    }
}

public class MainTest {

    ListNode ln1 = new ListNode(1);
    ListNode ln2 = new ListNode(2);
    ListNode ln3 = new ListNode(3);
    ListNode ln4 = new ListNode(4);
    ListNode ln5 = new ListNode(5);

    {
        ln1.next = ln2;
        ln2.next = ln3;
        ln3.next = ln4;
        ln4.next = ln5;
    }

    public static <T> void printArr(T[] arr) {
        for (T t : arr) {
            System.out.print(t + " ");
        }
        System.out.println();
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    private void swap(List<Integer> list, int i, int j) {
        int tmp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, tmp);
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static void main(String[] args) throws Exception {

    }

    @Test
    public void test() {
        int a=maxJumps(new int[]{7,6,5,4,3,2,1},1);
    }

    public int maxJumps(int[] arr, int d) {
        int len=arr.length,ans=1;
        int[] dp=new int[len];
        int[][] idx=new int[len][];
        for(int i=0;i<len;i++){
            idx[i]=new int[]{arr[i],i};
        }
        Arrays.sort(idx,(o1,o2)->(Integer.compare(o1[0],o2[0])));
        for(int[] ii:idx){
            int i=ii[1];
            int l=Math.max(0,i-d);
            for(int j=i-1;j>=l;j--){
                if(arr[i]>arr[j]){
                    dp[i]=Math.max(dp[i],dp[j]+1);
                }else{
                    break;
                }
            }
            int r=Math.min(len-1,i+d);
            for(int j=i+1;j<=r;j++){
                if(arr[i]>arr[j]){
                    dp[i]=Math.max(dp[i],dp[j]+1);
                }else{
                    break;
                }
            }
            ans=Math.max(ans,dp[i]+1);
        }
        return ans;
    }


}
