package org.xinhua.example.datastruct.tree;

import java.util.function.Consumer;

public class Tree<V> {

    public static void main(String[] args) {
        Tree<Integer> tree = new Tree<>();
        TreeNode root = new TreeNode(4);
        tree.root = root;
        root.left = new TreeNode(2);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(5);
        root.right.right = new TreeNode(7);
        //     4
        //  2    6
        // 1 3  5  7
        tree.mirrorsPreOrder(v -> System.out.print(v + ","));
        System.out.println();
        tree.mirrorsInOrder(v -> System.out.print(v + ","));
        System.out.println();
        tree.mirrorsPostOrder(v -> System.out.print(v + ","));
    }

    private TreeNode<V> root;

    public void mirrors() {
        TreeNode<V> cur = root;
        TreeNode<V> rightMost = null;

        while (cur != null) {
            if (cur.left != null) {
                rightMost = cur.left;
                while (rightMost.right != null && rightMost.right != cur) {
                    rightMost = rightMost.right;
                }
                if (rightMost.right == null) {
                    rightMost.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    rightMost.right = null;
                }
            }
            cur = cur.right;
        }
    }

    public void mirrorsPreOrder(Consumer<? super V> action) {
        TreeNode<V> cur = root;
        TreeNode<V> rightMost = null;

        while (cur != null) {
            if (cur.left != null) {
                rightMost = cur.left;
                while (rightMost.right != null && rightMost.right != cur) {
                    rightMost = rightMost.right;
                }
                if (rightMost.right == null) {
                    action.accept(cur.v);
                    rightMost.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    rightMost.right = null;
                }
            } else {
                action.accept(cur.v);
            }
            cur = cur.right;
        }
    }

    public void mirrorsInOrder(Consumer<? super V> action) {
        TreeNode<V> cur = root;
        TreeNode<V> rightMost = null;

        while (cur != null) {
            if (cur.left != null) {
                rightMost = cur.left;
                while (rightMost.right != null && rightMost.right != cur) {
                    rightMost = rightMost.right;
                }
                if (rightMost.right == null) {
                    rightMost.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    rightMost.right = null;

                }
            }
            action.accept(cur.v);
            cur = cur.right;
        }
    }

    public void mirrorsPostOrder(Consumer<? super V> action) {
        TreeNode<V> cur = root;
        TreeNode<V> rightMost = null;

        while (cur != null) {
            if (cur.left != null) {
                rightMost = cur.left;
                while (rightMost.right != null && rightMost.right != cur) {
                    rightMost = rightMost.right;
                }
                if (rightMost.right == null) {
                    rightMost.right = cur;
                    cur = cur.left;
                } else {
                    rightMost.right = null;
                    reverseTraversal(cur.left, action);
                    cur = cur.right;
                }
            } else {
                cur = cur.right;
            }
        }
        if (root != null) {
            reverseTraversal(root, action);
        }
    }

    private void reverseTraversal(TreeNode<V> node, Consumer<? super V> action) {
        TreeNode<V> tail = reverseRight(node);
        TreeNode<V> head = tail;
        while (head != null) {
            action.accept(head.v);
            head = head.right;
        }
        reverseRight(tail);
    }

    private TreeNode<V> reverseRight(TreeNode<V> head) {
        TreeNode<V> newHead = null;
        TreeNode<V> next = null;
        while (head != null) {
            next = head.right;
            head.right = newHead;
            newHead = head;
            head = next;
        }
        return newHead;
    }

    static class TreeNode<V> {
        V v;
        TreeNode<V> left;
        TreeNode<V> right;

        public TreeNode(V v) {
            this.v = v;
        }
    }
}
