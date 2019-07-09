package com.syn.learning.algorithm;

import java.util.PriorityQueue;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName MergeStones
 * @Description 石子合并问题
 * @Date 2019/6/26 17:29
 **/
public class MergeStones {

    /**
     * 有n堆石子，第i堆有stones[i]个石子，现在要把这n堆合并为一堆，
     * 每次可以任意选择两堆合并，成本为这两堆的总数量，问最小成本
     * @param stones 石堆
     * @return 最小成本
     */
    public static int mergeStones1(int[] stones) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int stone : stones) {
            pq.add(stone);
        }
        while (pq.size() > 1) {
            int num = pq.poll();
            num += pq.poll();
            pq.add(num);
        }
        return pq.poll();
    }

    /**
     * 有n堆石子，第i堆有stones[i]个石子，现在要把这n堆合并为一堆，
     * 每次只能选择相邻的两堆合并，成本为这两堆的总数量，问最小成本
     * @param stones 石堆
     * @return 最小成本
     */
    public static int mergeStones2(int[] stones) {
        int len=stones.length;
        int[] sum=new int[len+1];
        int[][] dp=new int[len][len];
        for(int i=0;i<len;i++){
            sum[i+1]=sum[i]+stones[i];
        }
        for(int l=2;l<=len;l++){ //区间长度
            for(int j=l-1;j<len;j++){
                int i=j-l+1;
                int s=sum[j+1]-sum[i];
                dp[i][j]=(j-i)<=1?s:Integer.MAX_VALUE;
                for(int k=i;k<j;k++){
                    dp[i][j]=Math.min(dp[i][j],dp[i][k]+dp[k+1][j]+s);
                }
            }
        }
        return dp[0][len-1];
    }
}
