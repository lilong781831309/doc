package org.xinhua.example.algorithms.search;

public class KMP {

    private static int indexOf(String str, String target) {
        if (str == null || target == null || str.length() < target.length()) {
            return -1;
        }
        char[] chs1 = str.toCharArray();
        char[] chs2 = target.toCharArray();
        int[] next = getNext(chs2);
        int i = 0, j = 0;
        while (i < chs1.length && j < chs2.length) {
            if (chs1[i] == chs2[j]) {
                i++;
                j++;
            } else if (next[j] == -1) {
                i++;
            } else {
                j = next[j];
            }
        }

        return j == chs2.length ? i - j : -1;
    }

    private static int[] getNext(char[] chs) {
        if (chs.length < 2) {
            return new int[]{-1};
        }
        int[] next = new int[chs.length];
        next[0] = -1;
        next[1] = 0;
        int i = 2, j = 0;
        while (i < chs.length) {
            if (chs[i - 1] == chs[j]) {
                next[i++] = ++j;
            } else if (j > 0) {
                j = next[j];
            } else {
                next[i++] = 0;
            }
        }

        return next;
    }
}
