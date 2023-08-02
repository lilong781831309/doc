package org.xinhua.example.algorithms.huawei;

import java.util.Scanner;

public class Huawei06 {
    public static void main(String[] args) {
        f91();
    }

    public static void f91() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            char[] chars = scanner.nextLine().toCharArray();
            int len = chars.length;
            int result = 0;
            for (int i = 0; i < len; i++) {
                if (chars[i] != 'M') {
                    continue;
                }
                if (i < len - 1 && chars[i + 1] == 'I') {
                    result++;
                    i += 2;
                } else if (i > 0 && chars[i - 1] == 'I') {
                    result++;
                } else {
                    result = -1;
                    break;
                }
            }
            System.out.println(result);
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
