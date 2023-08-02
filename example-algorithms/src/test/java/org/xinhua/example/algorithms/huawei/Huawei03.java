package org.xinhua.example.algorithms.huawei;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Huawei03 {
    public static void main(String[] args) {
        f56();
    }

    public static void f56() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] split = scanner.nextLine().split(" ");
            int m = Integer.parseInt(split[0]);
            int n = Integer.parseInt(split[1]);
            int[][] matrix = new int[m][];
            for (int i = 0; i < m; i++) {
                matrix[i] = Arrays.stream(scanner.nextLine().split(" "))
                        .mapToInt(Integer::parseInt)
                        .toArray();
            }
            int max = 0;
            boolean[][] used = new boolean[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    int bfs = bfs(matrix, used, m, n, i, j);
                    if (bfs > max) {
                        max = bfs;
                    }
                }
            }
            System.out.println(max);
        }
    }

    private static int bfs(int[][] matrix, boolean[][] used, int m, int n, int i, int j) {
        if (used[i][j]) {
            return 0;
        }
        int count = 1;
        used[i][j] = true;
        if (i > 0 && Math.abs(matrix[i][j] - matrix[i - 1][j]) <= 1) {
            count += bfs(matrix, used, m, n, i - 1, j);
        }
        if (i < m - 1 && Math.abs(matrix[i][j] - matrix[i + 1][j]) <= 1) {
            count += bfs(matrix, used, m, n, i + 1, j);
        }
        if (j > 0 && Math.abs(matrix[i][j] - matrix[i][j - 1]) <= 1) {
            count += bfs(matrix, used, m, n, i, j - 1);
        }
        if (j < n - 1 && Math.abs(matrix[i][j] - matrix[i][j + 1]) <= 1) {
            count += bfs(matrix, used, m, n, i, j + 1);
        }
        used[i][j] = false;
        return count;
    }

    public static void f53() {
        Scanner scanner = new Scanner(System.in);
        PriorityQueue<Task> queue = new PriorityQueue<>(new Comparator<>() {
            @Override
            public int compare(Task o1, Task o2) {
                if (o1.startTime != o2.startTime) {
                    return o1.startTime - o2.startTime;
                }
                if (o2.propertity != o1.propertity) {
                    return o2.propertity - o1.propertity;
                }
                return o1.id - o2.id;
            }
        });
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line == null || line.length() == 0) {
                break;
            }
            int[] array = Arrays.stream(line.split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            queue.offer(new Task(array[0], array[1], array[2], array[3]));
        }

        int time = queue.peek().startTime;
        while (!queue.isEmpty()) {
            Task task = queue.poll();
            if (queue.isEmpty()) {
                System.out.println(task.id + " " + (time + task.costTime));
                break;
            }
            if (task.costTime == 1) {
                System.out.println(task.id + " " + (time + task.costTime));
                time++;
            } else {
                Task poll = queue.poll();
                if (time + task.costTime <= poll.startTime) {
                    System.out.println(task.id + " " + (time + task.costTime));
                    time = poll.startTime;
                } else if (task.propertity < poll.propertity) {
                    task.costTime -= poll.startTime - time;
                    task.startTime = poll.startTime + poll.costTime;
                    time = poll.startTime;
                    queue.offer(task);
                } else {//task.propertity >= poll.propertity
                    int nextStartTime = poll.startTime;
                    poll.startTime = time + task.costTime;
                    task.startTime = nextStartTime;
                    task.costTime -= nextStartTime - time;
                    time = nextStartTime;
                    queue.offer(task);
                }
                queue.offer(poll);
            }
        }
    }

    static class Task {
        int id;
        int propertity;
        int costTime;
        int startTime;

        public Task(int id, int propertity, int costTime, int startTime) {
            this.id = id;
            this.propertity = propertity;
            this.costTime = costTime;
            this.startTime = startTime;
        }
    }

    public static void f52() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] newspaper = scanner.nextLine().split(" ");
            String[] anonymous = scanner.nextLine().split(" ");

            Map<String, Integer> map = new HashMap<>();
            for (String s : newspaper) {
                char[] chars = s.toCharArray();
                Arrays.sort(chars);
                String key = new String(chars);
                map.put(key, map.getOrDefault(key, 0) + 1);
            }

            boolean flag = true;
            for (int i = 0; i < anonymous.length; i++) {
                char[] chars = anonymous[i].toCharArray();
                Arrays.sort(chars);
                String key = new String(chars);
                int count = map.getOrDefault(key, 0);
                if (count == 0) {
                    flag = false;
                    break;
                }
                map.put(key, count - 1);
            }

            System.out.println(flag);
        }
    }

    public static void f51() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String password = scanner.nextLine();
            String[] boxes = scanner.nextLine().substring(2).split(" ");

            int[] count = getCount(password.toCharArray());
            int index = -1;
            for (int i = 0; i < boxes.length; i++) {
                if (isSame(count, getCount(boxes[i].toCharArray()))) {
                    index = i;
                    break;
                }
            }
            System.out.println(index);
        }
    }

    private static int[] getCount(char[] chars) {
        int[] count = new int[26];
        for (int i = 0; i < chars.length; i++) {
            if (Character.isUpperCase(chars[i])) {
                count[chars[i] - 'A']++;
            } else if (Character.isLowerCase(chars[i])) {
                count[chars[i] - 'a']++;
            }
        }
        return count;
    }

    private static boolean isSame(int[] count1, int[] count2) {
        for (int i = 0; i < count1.length; i++) {
            if (count1[i] != count2[i]) {
                return false;
            }
        }
        return true;
    }


    public static void f50() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s1 = scanner.nextLine();
            String s2 = scanner.nextLine();
            int n = s1.length();
            int m = s2.length();
            int[][] dp = new int[n + 1][m + 1];
            int end = 0;
            int max = 0;
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= m; j++) {
                    if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                        dp[i][j] = dp[i - 1][j - 1] + 1;
                    }
                    if (dp[i][j] > max) {
                        max = dp[i][j];
                        end = i;
                    }
                }
            }

            if (max > 0) {
                System.out.println(s1.substring(end - max, end));
            } else {
                System.out.println("");
            }
        }
    }
}
