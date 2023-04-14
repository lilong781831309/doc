package org.xinhua.example.datastruct.tree;

import org.xinhua.example.datastruct.collection.ArrayQueue;
import org.xinhua.example.datastruct.collection.ArrayStack;
import org.xinhua.example.datastruct.collection.Queue;
import org.xinhua.example.datastruct.collection.Stack;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/14 5:24
 * @Description: 二叉搜索树    Node节点带parent指针
 * @Version: 1.0
 */
public class BinarySearchTree<E> {

    private final Comparator<? super E> comparator;
    private Node<E> root;
    private int size;

    public BinarySearchTree() {
        comparator = null;
    }

    public BinarySearchTree(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    /**
     * 插入结点
     */
    public void add(E e) {
        if (root == null) {
            root = new Node<>(e);
            size = 1;
            return;
        }
        Node<E> p = root, parent = null;
        int cmp = 0;

        if (comparator != null) {
            while (p != null) {
                cmp = comparator.compare(e, p.e);
                parent = p;
                if (cmp < 0) {
                    p = p.left;
                } else if (cmp > 0) {
                    p = p.right;
                } else {
                    return;
                }
            }
        } else {
            while (p != null) {
                cmp = ((Comparable) e).compareTo((Comparable) p.e);
                parent = p;
                if (cmp < 0) {
                    p = p.left;
                } else if (cmp > 0) {
                    p = p.right;
                } else {
                    return;
                }
            }
        }

        if (cmp < 0) {
            parent.left = new Node(parent, null, null, e);
        } else {
            parent.right = new Node(parent, null, null, e);
        }

        size++;
    }

    /**
     * 删除指定结点
     */
    public void remove(E e) {
        if (root == null || e == null) {
            return;
        }
        Node<E> p = search(e);
        if (p == null) {
            return;
        }

        if (p.left == null) {
            transplant(p, p.right);
        } else if (p.right == null) {
            transplant(p, p.left);
        } else {
            // 替换前驱
/*
            Node<E> predecessor = predecessor(p);
            if (p != predecessor.parent) {
                transplant(predecessor, predecessor.left);
                predecessor.left = p.left;
                predecessor.left.parent = predecessor;
            }
            predecessor.right = p.right;
            predecessor.right.parent = predecessor;
            transplant(p, predecessor);
*/

            // 替换后继

            Node<E> successor = successor(p);
            if (p != successor.parent) {
                transplant(successor, successor.right);
                successor.right = p.right;
                successor.right.parent = successor;
            }
            successor.left = p.left;
            successor.left.parent = successor;
            transplant(p, successor);

        }

        size--;
    }

    /**
     * 最小值
     */
    public E min() {
        return root == null ? null : min(root).e;
    }

    /**
     * 最大值
     */
    public E max() {
        return root == null ? null : max(root).e;
    }

    /**
     * 最小节点
     */
    private Node<E> min(Node<E> p) {
        if (p == null) {
            return null;
        }
        while (p.left != null) {
            p = p.left;
        }
        return p;
    }


    /**
     * 最大节点
     */
    private Node<E> max(Node<E> p) {
        if (p == null) {
            return null;
        }
        while (p.right != null) {
            p = p.right;
        }
        return p;
    }

    /**
     * 搜索
     */
    private Node<E> search(E e) {
        if (root == null || e == null) {
            return null;
        }
        Node<E> p = root;
        int cmp;
        if (comparator != null) {
            while (p != null) {
                cmp = comparator.compare(e, p.e);
                if (cmp < 0) {
                    p = p.left;
                } else if (cmp > 0) {
                    p = p.right;
                } else {
                    break;
                }
            }
        } else {
            while (p != null) {
                cmp = ((Comparable) e).compareTo((Comparable) p.e);
                if (cmp < 0) {
                    p = p.left;
                } else if (cmp > 0) {
                    p = p.right;
                } else {
                    break;
                }
            }
        }
        return p;
    }

    /**
     * 前驱节点
     */
    private Node<E> predecessor(Node<E> p) {
        if (root == null || p == null) {
            return null;
        }
        if (p.left != null) {
            return max(p.left);
        }
        Node<E> x = p, y = p.parent;
        while (y != null && x == y.left) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    /**
     * 后继节点
     */
    private Node<E> successor(Node<E> p) {
        if (root == null || p == null) {
            return null;
        }
        if (p.right != null) {
            return min(p.right);
        }
        Node<E> x = p, y = p.parent;
        while (y != null && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    /**
     * 用一棵以v为根的子树来替换一棵以u为根的子树时,结点u的父节点就变为结点v的父节点,并且最后v成为u的父节点的相应孩子
     */
    private void transplant(Node u, Node v) {
        if (u == null || u.parent == null) {
            if (v != null) {
                v.parent = null;
            }
            root = v;
            return;
        }
        if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
        u.parent = u.left = u.right = null;
    }

    /**
     * 前序遍历
     */
    public void preOrder(Consumer<? super E> action) {
        if (root != null) {
            Stack<Node<E>> stack = new ArrayStack<>();
            stack.push(root);
            Node<E> p = null;
            while (!stack.empty()) {
                p = stack.pop();
                action.accept(p.e);
                if (p.right != null) {
                    stack.push(p.right);
                }
                if (p.left != null) {
                    stack.push(p.left);
                }
            }
        }
    }

    /**
     * 中序遍历
     */
    public void inOrder(Consumer<? super E> action) {
        if (root != null) {
            Stack<Node<E>> stack = new ArrayStack<>();
            Node<E> p = root;
            while (!stack.empty() || p != null) {
                while (p != null) {
                    stack.push(p);
                    p = p.left;
                }
                p = stack.pop();
                action.accept(p.e);
                p = p.right;
            }
        }
    }

    /**
     * 后序遍历
     */
    public void postOrder(Consumer<? super E> action) {
        if (root != null) {
            Stack<Node<E>> stack = new ArrayStack<>();
            Node<E> p = root;
            Node<E> visit = null;
            while (!stack.empty() || p != null) {
                while (p != null) {
                    stack.push(p);
                    p = p.left;
                }
                p = stack.peek();
                if (p.right == null || p.right == visit) {
                    visit = stack.pop();
                    action.accept(p.e);
                    p = null;
                } else {
                    p = p.right;
                }
            }
        }
    }

    /**
     * 层次遍历
     */
    public void levelOrder(Consumer<? super E> action) {
        if (root != null) {
            Queue<Node<E>> queue = new ArrayQueue<>();
            queue.offer(root);
            Node<E> p = null;

            while (!queue.empty()) {
                p = queue.poll();
                action.accept(p.e);
                if (p.left != null) {
                    queue.offer(p.left);
                }
                if (p.right != null) {
                    queue.offer(p.right);
                }
            }
        }
    }

    static class Node<E> {
        Node parent, left, right;
        E e;

        public Node() {
        }

        public Node(E e) {
            this.e = e;
        }

        public Node(Node parent, Node left, Node right, E e) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.e = e;
        }
    }

}
