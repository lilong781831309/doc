package org.xinhua.example.datastruct.tree;

import java.util.Comparator;

/**
 * @Author: lilong
 * @createDate: 2023/4/13 20:05
 * @Description: 二叉搜索树            Node节点带parent指针
 * @Version: 1.0
 */
public class BST<E> extends AbstractBST<E> {

    public BST() {
        super();
    }

    public BST(Comparator<? super E> comparator) {
        super(comparator);
    }

    /**
     * 插入结点
     */
    @Override
    public void add(E e) {
        if (root == null) {
            root = new BSTNode<>(e);
            size = 1;
            return;
        }
        BSTNode<E> parent = (BSTNode) findInsertParent(e);
        if (parent == null) return;
        if (compare(e, parent.e) < 0) {
            parent.left = new BSTNode(parent, e);
        } else {
            parent.right = new BSTNode(parent, e);
        }
        size++;
    }

    /**
     * 删除指定结点
     */
    @Override
    public void remove(E e) {
        if (root == null || e == null) {
            return;
        }
        BSTNode<E> p = (BSTNode) search(e);
        if (p == null) {
            return;
        }

        if (p.left == null) {
            transplant(p, (BSTNode) p.right);
        } else if (p.right == null) {
            transplant(p, (BSTNode) p.left);
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
                transplant(successor, (BSTNode) successor.right);
                successor.right = p.right;
                ((BSTNode) successor.right).parent = successor;
            }
            successor.left = p.left;
            ((BSTNode) successor.left).parent = successor;
            transplant(p, successor);

        }

        size--;
    }


    /**
     * 前驱节点
     */
    @Override
    protected BSTNode<E> predecessor(Node<E> p) {
        if (root == null || p == null) {
            return null;
        }
        if (p.left != null) {
            return (BSTNode) max(p.left);
        }
        BSTNode<E> x = (BSTNode) p, y = ((BSTNode) p).parent;
        while (y != null && x == y.left) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    /**
     * 后继节点
     */
    @Override
    protected BSTNode<E> successor(Node<E> p) {
        if (root == null || p == null) {
            return null;
        }
        if (p.right != null) {
            return (BSTNode) min(p.right);
        }
        BSTNode<E> x = (BSTNode) p, y = ((BSTNode) p).parent;
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
        BSTNode<E> r = (BSTNode) p.right;
        transplant(p, r);
        p.right = r.left;
        if (r.left != null) {
            ((BSTNode) r.left).parent = p;
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
        BSTNode<E> l = (BSTNode) p.left;
        transplant(p, l);
        p.left = l.right;
        if (l.right != null) {
            ((BSTNode) l.right).parent = p;
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

    protected static class BSTNode<E> extends Node<E> {
        // public  BSTNode parent, left, right;
        protected BSTNode parent;

        public BSTNode() {
            super();
        }

        public BSTNode(E e) {
            super(e);
        }

        public BSTNode(BSTNode parent, E e) {
            super(e);
            this.parent = parent;
        }
    }

}
