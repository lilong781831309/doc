package org.xinhua.example.algorithms.search;

/**
 * @Author: lilong
 * @createDate: 2023/4/26 2:05
 * @Description: 二分搜索
 * @Version: 1.0
 */
public class BinarySearch {

    /**
     * 查找有序集合中关键字的位置,找到返回索引,没找到返回-1
     */
    public static int binarySearch(int[] arr, int left, int right, int key) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        int low = left < 0 ? 0 : left;
        int high = right < arr.length ? right : arr.length - 1;
        while (low <= high) {
            int mid = (low + high) >> 1;
            if (key < arr[mid]) {
                high = mid - 1;
            } else if (key > arr[mid]) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 查找有序集合中关键字最开始出现的位置,找到返回索引,没找到返回-1
     */
    public static int indexOf(int[] arr, int left, int right, int key) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        int low = left = left < 0 ? 0 : left;
        int high = right < arr.length ? right : arr.length - 1;
        while (low <= high) {
            int mid = (low + high) >> 1;
            if (key < arr[mid]) {
                high = mid - 1;
            } else if (key > arr[mid]) {
                low = mid + 1;
            } else if (mid > left && arr[mid - 1] == key) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 查找有序集合中关键字最后出现的位置,找到返回索引,没找到返回-1
     */
    public static int lastIndexOf(int[] arr, int left, int right, int key) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        int low = left < 0 ? 0 : left;
        int high = right = right < arr.length ? right : arr.length - 1;
        while (low <= high) {
            int mid = (low + high) >> 1;
            if (key < arr[mid]) {
                high = mid - 1;
            } else if (key > arr[mid]) {
                low = mid + 1;
            } else if (mid < right && arr[mid + 1] == key) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 查找关键字插入有序集合中的位置
     */
    public static int insertPosition(int[] arr, int left, int right, int key) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        int low = left < 0 ? 0 : left;
        int high = right < arr.length ? right + 1 : arr.length;
        if (key < arr[low]) {
            return low;
        }
        if (key > arr[high - 1]) {
            return high;
        }
        while (low < high) {
            int mid = (low + high) >> 1;
            if (key < arr[mid]) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return high;
    }

}
