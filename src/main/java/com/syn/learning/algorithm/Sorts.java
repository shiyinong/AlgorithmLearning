package com.syn.learning.algorithm;

import java.util.List;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName Sorts
 * @Description
 * @Date 2019/3/15 13:32
 **/
public class Sorts {

    public static int[] data = {999, 0, 111, 5, 3, 7, 6, 8, 8, 7, 6, 5, 6, 7, 8, 109, 209, 87, 5, 32, 4, 5, 5, 3, 2, 5, 7, 7, 5, 3, 23, 45, 67, 654, 34, 567, 6, 543, 5, 67, 6543, 45, 67, 654};

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static <E> void swap(List<E> list, int i, int j) {
        E tmp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, tmp);
    }

    public static void mergeSort(int[] arr) {

        mergeSortHelper(arr, 0, arr.length - 1);
    }


    private static void mergeSortHelper(int[] arr, int left, int right) {
        /**
         * 归并排序时间复杂度：n*logn；空间复杂度：n
         * 简单推导：
         * 画图可知，一共有logn层，每一层的时间复杂度都是n，所以总的时间复杂度就是n*logn
         */
        if (left >= right) return;
        int mid = (left + right) / 2;
        mergeSortHelper(arr, left, mid);
        mergeSortHelper(arr, mid + 1, right);
        int[] tmp = new int[right - left + 1];
        for (int i = left, j = mid + 1, idx = 0; idx <= (right - left); idx++) {
            if (i > mid) {
                tmp[idx] = arr[j++];
            } else if (j > right) {
                tmp[idx] = arr[i++];
            } else if (arr[i] < arr[j]) {
                tmp[idx] = arr[i++];
            } else {
                tmp[idx] = arr[j++];
            }
        }
//        for (int i = left; i <= right; i++) {
//            arr[i] = tmp[i - left];
//        }
        System.arraycopy(tmp, 0, arr, left, right - left + 1);
    }

    public static void quickSort(int[] arr) {
        /**
         * 快速排序时间复杂度和归并一样，不再分析
         * 空间复杂度，快排其实并没有使用额外的空间，真正消耗空间的是递归调用，所以空间复杂度是logn的
         */
        quickSortHelper(arr, 0, arr.length - 1);
    }

    private static void quickSortHelper(int[] arr, int left, int right) {
        if (left >= right) return;
        int base = arr[left];
        int i = left, j = right;
        while (i < j) {
            while (j > i && arr[j] > base) {
                j--;
            }
            if (j > i) arr[i++] = arr[j];
            while (i < j && arr[i] < base) {
                i++;
            }
            if (i < j) arr[j--] = arr[i];
        }
        arr[i] = base;
        quickSortHelper(arr, left, i - 1);
        quickSortHelper(arr, i + 1, right);
    }

    public static void heapSort(int[] arr) {
        /**
         * 堆排序，首先要了解怎样用一位数组存储完全二叉树
         * 1.对于任意节点索引i，它的父节点索引为(i-1)/2，0除外
         * 2.对于任意有孩子的节点i，它的左孩子索引为2*i+1，右孩子索引为2*i+2
         * 时间复杂度为n*logn，空间复杂度为常数
         */
        int len = arr.length;
        for (int i = len / 2 - 1; i >= 0; i--) {
            adjustHeap(arr, i, len);
        }
        while (--len >= 0) {
            swap(arr, 0, len);
            adjustHeap(arr, 0, len);
        }
    }

    private static void adjustHeap(int[] arr, int idx, int len) {
        for (int i = idx; i < len / 2; ) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (arr[i] >= arr[left] && (right >= len || arr[i] >= arr[right])) return;
            if (right < len && arr[left] < arr[right]) {
                swap(arr, i, right);
                i = right;
            } else {
                swap(arr, i, left);
                i = left;
            }
        }
    }


    public static void insertSort(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            for (int j = i - 1, idx = i; idx >= 0 && j >= 0; j--) {
                if (arr[idx] >= arr[j]) break;
                swap(arr, idx--, j);
            }
        }
    }

    public static void selectSort(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            int minIdx = i;
            for (int j = i; j < len; j++) {
                minIdx = arr[minIdx] > arr[j] ? j : minIdx;
            }
            swap(arr, i, minIdx);
        }
    }

    public static void popSort(int[] arr) {
        int len = arr.length;
        for (int i = len - 1; i >= 0; i--) {
            for (int j = 1; j <= i; j++) {
                if (arr[j - 1] > arr[j]) {
                    swap(arr, j - 1, j);
                }
            }
        }
    }

    public static void insertEleToHead(List<Integer> heap, int ele) {
        heap.add(ele);
        int len = heap.size();
        for (int i = len - 1; i >= 0; ) {
            int par = (i - 1) / 2;
            if (par < 0 || heap.get(par) >= heap.get(i)) break;
            swap(heap, par, i);
            i = par;
        }
    }

    public static void deleteEleToHeap(List<Integer> heap, int idx) {
        int len = heap.size() - 1;
        swap(heap, idx, len);
        heap.remove(len);
        for (int i = idx; i <= (len - 2) / 2; ) {
            int left = 2 * i + 1;
            int right = left + 1;
            if (heap.get(i) >= heap.get(left) && (right >= len || heap.get(i) >= heap.get(right))) break;
            if (right >= len || heap.get(left) >= heap.get(right)) {
                swap(heap, i, left);
                i = left;
            } else {
                swap(heap, i, right);
                i = right;
            }
        }
    }
}

