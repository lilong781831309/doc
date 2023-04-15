package org.xinhua.example.datastruct.tree;

import org.xinhua.example.datastruct.collection.ArrayQueue;
import org.xinhua.example.datastruct.collection.ArrayStack;
import org.xinhua.example.datastruct.collection.Queue;
import org.xinhua.example.datastruct.collection.Stack;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/16 3:49
 * @Description: 抽象二叉搜索树    Node节点不带parent指针
 * @Version: 1.0
 */
public abstract class AbstractBST<E> {

    protected final Comparator<? super E> comparator;
    protected Node<E> root;
    protected int size;

    public AbstractBST() {
        comparator = null;
    }

    public AbstractBST(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    abstract void add(E e);

    abstract void remove(E e);

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
    protected Node<E> min(Node<E> p) {
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
    protected Node<E> max(Node<E> p) {
        if (p == null) {
            return null;
        }
        while (p.right != null) {
            p = p.right;
        }
        return p;
    }

    /**
     * 查找插入位置的父节点，如果对应的值已经存在则返回空
     */
    protected Node<E> findInsertParent(E e) {
        if (root == null) return null;
        Node<E> p = root, parent = null;
        int cmp;
        if (comparator != null) {
            while (p != null) {
                parent = p;
                cmp = comparator.compare(e, p.e);
                if (cmp < 0) {
                    p = p.left;
                } else if (cmp > 0) {
                    p = p.right;
                } else {
                    return null;
                }
            }
        } else {
            while (p != null) {
                parent = p;
                cmp = ((Comparable) e).compareTo(p.e);
                if (cmp < 0) {
                    p = p.left;
                } else if (cmp > 0) {
                    p = p.right;
                } else {
                    return null;
                }
            }
        }
        return parent;
    }

    /**
     * 比较大小
     */
    protected int compare(E e, E e2) {
        if (e == null || e2 == null) {
            throw new RuntimeException();
        }
        if (comparator != null) {
            return comparator.compare(e, e2);
        } else {
            return ((Comparable) e).compareTo(e2);
        }
    }

    /**
     * 搜索
     */
    protected Node<E> search(E e) {
        if (root == null || e == null) {
            return null;
        }
        Node<E> p = root;
        int cmp = 0;
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
                cmp = ((Comparable) e).compareTo((Comparable) (p.e));
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
    protected Node<E> predecessor(Node<E> p) {
        if (p == null) {
            return null;
        }
        if (p.left != null) {
            return max(p.left);
        }

        Node<E> predecessor = null;
        E e = p.e;
        p = root;
        if (comparator != null) {
            int c = comparator.compare(p.e, e);
            while (c != 0) {
                while (c > 0) {
                    p = p.left;
                    if (p == null) {
                        return predecessor;
                    }
                    c = comparator.compare(p.e, e);
                }
                while (c < 0) {
                    predecessor = p;
                    p = p.right;
                    if (p == null) {
                        return predecessor;
                    }
                    c = comparator.compare(p.e, e);
                }
            }
        } else {
            int c = ((Comparable) p.e).compareTo((Comparable) e);
            while (c != 0) {
                while (c > 0) {
                    p = p.left;
                    if (p == null) {
                        return predecessor;
                    }
                    c = ((Comparable) p.e).compareTo((Comparable) e);
                }
                while (c < 0) {
                    predecessor = p;
                    p = p.right;
                    if (p == null) {
                        return predecessor;
                    }
                    c = ((Comparable) p.e).compareTo((Comparable) e);
                }
            }
        }

        return predecessor;
    }

    /**
     * 后继节点
     */
    protected Node<E> successor(Node<E> p) {
        if (p == null) {
            return null;
        }
        if (p.right != null) {
            return min(p.right);
        }

        Node<E> successor = null;
        E e = p.e;
        p = root;
        if (comparator != null) {
            int c = comparator.compare(p.e, e);
            while (c != 0) {
                while (c < 0) {
                    p = p.right;
                    if (p == null) {
                        return successor;
                    }
                    c = comparator.compare(p.e, e);
                }
                while (c > 0) {
                    successor = p;
                    p = p.left;
                    if (p == null) {
                        return successor;
                    }
                    c = comparator.compare(p.e, e);
                }
            }
        } else {
            int c = ((Comparable) p.e).compareTo((Comparable) e);
            while (c != 0) {
                while (c < 0) {
                    p = p.right;
                    if (p == null) {
                        return successor;
                    }
                    c = ((Comparable) p.e).compareTo((Comparable) e);
                }
                while (c > 0) {
                    successor = p;
                    p = p.left;
                    if (p == null) {
                        return successor;
                    }
                    c = ((Comparable) p.e).compareTo((Comparable) e);
                }
            }
        }

        return successor;
    }

    /**
     * 前序遍历
     */
    public void preOrder(Consumer<? super E> action) {
        if (root != null) {
            Stack<Node<E>> stack = new ArrayStack<>();
            stack.push(root);
            Node<E> p;
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

    protected static class Node<E> {
        protected Node left, right;
        protected  E e;

        public Node() {
        }

        public Node(E e) {
            this.e = e;
        }

        public Node(Node left, Node right, E e) {
            this.left = left;
            this.right = right;
            this.e = e;
        }
    }
}
