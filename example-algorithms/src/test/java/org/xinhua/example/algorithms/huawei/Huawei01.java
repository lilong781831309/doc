package org.xinhua.example.algorithms.huawei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Huawei01 {

    public static void main(String[] args) {
        f10();
    }

    public static void f10() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] strs = scanner.nextLine().split(" ");

            Set<String> set = new HashSet<>();
            for (String str : strs) {
                set.add(str);
            }

            String password = "";
            for (String str : strs) {
                if ((str.length() == password.length() && str.compareTo(password) > 0) ||
                        str.length() > password.length()) {
                    int len = str.length();
                    boolean flag = true;
                    for (int i = 1; i < len; i++) {
                        if (!set.contains(str.substring(0, i))) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        password = str;
                    }
                }
            }

            System.out.println(password);
        }
    }

    public static void f05() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {
            int m = scanner.nextInt();
            int[] nums = new int[m];
            int sum = 0;

            for (int i = 0; i < m; i++) {
                nums[i] = scanner.nextInt();
                sum += nums[i];
            }

            Arrays.sort(nums);

            int targetSum = 0;
            boolean[] used = new boolean[nums.length];

            for (int i = m; i > 0; i--) {
                if (sum % i == 0) {
                    targetSum = sum / i;
                    if (dfs(nums, used, targetSum, 0, 0)) {
                        break;
                    }
                }
            }

            System.out.println(targetSum);
        }
    }

    private static boolean dfs(int[] nums, boolean[] used, int targetSum, int curSum, int n) {
        if (curSum > targetSum) {
            return false;
        }
        if (nums.length == n) {
            return targetSum == curSum;
        }
        if (targetSum == curSum) {
            return dfs(nums, used, targetSum, 0, n);
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }
            if (nums[i] > targetSum) {
                return false;
            }
            used[i] = true;
            if (dfs(nums, used, targetSum, curSum + nums[i], n + 1)) {
                return true;
            }
            used[i] = false;
        }
        return false;
    }

    public static void f04() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {
            int m = scanner.nextInt();
            int n = scanner.nextInt();
            int[][] arr = new int[n][2];
            for (int i = 0; i < n; i++) {
                arr[i][0] = scanner.nextInt();
                arr[i][1] = scanner.nextInt();
            }

            int count = 0;
            for (int i = 0; i < (1 << m); i++) {
                boolean flag = true;
                for (int j = 0; j < n; j++) {
                    if (((i >> (m - arr[j][0])) & 1) == 1 && ((i >> (m - arr[j][1])) & 1) == 1) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    count++;
                }
            }
            System.out.println(count);
        }
    }

    public static void f03() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {
            int m = scanner.nextInt();
            int n = scanner.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }

            Arrays.sort(arr);

            int count = 0;
            int left = 0;
            int right = n - 1;
            while (left <= right) {
                if (arr[left] + arr[right] <= m) {
                    left++;
                }
                right--;
                count++;
            }

            System.out.println(count);
        }
    }

    public static void f02() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] strs = line.split(" ");

            Map<String, Integer> map = new TreeMap<>();
            for (String str : strs) {
                char[] chars = str.toCharArray();
                Arrays.sort(chars);
                map.compute(new String(chars), (k, v) -> {
                    return v == null ? 1 : v + 1;
                });
            }

            List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
            list.sort(new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    if (o1.getValue() < o2.getValue()) {
                        return 1;
                    } else if (o1.getValue() > o2.getValue()) {
                        return -1;
                    } else if (o1.getKey().length() < o2.getKey().length()) {
                        return -1;
                    } else if (o1.getKey().length() > o2.getKey().length()) {
                        return 1;
                    } else {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                }
            });

            StringBuilder sb = new StringBuilder();
            list.forEach(e -> {
                String word = e.getKey();
                int n = e.getValue();
                while (n-- > 0) {
                    sb.append(word).append(" ");
                }
            });
            System.out.println(sb);
        }
    }

    public static void f01() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {
            Set<Integer> set = new HashSet<>();
            int n = scanner.nextInt();
            int[][] arr = new int[n][3];

            for (int i = 0; i < n; i++) {
                arr[i][0] = scanner.nextInt();
                arr[i][1] = scanner.nextInt();
                arr[i][2] = scanner.nextInt();
                set.add(arr[i][0]);
                set.add(arr[i][1]);
            }

            int max = 0;
            for (Integer point : set) {
                int sum = 0;
                for (int i = 0; i < n; i++) {
                    if (arr[i][0] <= point && point <= arr[i][1]) {
                        sum += arr[i][2];
                    }
                }
                if (sum > max) {
                    max = sum;
                }
            }

            System.out.println(max);
        }
    }

}
