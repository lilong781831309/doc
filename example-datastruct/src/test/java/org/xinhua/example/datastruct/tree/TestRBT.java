package org.xinhua.example.datastruct.tree;

import org.xinhua.example.datastruct.tree.printer.BinaryTreeInfo;
import org.xinhua.example.datastruct.tree.printer.BinaryTrees;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

/**
 * @Author: lilong
 * @createDate: 2023/4/16 6:32
 * @Description: 测试RBT
 * @Version: 1.0
 */
public class TestRBT {

    static final String RN = "\n\n\n\n";
    static Random random = new Random();

    public static void main(String[] args) {
        Tree bst = new Tree();

        List<Integer> list = Util.getShuffleIntegers(500, 50);
        System.out.println(list);
        System.out.println(RN);

        for (int i = 0; i < list.size(); i++) {
            int add = list.get(i);
            bst.add(add);
            System.out.println("========add====" + add + "========size====" + bst.size());
            BinaryTrees.println(bst);
            System.out.println(RN);
        }

        while (list.size() > 0) {
            int index = random.nextInt(list.size());
            if (index == list.size()) index--;
            Integer remove = list.remove(index);
            System.out.println("========remove====" + remove + "========size====" + (bst.size() - 1));
            bst.remove(remove);
            BinaryTrees.println(bst);
            System.out.println(RN);
        }
    }

    static class Tree extends RBT implements BinaryTreeInfo {
        Field left = Util.getField(RBTNode.class, "left");
        Field right = Util.getField(RBTNode.class, "right");
        Field color = Util.getField(RBTNode.class, "color");
        Field e = Util.getField(RBTNode.class, "e");

        @Override
        public Object root() {
            return root;
        }

        @Override
        public Object left(Object node) {
            try {
                return left.get(node);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Object right(Object node) {
            try {
                return right.get(node);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Object string(Object node) {
            if (node == null) return null;
            try {
                return e.get(node).toString() + "(" + ((boolean) (color.get(node)) ? "B" : "R") + ")";
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

