package org.xinhua.example.design_pattern.creational.factory_method.demo2;

/**
 * @Author: lilong
 * @createDate: 2023/4/7 17:59
 * @Description: 测试
 * @Version: 1.0
 */
public class Client {

    public static void main(String[] args) {
        Collection<Parent> collection = new ArrayList<>();
        collection.add(new Parent("1"));
        collection.add(new Parent("2"));
        collection.add(new Parent("3"));

        LinkedList<Sub> list = new LinkedList();
        list.add(new Sub("4"));
        list.add(new Sub("5"));
        list.add(new Sub("6"));

        collection.addAll(list);

        collection.forEach(x -> System.out.println(x.getName()));
    }

    public static void test1() {
        Collection<Integer> collection = new ArrayList<>();

        collection.add(1);
        collection.add(2);
        collection.add(3);
        collection.add(4);

        for (int i = 0; i < collection.size(); i++) {
            System.out.println(collection.get(i));
        }

        System.out.println("=====");

        collection.forEach(System.out::println);

        System.out.println("=====");
        Iterator<Integer> it = collection.iterator();

        while (it.hasNext()) {
            Integer next = it.next();
            if (next.equals(2)) {
                System.out.println(next + " :remove");
                it.remove();
            } else {
                System.out.println(next);
            }
        }

        System.out.println("=====");
        it = collection.iterator();

        while (it.hasNext()) {
            Integer next = it.next();
            System.out.println(next);
        }
    }

    public static void test2() {
        Collection<Integer> collection = new LinkedList();

        collection.add(1);
        collection.add(2);
        collection.add(3);
        collection.add(4);

        for (int i = 0; i < collection.size(); i++) {
            System.out.println(collection.get(i));
        }

        System.out.println("=====");

        collection.forEach(System.out::println);

        System.out.println("=====");
        Iterator<Integer> it = collection.iterator();

        while (it.hasNext()) {
            Integer next = it.next();
            if (next.equals(3)) {
                System.out.println(next + " :remove");
                it.remove();
            } else {
                System.out.println(next);
            }
        }

        System.out.println("=====");
        it = collection.iterator();

        while (it.hasNext()) {
            Integer next = it.next();
            System.out.println(next);
        }
    }

}
