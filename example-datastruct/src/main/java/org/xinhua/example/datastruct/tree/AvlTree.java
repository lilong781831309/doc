package org.xinhua.example.datastruct.tree;

import java.util.Comparator;

/**
 * @Author: lilong
 * @createDate: 2023/4/14 20:05
 * @Description: 平衡二叉树      利用平衡因子实现
 * @Version: 1.0
 */
public class AvlTree<E> extends BST<E> {

    public AvlTree() {
        super();
    }

    public AvlTree(Comparator<E> comparator) {
        super(comparator);
    }

    @Override
    public void add(E e) {
        if (e == null) {
            return;
        }
        if (root == null) {
            root = new AvlNode<>(null, e);
            size = 1;
            return;
        }

        AvlNode<E> parent = (AvlNode) findInsertParent(e);
        if (parent == null) return;
        AvlNode<E> node = new AvlNode<>(parent, e);

        if (compare(e, parent.e) < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        balanceAfterAdd((AvlNode) node.parent);
        size++;
    }

    public void remove(E e) {
        if (root == null || e == null) return;
        AvlNode<E> node = (AvlNode) search(e);
        if (node == null) return;
        if (node.left != null && node.right != null) {
            AvlNode<E> predecessor = (AvlNode) predecessor(node);
            node.e = predecessor.e;
            node = predecessor;
        }
        AvlNode<E> parent = (AvlNode) node.parent;
        if (node.left == null && node.right == null) {
            transplant(node, null);
        } else if (node.left == null) {
            transplant(node, (AvlNode) node.right);
        } else {
            transplant(node, (AvlNode) node.left);
        }
        balanceAfterDelete(parent);
        size--;
    }

    private void balanceAfterAdd(AvlNode<E> p) {
        while (p != null) {
            int oldHight = p.hight;
            int bf = getBalanceFactor(p);
            if (bf == 2) {
                p = balanceLeft(p);
            } else if (bf == -2) {
                p = balanceRight(p);
            } else {
                updateHight(p);
            }
            if (p.hight == oldHight) break;
            p = (AvlNode) p.parent;
        }
    }

    private void balanceAfterDelete(AvlNode<E> p) {
        balanceAfterAdd(p);
    }

    private AvlNode<E> balanceLeft(AvlNode<E> p) {
        if (getBalanceFactor((AvlNode) p.left) == -1) {
            rotateLeft((AvlNode) p.left);
        }
        return rotateRight(p);
    }

    private AvlNode<E> balanceRight(AvlNode<E> p) {
        if (getBalanceFactor((AvlNode) p.right) == 1) {
            rotateRight((AvlNode) p.right);
        }
        return rotateLeft(p);
    }

    private int getBalanceFactor(AvlNode<E> p) {
        return getHight((AvlNode) p.left) - getHight((AvlNode) p.right);
    }

    private int getHight(AvlNode<E> p) {
        if (p == null) return 0;
        int lh = p.left == null ? 0 : ((AvlNode) p.left).hight;
        int rh = p.right == null ? 0 : ((AvlNode) p.right).hight;
        return lh > rh ? lh + 1 : rh + 1;
    }

    private void updateHight(AvlNode<E> p) {
        if (p == null) return;
        int lh = p.left == null ? 0 : ((AvlNode) p.left).hight;
        int rh = p.right == null ? 0 : ((AvlNode) p.right).hight;
        p.hight = lh > rh ? lh + 1 : rh + 1;
    }

    @Override
    protected AvlNode<E> rotateLeft(BSTNode<E> node) {
        AvlNode<E> p = (AvlNode) node;
        AvlNode<E> r = (AvlNode) p.right;
        super.rotateLeft(node);
        updateHight(p);
        updateHight(r);
        return r;
    }

    @Override
    protected AvlNode<E> rotateRight(BSTNode<E> node) {
        AvlNode<E> p = (AvlNode) node;
        AvlNode<E> l = (AvlNode) p.left;
        super.rotateRight(node);
        updateHight(p);
        updateHight(l);
        return l;
    }


    static class AvlNode<E> extends BSTNode<E> {
        int hight = 1;

        public AvlNode() {
        }

        public AvlNode(AvlNode<E> parent, E e) {
            this.parent = parent;
            this.e = e;
        }
    }
}
