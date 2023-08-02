import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Test4 {
    public static void main(String[] args) {
        fn();
    }

    public static void fn() {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            int[] arr = Arrays.stream(in.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();
            int n = (int) Math.sqrt(arr.length);
            int count1 = 0;
            int[][] matrix = new int[n][n];
            for (int i = 0, k = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = arr[k++];
                    count1 += matrix[i][j];
                }
            }

            if (count1 == 0 || count1 == arr.length) {
                System.out.println(-1);
                continue;
            }

            int day = 0;
            Queue<int[]> queue = new LinkedList<>();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (matrix[i][j] == 1) {
                        queue.offer(new int[]{i, j});
                    }
                }
            }
            while (!queue.isEmpty()) {
                day++;
                int size = queue.size();
                while (size-- > 0) {
                    int[] poll = queue.poll();
                    int i = poll[0];
                    int j = poll[1];
                    if (i > 0 && matrix[i - 1][j] == 0) {
                        matrix[i - 1][j] = 1;
                        queue.offer(new int[]{i - 1, j});
                    }
                    if (i < n - 1 && matrix[i + 1][j] == 0) {
                        matrix[i + 1][j] = 1;
                        queue.offer(new int[]{i + 1, j});
                    }
                    if (j > 0 && matrix[i][j - 1] == 0) {
                        matrix[i][j - 1] = 1;
                        queue.offer(new int[]{i, j - 1});
                    }
                    if (j < n - 1 && matrix[i][j + 1] == 0) {
                        matrix[i][j + 1] = 1;
                        queue.offer(new int[]{i, j + 1});
                    }
                }
            }
            System.out.println(day);
        }
    }

}
