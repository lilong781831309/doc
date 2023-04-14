package org.xinhua.example.algorithms.sort;

/**
 * 归并排序
 */
public class MergeSort {

    public static void sort(int arr[]) {
        recursion(arr, 0, arr.length - 1, new int[arr.length]);
    }

    //递归
    private static void recursion(int arr[], int low, int high, int[] temp) {
        if (low < high) {
            int mid = (low + high) >> 1;
            recursion(arr, low, mid, temp);
            recursion(arr, mid + 1, high, temp);
            merge(arr, low, mid, high, temp);
        }
    }

    //非递归
    private static void nonRecursion(int arr[]) {
        int len = arr.length, low, k = 1;
        int[] temp = new int[len];
        while (k < len) {
            low = 0;
            while (low + k * 2 - 1 < len) {
                merge(arr, low, low + k - 1, low + k * 2 - 1, temp);
                low += k * 2;
            }
            if (low + k < len) {
                merge(arr, low, low + k - 1, len - 1, temp);
            }
            k <<= 1;
        }
    }

    private static void merge(int arr[], int low, int mid, int high, int[] temp) {
        if (low < high) {
            int i = low, j = mid + 1, k = 0;
            while (i <= mid && j <= high) {
                if (arr[i] < arr[j]) {
                    temp[k++] = arr[i++];
                } else {
                    temp[k++] = arr[j++];
                }
            }
            while (i <= mid) {
                temp[k++] = arr[i++];
            }
            while (j <= high) {
                temp[k++] = arr[j++];
            }
            for (int t = 0; t < k; t++) {
                arr[t + low] = temp[t];
            }
        }
    }
}
