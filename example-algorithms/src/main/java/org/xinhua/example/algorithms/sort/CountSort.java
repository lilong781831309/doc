package org.xinhua.example.algorithms.sort;

/**
 * 计数排序
 */
public class CountSort {

    public static void sort(int[] arr) {
        int len = arr.length, min = arr[0], max = arr[0];
        int i;
        for (i = 1; i < len; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
            if (arr[i] > max) {
                max = arr[i];
            }
        }

        int[] count = new int[max - min + 1];
        for (i = 0; i < len; i++) {
            count[arr[i] - min]++;
        }

        for (i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        int[] temp = new int[len];
        for (i = len - 1; i >= 0; i--) {
            temp[--count[arr[i] - min]] = arr[i];
        }

        for (i = 0; i < len; i++) {
            arr[i] = temp[i];
        }
    }
}
