package org.xinhua.example.algorithms.huawei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        /**
         * KMP
         * DP
         * DFS
         * BFS
         * UNION_FIND
         * 贪心
         * 回溯
         * LRU
         * LRU
         * BINARY_SEARCH
         */
/*        int[] arr = {1, 2, 3, 4, 3, 5};
        System.out.println(LIS(arr));*/
        String s = "0000";
        System.out.println(restoreIpAddresses(s));
    }

    public static ArrayList<String> restoreIpAddresses(String s) {
        ArrayList<String> result = new ArrayList<>();
        int[] nums = new int[4];
        int len = s.length();
        dfs(result, nums, len, 0, 0, s);
        return result;
    }

    private static void dfs(List<String> result, int[] nums, int len,
                            int startIndex, int pointNum, String s) {
        if (startIndex == len && pointNum == 4) {
            StringBuilder sb = new StringBuilder();
            sb.append(nums[0]).append(".")
                    .append(nums[1]).append(".")
                    .append(nums[2]).append(".")
                    .append(nums[3]);
            result.add(sb.toString());
            return;
        }
        if (startIndex >= len || pointNum == 4) {
            return;
        }
        if (s.charAt(startIndex) == '0') {
            nums[pointNum] = 0;
            dfs(result, nums, len, startIndex + 1, pointNum + 1, s);
        } else {
            for (int i = 1; i <= 3 && startIndex + i <= len; i++) {
                int num = Integer.parseInt(s.substring(startIndex, startIndex + i));
                if (num <= 255) {
                    nums[pointNum] = num;
                    dfs(result, nums, len, startIndex + i, pointNum + 1, s);
                }
            }
        }
    }

    public static int LIS(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int max = 1;
        int len = arr.length;
        int[] dp = new int[len];
        Arrays.fill(dp, 1);
        for (int i = 1; i < len; i++) {
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            max = Math.max(dp[i], max);
        }
        return max;
    }

    public static int getLongestPalindrome(String A) {
        int len = A.length();
        if (len < 2) {
            return len;
        }
        char[] chars = A.toCharArray();
        int max = 1;
        for (int i = 0; i < len; i++) {
            max = Math.max(max, getLongestPalindrome(chars, i, i, len));
            max = Math.max(max, getLongestPalindrome(chars, i, i + 1, len));
        }
        return max;
    }

    private static int getLongestPalindrome(char[] chars, int left, int right, int len) {
        while (left >= 0 && right < len && chars[left] == chars[right]) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    public static int minMoney(int[] arr, int aim) {
        arr = Arrays.stream(arr).filter(i -> i <= aim).toArray();
        if (arr.length == 0) {
            return -1;
        }

        Arrays.sort(arr);

        int len = arr.length;
        int mid = len / 2;
        int temp;
        for (int i = 0; i < mid; i++) {
            temp = arr[i];
            arr[i] = arr[len - i - 1];
            arr[len - i - 1] = temp;
        }

        int min = arr[len - 1];
        if (aim < min) {
            return -1;
        }

        int[] dp = new int[aim + 1];
        Arrays.fill(dp, -1);

        for (int i = min; i <= aim; i++) {
            int less = -1;
            for (int j = 0; j < len; j++) {
                if (i - arr[j] == 0) {
                    less = 1;
                    break;
                } else if (i - arr[j] > 0 && dp[i - arr[j]] != -1) {
                    int t = dp[i - arr[j]] + 1;
                    if (less == -1 || t < less) {
                        less = t;
                    }
                }
            }
            dp[i] = less;
        }

        return dp[aim];
    }

    public static int[] LFU(int[][] operators, int k) {
        List<Integer> list = new ArrayList<>();
        Lfu lfu = new Lfu(k);
        for (int[] operator : operators) {
            if (operator[0] == 1) {
                lfu.set(operator[1], operator[2]);
            } else {
                list.add(lfu.get(operator[1]));
            }
        }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    static class Lfu {
        Map<Integer, Node> map;
        SortedMap<Integer, DoubleLinkList> freqMap;
        int k;
        int size;

        public Lfu(int k) {
            this.k = k;
            map = new HashMap<>(k);
            freqMap = new TreeMap<>();
        }

        public int get(int key) {
            Node node = map.get(key);
            if (node == null) {
                return -1;
            }
            visit(node, false);
            return node.val;
        }

        public void set(int key, int value) {
            Node node = map.get(key);
            if (node == null) {
                if (k == size) {
                    int minFreq = freqMap.firstKey();
                    DoubleLinkList list = freqMap.get(minFreq);
                    Node minFreqNode = list.removeLast();
                    if (list.isEmpty()) {
                        freqMap.remove(minFreq);
                    }
                    map.remove(minFreqNode.key);
                    size--;
                }
                node = new Node(key, value, 0);
                visit(node, true);
                map.put(key, node);
                size++;
            } else {
                node.val = value;
                visit(node, false);
            }
        }

        private void visit(Node node, boolean newNode) {
            if (!newNode) {
                DoubleLinkList list = freqMap.get(node.freq);
                list.remove(node);
                if (list.isEmpty()) {
                    freqMap.remove(node.freq);
                }
            }
            node.freq++;
            DoubleLinkList list = freqMap.get(node.freq);
            if (list == null) {
                list = new DoubleLinkList();
                freqMap.put(node.freq, list);
            }
            list.addFirst(node);
        }
    }

    static class DoubleLinkList {
        Node head;
        Node tail;

        public boolean isEmpty() {
            return head == null;
        }

        public void addFirst(Node node) {
            if (head == null) {
                head = tail = node;
            } else {
                node.next = head;
                head.pre = node;
                head = node;
            }
        }

        public Node removeFirst() {
            Node node = head;
            if (head == tail) {
                head = tail = null;
            } else {
                Node next = head.next;
                next.pre = null;
                head.next = null;
                head = next;
            }
            return node;
        }

        public Node removeLast() {
            Node node = tail;
            if (head == tail) {
                head = tail = null;
            } else {
                Node pre = tail.pre;
                pre.next = null;
                tail.pre = null;
                tail = pre;
            }
            return node;
        }

        public void remove(Node node) {
            if (node == head) {
                removeFirst();
            } else if (node == tail) {
                removeLast();
            } else {
                Node pre = node.pre;
                Node next = node.next;
                pre.next = next;
                next.pre = pre;
                node.pre = node.next = null;
            }
        }
    }

    static class Node {
        int key;
        int val;
        int freq;
        Node pre;
        Node next;

        public Node(int key, int val, int freq) {
            this.key = key;
            this.val = val;
            this.freq = freq;
        }
    }

}
