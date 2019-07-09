package com.syn.learning.algorithm;

import java.util.*;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName PathPlanning
 * @Description
 * @Date 2019/3/18 10:35
 **/
public class PathPlanning {

    //int[] path={from,to,cost}
    public static List<int[]> data = new ArrayList<>();
    static {
        data.add(new int[]{0,1,100});
        data.add(new int[]{0,3,200});
        data.add(new int[]{1,2,150});
        data.add(new int[]{1,3,300});
        data.add(new int[]{2,4,160});
        data.add(new int[]{3,4,190});
        data.add(new int[]{3,5,390});
        data.add(new int[]{4,5,30});
    }

    public static int bellmanFord(List<int[]> edges,int from,int to,int n) {
        /**
         * 时间复杂度：m*n，空间复杂度：n。m为图的边数，n为节点的数量。
         * 贝尔曼福特算法是一种动态规划算法，代码比较简单，而且还可以设置路径最多只能k次中转。
         * 相比迪杰斯特拉，最大的优势是支持负权重
         *
         * 在不存在负环路的情况下，n条边最多循环n-1次就可以找到from点到所有点的最小成本。
         * 若存在负环路，则无法求出最小成本，但是该算法可以检测出图是否存在负环路，即循环完n-1次之后，
         * 再循环一次，若仍有节点的最小成本变小，则一定存在负环路。
         */
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.MAX_VALUE;
        }
        arr[from] = 0;
        for (int i = 0; i < n; i++) {
            int[] tmp = arr.clone();
            for (int[] edge : edges) {
                if (tmp[edge[0]] != Integer.MAX_VALUE) {
                    arr[edge[1]] = Math.min(tmp[edge[1]], tmp[edge[0]] + edge[2]);
                }
            }
        }
        return arr[to];
    }

    public static int Dijkstra(List<int[]> edges,int from,int to,int n) {
        /**
         * 时间复杂度n*logm，空间复杂度n。n是点数，m是边数
         * 不支持负权重
         */
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.MAX_VALUE;
        }
        arr[from] = 0;
        Map<Integer, List<int[]>> mp = new HashMap<>(); //key:from; value: to cost

        for (int[] edge : edges) {
            List<int[]> val = mp.getOrDefault(edge[0], new ArrayList<int[]>());
            val.add(new int[]{edge[1], edge[2]});
            mp.put(edge[0], val);
        }

        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[1] - o2[1];
            }
        });
        pq.add(new int[]{from, 0});
        Set<Integer> visited = new HashSet<>();
        while (!pq.isEmpty()) {
            int[] tmp = pq.poll(); //to cost
            if (visited.contains(tmp[0])) continue;
            visited.add(tmp[0]);
            if(!mp.containsKey(tmp[0])) continue;
            for (int[] edge : mp.get(tmp[0])) { //to cost
                arr[edge[0]] = Math.min(arr[edge[0]], arr[tmp[0]] + edge[1]);
                pq.add(new int[]{edge[0], arr[edge[0]]});
            }
        }
        return arr[to];

    }

}
