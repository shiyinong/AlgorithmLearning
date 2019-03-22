package com.syn;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName Sorts
 * @Description ���־���������㷨
 * @Date 2019/3/15 13:32
 **/
public class Sorts {

    //�����õ�����
    public static int[] data={999,0,111,5,3,7,6,8,8,7,6,5,6,7,8,109,209,87,5,32,4,5,5,3,2,5,7,7,5,3,23,45,67,654,34,567,6,543,5,67,6543,45,67,654};

    private static void swap(int[] arr,int i,int j){
        int tmp=arr[i];
        arr[i]=arr[j];
        arr[j]=tmp;
    }
    /**
     * �鲢����
     * @param arr ��������
     */
    public static void mergeSort(int[] arr){
        /**
         * �鲢����ʱ�临�Ӷȣ�n*logn���ռ临�Ӷȣ�n
         * ���Ƶ���
         * ��ͼ��֪��һ����logn�㣬ÿһ���ʱ�临�Ӷȶ���n�������ܵ�ʱ�临�ӶȾ���n*logn
         */
        mergeSortHelper(arr,0,arr.length-1);
    }

    /**
     * �鲢����helper
     * @param arr ��������
     * @param left �������������߽� ����ұ�����
     * @param right ������������ұ߽� ����ұ�����
     */
    private static void mergeSortHelper(int[] arr,int left,int right) {
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
        for (int i = left; i <= right; i++) {
            arr[i] = tmp[i - left];
        }
    }

    /**
     * ��������
     * @param arr ��������
     */
    public static void quickSort(int[] arr){
        /**
         * ��������ʱ�临�ӶȺ͹鲢һ�������ٷ���
         * �ռ临�Ӷȣ�������ʵ��û��ʹ�ö���Ŀռ䣬�������Ŀռ���ǵݹ���ã����Կռ临�Ӷ���logn��
         */
        quickSortHelper(arr,0,arr.length-1);
    }

    /**
     * ��������helper
     * @param arr ��������
     * @param left ���������������߽� ����ұ�
     * @param right ��������������ұ߽� ����ұ�
     */
    private static void quickSortHelper(int[] arr,int left,int right) {
        if (left >= right) return;
        int base = arr[left]; //��׼
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

    /**
     * ������
     * @param arr ��������
     */
    public static void heapSort(int[] arr) {
        /**
         * ����������Ҫ�˽�������һλ����洢��ȫ������
         * 1.��������ڵ�����i�����ĸ��ڵ�����Ϊ(i-1)/2��0����
         * 2.���������к��ӵĽڵ�i��������������Ϊ2*i+1���Һ�������Ϊ2*i+2
         * ʱ�临�Ӷ�Ϊn*logn���ռ临�Ӷ�Ϊ����
         */
        //��ʼ���󶥶�
        int len = arr.length;
        for (int i = len / 2 - 1; i >= 0; i--) {
            adjustHeap(arr, i, len);
        }
        //��ʼ�Ӵ󶥶���ȡԪ��
        while (--len >= 0) {
            swap(arr, 0, len); //ȡ��Ԫ��
            adjustHeap(arr, 0, len); //�޸��󶥶�
        }
    }

    /**
     * �޸��󶥶�
     * @param arr ��������
     * @param idx Ҫ�޸��Ľڵ�����
     */
    private static void adjustHeap(int[] arr,int idx,int len) {
        for (int i = idx; i < len / 2; ) {
            int left = 2 * i + 1; //���ӽڵ�����
            int right = 2 * i + 2; //�Һ��ӽڵ�����
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

    /**
     * ��������
     * @param arr ��������
     */
    public static void insertSort(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            for (int j = i - 1, idx = i; idx >= 0 && j >= 0; j--) {
                if (arr[idx] >= arr[j]) break;
                swap(arr, idx--, j);
            }
        }
    }

    /**
     * ѡ������
     * @param arr ��������
     */
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

    /**
     * ð������
     * @param arr ��������
     */
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

}

