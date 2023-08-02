package org.xinhua.example.algorithms.huawei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Huawei04 {
    public static void main(String[] args) {
        f71();
    }

    public static void f71() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] split1 = scanner.nextLine().split(" ");
            String[] split2 = scanner.nextLine().split(" ");
            String[] split3 = scanner.nextLine().split(" ");
            int m = Integer.parseInt(split1[0]);
            int n = Integer.parseInt(split1[1]);
            int x = Integer.parseInt(split3[0]);
            int y = Integer.parseInt(split3[1]);
            int[][] matrix = new int[m][n];
            int p = 0, q = 0;
            for (int i = 0, k = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = Integer.parseInt(split2[k++]);
                    if (matrix[i][j] > 0) {
                        p = i;
                        q = j;
                    }
                }
            }
            int[][] directs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] direct : directs) {
                dfs(matrix, directs, m, n, p + direct[0], q + direct[1], matrix[p][q]);
            }
            System.out.println(matrix[x][y]);
        }
    }

    public static void dfs(int[][] matrix, int[][] directs, int m, int n, int i, int j, int v) {
        if (i < 0 || i == m || j < 0 || j == n || matrix[i][j] == -1 || matrix[i][j] >= v) {
            return;
        }
        matrix[i][j] = v - 1;
        if (matrix[i][j] == 0) {
            return;
        }
        for (int[] direct : directs) {
            dfs(matrix, directs, m, n, i + direct[0], j + direct[1], matrix[i][j]);
        }
    }

    public static void f70() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int amount = Integer.parseInt(scanner.nextLine());
            int[] prices = Arrays.stream(scanner.nextLine().split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            Arrays.sort(prices);
            List<List<Integer>> result = new ArrayList<>();
            List<Integer> temp = new ArrayList<>();
            dfs(result, temp, prices, amount, 0);
            System.out.println(result);
        }
    }//[[100, 100, 100, 100, 100], [100, 100, 100, 200], [100, 100, 300], [100, 200, 200], [200, 300], [500]]

    public static void dfs(List<List<Integer>> result, List<Integer> temp, int[] prices, int amount, int startIndex) {
        if (amount == 0) {
            result.add(new ArrayList<>(temp));
            return;
        }
        if (amount < 0 || amount < prices[startIndex]) {
            return;
        }
        for (int i = startIndex; i < prices.length; i++) {
            if (i > 0 && prices[i] == prices[i - 1]) {
                continue;
            }
            temp.add(prices[i]);
            dfs(result, temp, prices, amount - prices[i], i);
            temp.remove(temp.size() - 1);
        }
    }

    public static void f66() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int[] nums = Arrays.stream(scanner.nextLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            int sum = sum(nums, 0);
            int targetSum = 128 * nums.length;
            int minAbs = Math.abs(targetSum - sum);
            int x = 0;
            for (int i = -128; i <= 128; i++) {
                int abs = Math.abs(targetSum - sum(nums, i));
                if (abs < minAbs) {
                    minAbs = abs;
                    x = i;
                }
            }
            System.out.println(x);
        }
    }

    private static int sum(int[] nums, int x) {
        int sum = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (nums[i] + x > 255) {
                sum += 255;
            } else if (nums[i] + x > 0) {
                sum += nums[i] + x;
            }
        }
        return sum;
    }

    public static void f59() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] split = scanner.nextLine().split(" ");
            int n = Integer.parseInt(split[0]);
            int m = Integer.parseInt(split[1]);
            int[][] matrix = new int[n][m];
            for (int i = 0; i < n; i++) {
                split = scanner.nextLine().split(" ");
                for (int j = 0; j < m; j++) {
                    matrix[i][j] = Integer.parseInt(split[j]);
                }
            }

            int count = 0;
            int[][] directs = {
                    {-1, -1}, {-1, 0}, {-1, 1},
                    {0, -1}, /*      */{0, 1},
                    {1, -1}, {1, 0}, {1, 1}};

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (matrix[i][j] == 1) {
                        count++;
                        bfs(matrix, n, m, i, j, directs);
                    }
                }
            }
            System.out.println(count);
        }
    }

    private static void bfs(int[][] matrix, int n, int m, int i, int j, int[][] directs) {
        if (i < 0 || j < 0 || i == n || j == m || matrix[i][j] == 0) {
            return;
        }
        for (int[] direct : directs) {
            bfs(matrix, n, m, i + direct[0], j + direct[1], directs);
        }
    }

    public static void f57() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] split = scanner.nextLine().split(" ");
            int t = Integer.parseInt(split[0]);
            int n = Integer.parseInt(split[1]);
            int[][] tasks = new int[n][2];
            for (int i = 0; i < n; i++) {
                split = scanner.nextLine().split(" ");
                tasks[i][0] = Integer.parseInt(split[0]);
                tasks[i][1] = Integer.parseInt(split[1]);
            }

            int min = 0;
            for (int i = 0; i < n; i++) {
                if (tasks[i][0] < min) {
                    min = tasks[i][0];
                }
            }

            int[][] dp = new int[n + 1][t + 1];
            for (int i = 1; i <= n; i++) {
                for (int j = min; j <= t; j++) {
                    int[] task = tasks[i - 1];
                    if (task[0] > j) {
                        dp[i][j] = dp[i - 1][j];
                    } else {
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - task[0]] + task[1]);
                    }
                }
            }
            System.out.println(dp[n][t]);
        }
    }

}
