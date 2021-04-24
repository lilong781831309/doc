package org.xinua.example.algorithms.sort;

/**
 * 堆排序
 */
public class HeapSort {

    public static void sort(int[] arr) {
        int last = arr.length - 1;
        int p = (last - 1) >> 1;
        while (p >= 0) {
            shiftDown(arr, p--, last);
        }
        while (last > 0) {
            swap(arr, 0, last);
            shiftDown(arr, 0, --last);
        }
    }

    public static void shiftDown(int[] arr, int p, int last) {
        int k = (p << 1) + 1, temp = arr[p];
        while (k <= last) {
            if (k < last && arr[k] < arr[k + 1]) {
                k++;
            }
            if (arr[k] > temp) {
                arr[p] = arr[k];
            } else {
                break;
            }
            p = k;
            k = (p << 1) + 1;
        }
        arr[p] = temp;
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
