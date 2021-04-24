package org.xinua.example.algorithms.sort;

/**
 * 插入排序
 */
public class InsertSort {

    public static void sort(int arr[]) {
        sort(arr, 0, arr.length - 1);
    }

    public static void sort(int arr[], int low, int high) {
        int i, j, temp;
        for (i = low; i <= high; i++) {
            j = i;
            temp = arr[j];
            while (j > low && temp < arr[j - 1]) {
                arr[j] = arr[j - 1];
                j--;
            }
            arr[j] = temp;
        }
    }
}
