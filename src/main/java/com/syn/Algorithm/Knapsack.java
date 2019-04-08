package com.syn.Algorithm;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName Knapsack
 * @Description 背包问题
 * @Date 2019/3/29 10:56
 **/
public class Knapsack {


    /**
     * @description 01背包问题，即每个物体只存在一份。
     * @date 15:16 2019/3/29
     * @Param [weight, costs, w]
     * @return int
    **/
    public static int knapsack1(int[] weight,int[] costs,int w) {
        int len = weight.length;
//        int[][] dp = new int[len + 1][w + 1];
//        for (int i = 1; i < len + 1; i++) {
//            for (int j = 1; j < w + 1; j++) {
//                if (j >= weight[i - 1]) {
//                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - weight[i - 1]] + costs[i - 1]);
//                } else {
//                    dp[i][j] = dp[i - 1][j];
//                }
//            }
//        }
//        return dp[len][w];
        /**
         * 这里做出优化，空间复杂度由m*n降到m
         * 因为二维dp中的第i行结果只和第i-1行有关，所以可以只使用一维数组即可。
         * 这里注意，遍历一维数组时要从后往前遍历。因为要使用的是上一次遍历的结果
         */
        int[] dp = new int[w + 1];
        for (int i = 0; i < len; i++) {
            for (int j = w; j >=weight[i]; j--) {
                dp[j] = Math.max(dp[j], dp[j - weight[i]] + costs[i]);
            }
        }
        return dp[w];
    }

    /**
     * @description 完全背包问题，即每个物体可以取无限次。
     * @date 15:31 2019/3/29
     * @Param [weight, costs, w]
     * @return int
    **/
    public static int knapsack2(int[] weight,int[] costs,int w) {
        int len = weight.length;
        int[] dp = new int[w + 1];
        for (int i = 0; i < len; i++) {
            for (int j = weight[i]; j <= w; j++) { //与01背包问题的区别仅此而已。
                dp[j] = Math.max(dp[j], dp[j - weight[i]] + costs[i]);
            }
        }
        return dp[w];
    }


    /**
     * @description 多重背包问题，即给定每个物体的个数
     * @date 15:32 2019/3/29
     * @Param [weight, costs, w]
     * @return int
    **/
    public static int knapsack3(int[] weight,int[] costs,int w) {
        int len = weight.length;
        int[] dp = new int[w + 1];
        for (int i = 0; i < len; i++) {
            for (int j = weight[i]; j <= w; j++) { //与01背包问题的区别仅此而已。
                dp[j] = Math.max(dp[j], dp[j - weight[i]] + costs[i]);
            }
        }
        return dp[w];
    }

}
