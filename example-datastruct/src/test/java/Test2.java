import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Test2 {
    public static void main(String[] args) {

        fn();
    }

    public static void fn() {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            int n = Integer.parseInt(in.nextLine());
            List<String> logs = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                logs.add(in.nextLine());
            }
            logs = sortN(logs);
            logs = sortS(logs);
            logs = sortM(logs);
            logs = sortH(logs);
            for (String log : logs) {
                System.out.println(log);
            }
        }
    }

    private static List<String> sortH(List<String> logs) {
        Map<Integer, List<String>> bulcks = new HashMap<>();
        for (int i = 0; i < 60; i++) {
            bulcks.put(i, new ArrayList<>());
        }
        for (String log : logs) {
            String hour = log.split(":")[0];
            bulcks.get(Integer.parseInt(hour)).add(log);
        }

        List<String> temp = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            temp.addAll(bulcks.get(i));
        }
        return temp;
    }

    private static List<String> sortM(List<String> logs) {
        Map<Integer, List<String>> bulcks = new HashMap<>();
        for (int i = 0; i < 60; i++) {
            bulcks.put(i, new ArrayList<>());
        }
        for (String log : logs) {
            String min = log.split(":")[1];
            bulcks.get(Integer.parseInt(min)).add(log);
        }

        List<String> temp = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            temp.addAll(bulcks.get(i));
        }
        return temp;
    }

    private static List<String> sortS(List<String> logs) {
        Map<Integer, List<String>> bulcks = new HashMap<>();
        for (int i = 0; i < 60; i++) {
            bulcks.put(i, new ArrayList<>());
        }
        for (String log : logs) {
            String sec = log.split(":")[2].split("\\.")[0];
            bulcks.get(Integer.parseInt(sec)).add(log);
        }

        List<String> temp = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            temp.addAll(bulcks.get(i));
        }
        return temp;
    }

    private static List<String> sortN(List<String> logs) {
        Map<Integer, List<String>> bulcks = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            bulcks.put(i, new ArrayList<>());
        }
        for (String log : logs) {
            String sec = log.split(":")[2].split("\\.")[1];
            bulcks.get(Integer.parseInt(sec)).add(log);
        }

        List<String> temp = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            temp.addAll(bulcks.get(i));
        }
        return temp;
    }

}
