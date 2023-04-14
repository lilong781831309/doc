package org.xinhua.example.algorithms.sort;

/**
 * 希尔排序
 */
public class ShellSort {

    public static void sort(int arr[]) {
        int len = arr.length, gap = len >> 1, i, j, temp;
        while (gap > 0) {
            for (i = gap; i < len; i++) {
                j = i;
                temp = arr[j];
                while (j >= gap && temp < arr[j - gap]) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = temp;
            }
            gap >>= 1;
        }
    }

}
