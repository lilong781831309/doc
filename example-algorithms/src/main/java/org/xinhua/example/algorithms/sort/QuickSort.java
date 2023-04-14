package org.xinhua.example.algorithms.sort;

import java.util.Stack;

/**
 * 快速排序
 */
public class QuickSort {

    public static void sort(int[] arr) {
        recursion(arr, 0, arr.length - 1);
    }

    //递归
    private static void recursion(int[] arr, int low, int high) {
        if (low < high) {
            //轴
            int pivot = partition(arr, low, high);
            recursion(arr, low, pivot - 1);
            recursion(arr, pivot + 1, high);
        }
    }

    //非递归
    private static void nonRecursion(int[] arr) {
        Stack<Integer> stack = new Stack<>();
        stack.push(arr.length - 1);
        stack.push(0);

        int low, high;

        while (!stack.empty()) {
            low = stack.pop();
            high = stack.pop();

            if (low < high) {
                //轴
                int pivot = partition(arr, low, high);

                stack.push(high);
                stack.push(pivot + 1);

                stack.push(pivot - 1);
                stack.push(low);
            }
        }
    }

    //分区
    private static int partition(int[] arr, int low, int high) {
        if (low < high) {
            int i = low, j = high, temp = arr[i];
            while (i < j) {
                while (i < j && arr[j] >= temp) {
                    j--;
                }
                arr[i] = arr[j];
                while (i < j && arr[i] <= temp) {
                    i++;
                }
                arr[j] = arr[i];
            }
            arr[i] = temp;
            return i;
        }
        return low;
    }
}
