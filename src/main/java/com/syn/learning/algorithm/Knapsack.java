package com.syn.learning.algorithm;


import java.util.Arrays;

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
        int[] dp = new int[w + 1]; //当背包容量为j时，能装得最多的货物是dp[j]
        for (int i = 0; i < len; i++) {
            for (int j = w; j >= weight[i]; j--) { //从后往前遍历，不会重复装多个货物i
                // 状态转移方程：dp[i] 不装第i个货物。dp[j - weight[i]] + costs[i] 装第i个货物
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
            for (int j = weight[i]; j <= w; j++) { //与01背包问题的区别仅此而已。从前往后遍历，可能会重复装多个货物i
                dp[j] = Math.max(dp[j], dp[j - weight[i]] + costs[i]);
            }
        }
        return dp[w];
    }


    /**
     * @description 多重背的包问题，即给定每个物体个数
     * @date 15:32 2019/3/29
     * @Param [weight, costs, w]
     * @return int
    **/
    public static int knapsack3(int[] weight,int[] costs,int w) {
        // 简单思路：将该问题转化为01背包问题。
        int len = weight.length;
        int[] dp = new int[w + 1];
        for (int i = 0; i < len; i++) {
            for (int j = w; j >=weight[i]; j--) {
                dp[j] = Math.max(dp[j], dp[j - weight[i]] + costs[i]);
            }
        }
        return dp[w];
    }

    /**
     * @description 背包问题变种，给定一个数组，从数组中选取两堆数组成两个子集，要求这两个子集的sum相等，求这个sum最大是多少
     * @date 9:32 2019/4/19
     * @Param [rods] 数组长度最大为20，元素和最大为5000。
     * @return int
    **/
    public static int tallestBillboard(int[] rods) {
        int len=rods.length,sum=0;
        int[][] dp=new int[len+1][5001];
        for (int i = 0; i < len+1; i++){
            Arrays.fill(dp[i],-5001);
        }
        dp[0][0] = 0;
        /**
         * 这里写点我的理解。为什么要把dp所有的元素都初始化为-5001，dp[0][0]为0。
         * 两层循环，第一层遍历元素i，没什么说的；第二层遍历二者的sum差j，注意到，
         * 这个j并不一定是客观存在的，如arr={3,5}，当i==2时遍历j，j会从0遍历到3，
         * 但实际上j只能取2和3，其他的值都是无法3和5构成的。这里如果不初始化，
         * 一些无法构成的sum差，就会有>0的值出现。如遍历到i==2，j==1时，显然对于3和5来说
         * ，不可能有差为4的情况，但是通过
         * dp[i][Math.abs(j-h)]=Math.max(dp[i][Math.abs(j-h)],dp[i-1][j]+Math.min(j,h));
         * 之后，就会出现dp[2][4]=1.所以需要初始化，这样初始化之后，所有不可能出现的情况就都为<0的值了。
         */
        for(int i=1;i<=len;i++){
            int h=rods[i-1];
            sum+=h;
            for(int j=0;j+h<=sum;j++){
                dp[i][j]=Math.max(dp[i][j],dp[i-1][j]);
                if(j+h<=sum) {
                    dp[i][j+h]=Math.max(dp[i][j+h],dp[i-1][j]);
                }
                dp[i][Math.abs(j-h)]=Math.max(dp[i][Math.abs(j-h)],dp[i-1][j]+Math.min(j,h));
            }
        }
        return dp[len][0];
    }

}
