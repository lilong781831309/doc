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
 * @Description: 二叉搜索树    Node节点带parent指针
 * @Version: 1.0
 */
public class BST<E> {

    protected final Comparator<? super E> comparator;
    protected BSTNode<E> root;
    protected int size;

    public BST() {
        comparator = null;
    }

    public BST(Comparator<? super E> comparator) {
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
            root = new BSTNode<>(e);
            size = 1;
            return;
        }
        BSTNode<E> p = root;
        BSTNode<E> parent = null;
        int cmp = 0;
        if (comparator != null) {
            while (p != null) {
                parent = p;
                cmp = comparator.compare(e, p.e);
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
                parent = p;
                cmp = ((Comparable) e).compareTo(p.e);
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
            parent.left = new BSTNode(parent, e);
        } else {
            parent.right = new BSTNode(parent, e);
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
        BSTNode<E> p = search(e);
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

            BSTNode<E> successor = successor(p);
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
    protected BSTNode<E> min(BSTNode<E> p) {
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
    protected BSTNode<E> max(BSTNode<E> p) {
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
    protected BSTNode<E> search(E e) {
        if (root == null || e == null) {
            return null;
        }
        BSTNode<E> p = root;
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
    protected BSTNode<E> predecessor(BSTNode<E> p) {
        if (root == null || p == null) {
            return null;
        }
        if (p.left != null) {
            return max(p.left);
        }
        BSTNode<E> x = p, y = p.parent;
        while (y != null && x == y.left) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    /**
     * 后继节点
     */
    protected BSTNode<E> successor(BSTNode<E> p) {
        if (root == null || p == null) {
            return null;
        }
        if (p.right != null) {
            return min(p.right);
        }
        BSTNode<E> x = p, y = p.parent;
        while (y != null && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    /**
     * 左旋操作（右孩子的右子树插入节点）
     * 返回新的子树根节点
     */
    protected BSTNode<E> rotateLeft(BSTNode<E> p) {
        BSTNode<E> r = p.right;
        transplant(p, r);
        p.right = r.left;
        if (r.left != null) {
            r.left.parent = p;
        }
        r.left = p;
        p.parent = r;
        return r;
    }

    /**
     * 右旋操作（左孩子的左子树插入节点）
     * 返回新的子树根节点
     */
    protected BSTNode<E> rotateRight(BSTNode<E> p) {
        BSTNode<E> l = p.left;
        transplant(p, l);
        p.left = l.right;
        if (l.right != null) {
            l.right.parent = p;
        }
        l.right = p;
        p.parent = l;
        return l;
    }

    /**
     * 用一棵以v为根的子树来替换一棵以u为根的子树时,结点u的父节点就变为结点v的父节点,并且最后v成为u的父节点的相应孩子
     */
    protected void transplant(BSTNode u, BSTNode v) {
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
    }


    /**
     * 前序遍历
     */
    public void preOrder(Consumer<? super E> action) {
        if (root != null) {
            Stack<BSTNode<E>> stack = new ArrayStack<>();
            stack.push(root);
            BSTNode<E> p;
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
            Stack<BSTNode<E>> stack = new ArrayStack<>();
            BSTNode<E> p = root;
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
            Stack<BSTNode<E>> stack = new ArrayStack<>();
            BSTNode<E> p = root;
            BSTNode<E> visit = null;
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
            Queue<BSTNode<E>> queue = new ArrayQueue<>();
            queue.offer(root);
            BSTNode<E> p = null;
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

    protected static class BSTNode<E> {
        protected BSTNode parent, left, right;
        protected E e;

        public BSTNode() {
        }

        public BSTNode(E e) {
            this.e = e;
        }

        public BSTNode(BSTNode parent, E e) {
            this.parent = parent;
            this.e = e;
        }
    }

}
