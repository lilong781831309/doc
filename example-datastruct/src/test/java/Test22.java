import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test22 {
    public static void main(String[] args) {
        fn();
    }

    public static void fn() {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            int n = Integer.parseInt(in.nextLine());
            List<Log> logs = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                logs.add(new Log(in.nextLine()));
            }
            sortN(logs);
            sortS(logs);
            sortM(logs);
            sortH(logs);
            for (Log log : logs) {
                System.out.println(log.line);
            }
        }
    }

    private static void sortH(List<Log> logs) {
        List<Log>[] bulcks = new List[60];
        for (Log log : logs) {
            if (bulcks[log.h] == null) {
                bulcks[log.h] = new ArrayList<>();
            }
            bulcks[log.h].add(log);
        }
        logs.clear();
        for (List<Log> list : bulcks) {
            if (list != null) {
                logs.addAll(list);
            }
        }
    }

    private static void sortM(List<Log> logs) {
        List<Log>[] bulcks = new List[60];
        for (Log log : logs) {
            if (bulcks[log.m] == null) {
                bulcks[log.m] = new ArrayList<>();
            }
            bulcks[log.m].add(log);
        }
        logs.clear();
        for (List<Log> list : bulcks) {
            if (list != null) {
                logs.addAll(list);
            }
        }
    }

    private static void sortS(List<Log> logs) {
        List<Log>[] bulcks = new List[60];
        for (Log log : logs) {
            if (bulcks[log.s] == null) {
                bulcks[log.s] = new ArrayList<>();
            }
            bulcks[log.s].add(log);
        }
        logs.clear();
        for (List<Log> list : bulcks) {
            if (list != null) {
                logs.addAll(list);
            }
        }
    }

    private static void sortN(List<Log> logs) {
        List<Log>[] bulcks = new List[1000];
        for (Log log : logs) {
            if (bulcks[log.n] == null) {
                bulcks[log.n] = new ArrayList<>();
            }
            bulcks[log.n].add(log);
        }
        logs.clear();
        for (List<Log> list : bulcks) {
            if (list != null) {
                logs.addAll(list);
            }
        }
    }

    static class Log {
        int h;
        int m;
        int s;
        int n;
        String line;

        public Log(String line) {
            this.line = line;
            String[] split = line.split(":");
            h = Integer.parseInt(split[0]);
            m = Integer.parseInt(split[1]);
            split = split[2].split("\\.");
            s = Integer.parseInt(split[0]);
            n = Integer.parseInt(split[1]);
        }
    }
}
