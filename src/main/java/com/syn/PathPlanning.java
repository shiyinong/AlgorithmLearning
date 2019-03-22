package com.syn;

import java.util.*;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName PathPlanning
 * @Description ·���滮�ľ����㷨
 * @Date 2019/3/18 10:35
 **/
public class PathPlanning {

    //ÿ���ߵĸ�ʽ��int[] path={from,to,cost} ��㣬�յ㣬�ɱ�
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

    /**
     * �����������㷨
     * @param edges ����·��
     * @param from  ���
     * @param to �յ�
     * @param n �ڵ������
     * @return ��С�ɱ�
     */
    public static int bellmanFord(List<int[]> edges,int from,int to,int n) {
        /**
         * ʱ�临�Ӷȣ�m*n���ռ临�Ӷȣ�n��mΪͼ�ı�����nΪ�ڵ��������
         * �����������㷨��һ�ֶ�̬�滮�㷨������Ƚϼ򵥣����һ���������·�����ֻ��k����ת��
         * ��ȵϽ�˹����������������֧�ָ�Ȩ��
         *
         * �ڲ����ڸ���·������£�n�������ѭ��n-1�ξͿ����ҵ�from�㵽���е����С�ɱ���
         * �����ڸ���·�����޷������С�ɱ������Ǹ��㷨���Լ���ͼ�Ƿ���ڸ���·����ѭ����n-1��֮��
         * ��ѭ��һ�Σ������нڵ����С�ɱ���С����һ�����ڸ���·��
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

    /**
     * �Ͻ�˹�����㷨
     * @param edges ����·��
     * @param from ���
     * @param to �յ�
     * @return ��С�ɱ�
     */
    public static int Dijkstra(List<int[]> edges,int from,int to,int n) {
        /**
         * ʱ�临�Ӷ�n*logm���ռ临�Ӷ�n��n�ǵ�����m�Ǳ���
         * ��֧�ָ�Ȩ��
         */
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.MAX_VALUE;
        }
        arr[from] = 0;
        Map<Integer, List<int[]>> mp = new HashMap<>(); //key:from; value: to cost
        //��ͼ
        for (int[] edge : edges) {
            List<int[]> val = mp.getOrDefault(edge[0], new ArrayList<int[]>());
            val.add(new int[]{edge[1], edge[2]});
            mp.put(edge[0], val);
        }
        //��ʼ�����ȼ�����
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
            if (visited.contains(tmp[0])) continue; //����ýڵ��Ѿ����ʹ���
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
