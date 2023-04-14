package org.xinhua.example.algorithms.sort;

/**
 * 冒泡排序
 */
public class BubbleSort {

    public static void sort(int[] arr) {
        int len = arr.length, i, j;
        for (i = 0; i < len - 1; i++) {
            for (j = 0; j < len - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
