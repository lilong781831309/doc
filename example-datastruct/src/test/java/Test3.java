import java.util.Arrays;
import java.util.Scanner;

public class Test3 {
    public static void main(String[] args) {

        fn();
    }

    public static void fn() {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            int[] arr = Arrays.stream(in.nextLine().split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
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
            } else {
                int count = 0;
                while (check(matrix, n)) {
                    count++;
                    boolean[][] visit = new boolean[n][n];
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (matrix[i][j] == 1 && !visit[i][j]) {
                                visit[i][j] = true;
                                spread(matrix, visit, n, i, j);
                            }
                        }
                    }
                }
                System.out.println(count);
            }
        }
    }

    private static void spread(int[][] matrix, boolean[][] visit, int n, int i, int j) {
        if (i > 0 && !visit[i - 1][j] && matrix[i - 1][j] == 0) {
            visit[i - 1][j] = true;
            matrix[i - 1][j] = 1;
        }
        if (i < n - 1 && !visit[i + 1][j] && matrix[i + 1][j] == 0) {
            visit[i + 1][j] = true;
            matrix[i + 1][j] = 1;
        }
        if (j > 0 && !visit[i][j - 1] && matrix[i][j - 1] == 0) {
            visit[i][j - 1] = true;
            matrix[i][j - 1] = 1;
        }
        if (j < n - 1 && !visit[i][j + 1] && matrix[i][j + 1] == 0) {
            visit[i][j + 1] = true;
            matrix[i][j + 1] = 1;
        }
    }

    private static boolean check(int[][] matrix, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
