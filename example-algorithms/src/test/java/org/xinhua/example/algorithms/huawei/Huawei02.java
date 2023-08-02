package org.xinhua.example.algorithms.huawei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Huawei02 {
    public static void main(String[] args) {
        f47();
    }

    public static void f47() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            List<String> list1 = Arrays.stream(scanner.nextLine().split(",")).collect(Collectors.toList());
            List<String> list2 = Arrays.stream(scanner.nextLine().split(",")).collect(Collectors.toList());
            Map<String, String> map1 = new HashMap<>();
            Map<String, String> map2 = new HashMap<>();
            Set<Character> set = new HashSet<>();

            list2.forEach(s -> {
                set.clear();
                char[] chars = s.toCharArray();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < chars.length; i++) {
                    if (!set.contains(chars[i])) {
                        set.add(chars[i]);
                        sb.append(chars[i]);
                    }
                }
                Arrays.sort(chars);
                map1.put(new String(chars), s);
                map2.put(sb.toString(), s);
            });

            StringBuilder res = new StringBuilder();
            boolean first = true;
            for (String s : list1) {
                boolean find = false;
                String str = "not found";

                if (map1.containsKey(s)) {
                    str = map1.get(s);
                    find = true;
                }

                if (!find && map2.containsKey(s)) {
                    str = map2.get(s);
                    find = true;
                }

                if (!find) {
                    set.clear();
                    char[] chars = s.toCharArray();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < chars.length; i++) {
                        if (!set.contains(chars[i])) {
                            set.add(chars[i]);
                            sb.append(chars[i]);
                        }
                    }

                    Arrays.sort(chars);

                    String key = new String(chars);
                    find = map1.containsKey(key);
                    if (find) {
                        str = map1.get(key);
                    }

                    if (!find) {
                        key = sb.toString();
                        if (map2.containsKey(key)) {
                            str = map2.get(key);
                        }
                    }
                }

                if (first) {
                    first = false;
                } else {
                    res.append(",");
                }
                res.append(str);
            }
            System.out.println(res);
        }
    }

    public static void f43() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int m = Integer.parseInt(scanner.nextLine());
            int[] nums = Arrays.stream(scanner.nextLine().split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            int min = 0;
            for (int i = 1; i < m; i++) {
                if (nums[i] <= nums[min]) {
                    min = i;
                }
            }

            List<Integer> list = new ArrayList<>();
            Deque<Integer> queue = new LinkedList<>();
            list.add(nums[min]);
            queue.offer(min);

            for (int i = m; i < nums.length; i++) {
                if (nums[queue.peekLast()] >= nums[i]) {
                    queue.clear();
                    queue.offerLast(i);
                } else {
                    while (!queue.isEmpty() && nums[queue.peekFirst()] >= nums[i]) {
                        queue.pollFirst();
                    }
                    while (!queue.isEmpty() && queue.peekLast() < i - m + 1) {
                        queue.pollLast();
                    }
                    queue.offerFirst(i);
                }
                list.add(nums[queue.peekLast()]);
            }

            System.out.println(list);
        }
    }

    public static void f41() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int n = Integer.parseInt(scanner.nextLine());
            int[] nums = new int[n];
            String[] numStr = scanner.nextLine().split(" ");

            for (int i = 0; i < n; i++) {
                nums[i] = Integer.parseInt(numStr[i]);
            }

            int[] dp = new int[n];
            dp[0] = nums[0];
            for (int i = 1; i < n; i++) {
                dp[i] = nums[i] + dp[i - 1];
            }

            Map<Integer, List<int[]>> map = new HashMap<>();
            for (int i = 0; i < n; i++) {
                for (int j = i; j < n; j++) {
                    int sum = dp[j];
                    if (i > 0) {
                        sum -= dp[i - 1];
                    }
                    List<int[]> list = map.get(sum);
                    if (list == null) {
                        list = new ArrayList<>();
                        map.put(sum, list);
                    }
                    list.add(new int[]{i, j});
                }
            }

            int max = 0;
            for (List<int[]> list : map.values()) {
                list.sort((arr1, arr2) -> {
                    int result = arr1[0] - arr2[0];
                    if (result == 0) {
                        return arr1[1] - arr2[1];
                    }
                    return result;
                });

                int count = 1;
                int cur = list.get(0)[1];
                int size = list.size();
                for (int i = 1; i < size; i++) {
                    if (cur < list.get(i)[0]) {
                        cur = list.get(i)[1];
                        count++;
                    }
                }

                if (count > max) {
                    max = count;
                }
            }

            System.out.println(max);
        }
    }

    public static void f39() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] mn = scanner.nextLine().split(" ");
            int m = Integer.parseInt(mn[0]);
            int n = Integer.parseInt(mn[1]);

            char[][] matrix = new char[m][n];
            for (int i = 0; i < m; i++) {
                String s = scanner.nextLine();
                for (int j = 0, k = 0; j < n; j++, k += 2) {
                    matrix[i][j] = s.charAt(k);
                }
            }

            List<int[]> result = new ArrayList<>();

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isSingleEntryPoint(matrix, m, n, i, j)) {
                        matrix[i][j] = 'X';
                        int count = dfs(matrix, m, n, i, j);
                        if (count != -1) {
                            if (result.isEmpty() || count == result.get(0)[2]) {
                                result.add(new int[]{i, j, count});
                            } else if (count > result.get(0)[2]) {
                                result.clear();
                                result.add(new int[]{i, j, count});
                            }
                        }
                        matrix[i][j] = 'O';
                    }
                }
            }

            if (result.isEmpty()) {

            } else if (result.size() == 1) {

            } else {

            }
        }
    }

    private static int dfs(char[][] matrix, int m, int n, int i, int j) {
        if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
            if (matrix[i][j] == 'O') return -1;
        }
        if (matrix[i][j] == 'X') return 0;
        matrix[i][j] = 'X';
        int up = -1, down = -1, left = -1, right = -1;
        up = dfs(matrix, m, n, i - 1, j);
        if (up != -1) {
            down = dfs(matrix, m, n, i + 1, j);
        }
        if (down != -1) {
            left = dfs(matrix, m, n, i, j - 1);
        }
        if (left != -1) {
            right = dfs(matrix, m, n, i, j + 1);
        }
        matrix[i][j] = 'O';
        if (up == -1 || down == -1 || left == -1 || right == -1) {
            return -1;
        }
        return 1 + up + down + left + right;
    }

    private static boolean isSingleEntryPoint(char[][] matrix, int m, int n, int i, int j) {
        if (matrix[i][j] == 'X') {
            return false;
        }
        if ((i == 0 || i == m - 1) && (j == 0 || j == n - 1)) {
            return false;
        }
        if (i == 0) {
            return matrix[i][j + 1] != 'O' && matrix[i][j - 1] != 'O' && matrix[i + 1][j] != 'O';
        } else if (i == (m - 1)) {
            return matrix[i][j + 1] != 'O' && matrix[i - 1][j] != 'O' && matrix[i][j + 1] != 'O';
        } else if (j == 0) {
            return matrix[i - 1][j] != 'O' && matrix[i + 1][j] != 'O' && matrix[i][j + 1] != 'O';
        } else if (j == (n - 1)) {
            return matrix[i - 1][j] != 'O' && matrix[i + 1][j] != 'O' && matrix[i][j - 1] != 'O';
        }
        return false;
    }
}
