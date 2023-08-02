import java.util.Comparator;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        Comparator<Data> comparator = new Comparator<Data>() {
            @Override
            public int compare(Data data1, Data data2) {
                if (data1.priporty != data2.priporty) {
                    return data2.priporty - data1.priporty;
                }
                return data1.id - data2.id;
            }
        };

        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            PriportyQueue queue = new PriportyQueue(comparator);
            String[] split = in.nextLine().split("\\(");
            int id = 1;
            for (int i = 1; i < split.length; i++) {
                String[] pair = split[i].split("\\)")[0].split(",");
                int value = Integer.parseInt(pair[0]);
                int priporty = Integer.parseInt(pair[1]);
                queue.offer(new Data(value, priporty, id++));
            }

            StringBuilder sb = new StringBuilder("");
            if (queue.size > 0) {
                sb.append(queue.poll().value);
            }
            while (queue.size > 0) {
                sb.append(",").append(queue.poll().value);
            }
            System.out.println(sb);
        }

    }

    static class PriportyQueue {

        Data[] elements = new Data[110];
        int size = 0;
        Comparator<Data> comparator;

        public PriportyQueue(Comparator<Data> comparator) {
            this.comparator = comparator;
        }

        private boolean exist(Data data) {
            for (int i = 0; i < size; i++) {
                if (data.priporty == elements[i].priporty && data.value == elements[i].value) {
                    return true;
                }
            }
            return false;
        }

        public void offer(Data data) {
            if (exist(data)) {
                return;
            }
            elements[size++] = data;
            shiftUp(size - 1);
        }

        public Data poll() {
            Data data = elements[0];
            size--;
            elements[0] = elements[size];
            elements[size] = null;
            shiftDown(0);
            return data;
        }

        public void shiftUp(int k) {
            int p = (k - 1) >> 1;
            Data data = elements[k];
            while (p >= 0) {
                if (comparator.compare(elements[p], data) > 0) {
                    elements[k] = elements[p];
                    k = p;
                    p = (k - 1) >> 1;
                } else {
                    break;
                }
            }
            elements[k] = data;
        }

        public void shiftDown(int p) {
            int k = p * 2 + 1;
            Data data = elements[p];
            while (k < size) {
                if (k < size - 1 && comparator.compare(elements[k + 1], elements[k]) < 0) {
                    k++;
                }
                if (comparator.compare(elements[k], data) < 0) {
                    elements[p] = elements[k];
                    p = k;
                    k = p * 2 + 1;
                } else {
                    break;
                }
            }
            elements[p] = data;
        }
    }

    static class Data {
        int value;
        int priporty;
        int id;

        public Data(int value, int priporty, int id) {
            this.value = value;
            this.priporty = priporty;
            this.id = id;
        }
    }
}
