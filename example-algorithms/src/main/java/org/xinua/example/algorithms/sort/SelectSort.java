package org.xinua.example.algorithms.sort;

/**
 * 选择排序
 */
public class SelectSort {

    public static void sort(int[] arr) {
        int len = arr.length, left, right, min, max, i;
        for (left = 0, right = len - 1; left < right; left++, right--) {
            min = left;
            max = right;
            for (i = left; i <= right; i++) {
                if (arr[i] < arr[min]) {
                    min = i;
                }
                if (arr[i] > arr[max]) {
                    max = i;
                }
            }
            if (min != left) {
                swap(arr, min, left);
            }
            if (max != right) {
                swap(arr, left == max ? min : max, right);
            }
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
