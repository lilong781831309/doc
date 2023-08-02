package org.xinhua.example.algorithms.huawei;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Huawei05 {
    public static void main(String[] args) {
        f89();
    }

    public static void f89() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int totalCost = 0;
            String[] split = scanner.nextLine().split(" ");
            int cityNum = Integer.parseInt(split[0]);
            int optionRoadNum = Integer.parseInt(split[1]);
            int mustRoadNum = Integer.parseInt(split[2]);
            UnionFind uf = new UnionFind(cityNum);
            Map<String, Road> map = new HashMap<>();
            Set<Road> set = new HashSet<>();

            for (int i = 0; i < optionRoadNum; i++) {
                split = scanner.nextLine().split(" ");
                int city1 = Integer.parseInt(split[0]);
                int city2 = Integer.parseInt(split[1]);
                int cost = Integer.parseInt(split[2]);
                Road road = new Road(city1, city2, cost);
                String key = road.city1 + ":" + road.city2;
                map.put(key, road);
                set.add(road);
            }

            for (int i = 0; i < mustRoadNum; i++) {
                split = scanner.nextLine().split(" ");
                int city1 = Integer.parseInt(split[0]);
                int city2 = Integer.parseInt(split[1]);
                if (!uf.isSame(city1, city2)) {
                    int min = city1 < city2 ? city1 : city2;
                    int max = city1 > city2 ? city1 : city2;
                    String key = min + ":" + max;
                    Road road = map.get(key);
                    totalCost += road.cost;
                    uf.union(city1, city2);
                    set.remove(road);
                }
            }

            if (uf.count == 1) {
                System.out.println(totalCost);
            } else {
                Queue<Road> queue = new PriorityQueue<>((Road o1, Road o2) -> {
                    if (o1.cost != o2.cost) {
                        return o1.cost - o2.cost;
                    }
                    if (o1.city1 != o2.city1) {
                        return o1.city1 - o2.city1;
                    }
                    return o1.city2 - o2.city2;
                });
                queue.addAll(set);

                while (!queue.isEmpty()) {
                    Road road = queue.poll();
                    if (!uf.isSame(road.city1, road.city2)) {
                        totalCost += road.cost;
                        uf.union(road.city1, road.city2);
                        if (uf.count == 1) {
                            break;
                        }
                    }
                }

                if (uf.count == 1) {
                    System.out.println(totalCost);
                } else {
                    System.out.println(-1);
                }
            }
        }
    }

    static class Road {
        int city1;
        int city2;
        int cost;

        public Road(int city1, int city2, int cost) {
            this.city1 = city1 < city2 ? city1 : city2;
            this.city2 = city1 > city2 ? city1 : city2;
            this.cost = cost;
        }
    }

    static class UnionFind {
        int[] items;
        int count;

        public UnionFind(int n) {
            this.count = n;
            items = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                items[i] = i;
            }
        }

        public int find(int x) {
            if (x != items[x]) {
                return items[x] = find(items[x]);
            }
            return x;
        }

        public void union(int x, int y) {
            int fx = find(x);
            int fy = find(y);
            if (fx != fy) {
                items[x] = fy;
                count--;
            }
        }

        public boolean isSame(int x, int y) {
            return find(x) == find(y);
        }
    }

    public static void f86() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int n = Integer.parseInt(scanner.nextLine());
            int[] arr = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            int pmax = Integer.parseInt(scanner.nextLine());

            int pmin = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (arr[i] < pmin) {
                    pmin = arr[i];
                }
            }

            int max = 0;
            int[][] dp = new int[n + 1][pmax + 1];
            outer:
            for (int i = 1; i <= n; i++) {
                for (int j = pmin; j <= pmax; j++) {
                    if (arr[i - 1] > j) {
                        dp[i][j] = dp[i - 1][j];
                    } else {
                        int p1 = dp[i - 1][j];
                        int p2 = dp[i - 1][j - arr[i - 1]] + arr[i - 1];
                        if (p2 == pmax) {
                            max = pmax;
                            break outer;
                        } else if (p2 > p1 && p2 < pmax) {
                            dp[i][j] = p2;
                        } else {
                            dp[i][j] = p1;
                        }
                    }
                }
            }

            if (max == 0) {
                max = dp[n][pmax];
            }
            System.out.println(max);
        }
    }

    public static void f84() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int m = Integer.parseInt(scanner.nextLine());
            int[] arr = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            int max = 0;
            int sum = 0;
            int left = 0;
            int right = 0;
            int len = arr.length;
            while (right < len) {
                int temp = sum + arr[right];
                if (temp > m) {
                    sum -= arr[left++];
                } else if (temp < m) {
                    sum += arr[right++];
                    if (sum > max) {
                        max = sum;
                    }
                } else {
                    max = m;
                    break;
                }
            }
            System.out.println(max);
        }
    }

    public static void f81() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int taskNum = Integer.parseInt(scanner.nextLine());
            int relationNum = Integer.parseInt(scanner.nextLine());

            Task[] tasks = new Task[taskNum];
            Queue<Task> queue = new LinkedList<>();
            Queue<Task> tempQueue = new LinkedList<>();

            for (int i = 0; i < taskNum; i++) {
                tasks[i] = new Task(i);
                queue.offer(tasks[i]);
            }

            for (int i = 0; i < relationNum; i++) {
                String[] split = scanner.nextLine().split(" ");
                int out = Integer.parseInt(split[0]);
                int in = Integer.parseInt(split[1]);
                tasks[out].out.add(tasks[in]);
                tasks[in].in.add(tasks[out]);
            }

            int count = 0;
            while (!queue.isEmpty()) {
                count++;
                int n = queue.size();
                for (int i = 0; i < n; i++) {
                    Task poll = queue.poll();
                    if (poll.in.isEmpty()) {
                        tempQueue.offer(poll);
                    } else {
                        queue.offer(poll);
                    }
                }

                while (!tempQueue.isEmpty()) {
                    Task poll = tempQueue.poll();
                    poll.out.forEach(task -> task.in.remove(poll));
                    poll.out.clear();
                }
            }

            System.out.println(count);
        }
    }

    static class Task {
        int id;
        Set<Task> in = new HashSet<>();
        Set<Task> out = new HashSet<>();

        public Task(int id) {
            this.id = id;
        }
    }

    //TODO
    public static void f74() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] split1 = scanner.nextLine().split(" ");
            String[] split2 = scanner.nextLine().split(" ");
            String[] split3 = scanner.nextLine().split(" ");
            int m = Integer.parseInt(split1[0]);
            int n = Integer.parseInt(split1[1]);
            // 9 9 8 8 7 7 6 6 5 5 4 4 3 3 2 2 1 1 0 0
        }
    }

}
