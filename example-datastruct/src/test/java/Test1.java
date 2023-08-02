import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Test1 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            Map<Integer, List<Integer>> map = new TreeMap<>((Integer i1, Integer i2) -> i2 - i1);
            Map<Integer, Set<Integer>> map2 = new HashMap<>();
            String[] split = in.nextLine().split("\\(");
            for (int i = 1; i < split.length; i++) {
                String[] pair = split[i].split("\\)")[0].split(",");
                int value = Integer.parseInt(pair[0]);
                int priporty = Integer.parseInt(pair[1]);
                Set<Integer> set = map2.get(priporty);
                if (set == null) {
                    set = new HashSet<>();
                    map2.put(priporty, set);
                }
                if (!set.contains(value)) {
                    set.add(value);
                    List<Integer> list = map.get(priporty);
                    if (list == null) {
                        list = new ArrayList<>();
                        map.put(priporty, list);
                    }
                    list.add(value);
                }
            }

            List<Integer> list = new ArrayList<>();
            map.forEach((k, v) -> list.addAll(v));

            StringBuilder sb = new StringBuilder("");
            int size = list.size();
            if (size > 0) {
                sb.append(list.get(0));
                for (int i = 1; i < size; i++) {
                    sb.append(",").append(list.get(i));
                }
            }
            System.out.println(sb);
        }
    }
}
