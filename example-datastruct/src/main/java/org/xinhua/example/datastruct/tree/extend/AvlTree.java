package org.xinhua.example.datastruct.tree.extend;

import java.util.Comparator;

/**
 * @Author: lilong
 * @createDate: 2023/4/13 20:05
 * @Description: 平衡二叉树      利用平衡因子实现
 * @Version: 1.0
 */
public class AvlTree<E> extends BsTree<E> {

    private static final int LH = 1;    //左高
    private static final int EH = 0;    //等高
    private static final int RH = -1;   //右高

    public AvlTree() {
        super();
    }

    public AvlTree(Comparator<? super E> comparator) {
        super(comparator);
    }

    /**
     * 插入结点
     */
    @Override
    public void add(E e) {
        if (e == null) return;
        if (root == null) {
            root = new AVLNode(null, e);
            size = 1;
            return;
        }
        AVLNode<E> p = (AVLNode)root;
        AVLNode<E> parent = null;
        int cmp = 0;
        if (comparator != null) {
            while (p != null) {
                parent = p;
                cmp = comparator.compare(e, p.e);
                if (cmp < 0) {
                    p =(AVLNode) p.left;
                } else if (cmp > 0) {
                    p = (AVLNode)p.right;
                } else {
                    return;
                }
            }
        } else {
            while (p != null) {
                parent = p;
                cmp = ((Comparable) e).compareTo(p.e);
                if (cmp < 0) {
                    p = (AVLNode)p.left;
                } else if (cmp > 0) {
                    p = (AVLNode)p.right;
                } else {
                    return;
                }
            }
        }

        AVLNode node = new AVLNode(parent, e);
        if (cmp < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        //调整平衡因子
        balanceAfterAdd(node);
        size++;
    }

    private void balanceAfterAdd(AVLNode<E> p) {
        AVLNode<E> parent = (AVLNode) p.parent;
        while (parent != null) {
            if (p == parent.left) {
                parent.bf++;
            } else {
                parent.bf--;
            }
            if (parent.bf == EH) {
                break;
            } else if (parent.bf == 2) {
                //左子树高于右子树; 如果R的左子树的根节点的BF为1时，做右旋；如果R的左子树的根节点的BF为-1时，先左旋然后再右旋
                balanceLeft(parent);
                break;
            } else if (parent.bf == -2) {
                //右子树高于左子树：如果R的右子树的根节点的BF为1时，先右旋后左旋;如果R的右子树的根节点的BF为-1时，做左旋
                balanceRight(parent);
                break;
            }
            p = parent;
            parent = (AVLNode) parent.parent;
        }
    }

    /**
     * 删除指定结点
     */
    @Override
    public void remove(E e) {
        if (root == null || e == null) return;
        AVLNode<E> node = (AVLNode) search(e);
        if (node == null) return;

        if (node.left != null && node.right != null) {
            //后继节点替换
            //Node<E> successor = successor(node);
            //node.e = successor.e;
            //node = successor;

            //前驱节点替换
            AVLNode<E> predecessor = (AVLNode) predecessor(node);
            node.e = predecessor.e;
            node = predecessor;
        }
        if (node.left == null && node.right == null) {
            //当node不是根节点时,调整node.parent.balance
            if (node.parent != null) {
                if (node == node.parent.left) {
                    ((AVLNode) node.parent).bf--;
                } else {
                    ((AVLNode) node.parent).bf++;
                }
            }
            transplant(node, null);
            balanceAfterDelete((AVLNode) node.parent);
        } else if (node.left == null) {
            transplant(node, (AVLNode) node.right);
            //node.right.balance 不需要调整
            balanceAfterDelete((AVLNode) node.right);
        } else {//node.right == null
            transplant(node, (AVLNode) node.left);
            //node.left.balance 不需要调整
            balanceAfterDelete((AVLNode) node.left);
        }
        size--;
    }

    /**
     * 删除之后调整树平衡
     */
    private void balanceAfterDelete(AVLNode<E> p) {
        if (p == null) return;
        AVLNode<E> parent;
        while (true) {
            //节点高度没有变,不需要调整
            if (p.bf == LH || p.bf == RH) break;
            if (p.bf == 2) {
                int bf = ((AVLNode) p.left).bf;
                p = balanceLeft(p);
                //调整完高度没有变,退出循环
                if (bf == EH) break;
            } else if (p.bf == -2) {
                int bf = ((AVLNode) p.right).bf;
                p = balanceRight(p);
                //调整完高度没有变,退出循环
                if (bf == EH) break;
            }
            parent = (AVLNode) p.parent;
            if (parent == null) break;
            if (p == parent.left) {
                parent.bf--;
            } else {
                parent.bf++;
            }
            p = parent;
        }
    }

    /**
     *
     *******************************************************************************************************************
     *  左子树的左边高  ll.bf > lr.bf
     *  p右旋后  p.bf = l.bf = EH , 其他节点 bf 不变 ,以l为根节点的子树高度下降
     *
     *                            p                               p                              p
     *                           /  \                           /   \                          /   \
     *                          l    r                         l     r                        l     r
     *                         /  \                           /  \                           /  \
     *                       ll   lr                         ll  lr                        ll   lr
     *                      /                                 \                            / \
     *                     d                                  f                          d    f
     *
     *                          ↓↓                               ↓↓                               ↓↓
     *                          ↓↓                               ↓↓                               ↓↓
     *                          ↓↓                               ↓↓                               ↓↓
     *                          ↓↓                               ↓↓                               ↓↓
     *
     *                            l                               l                                l
     *                          /   \                           /   \                            /   \
     *                        ll     p                        ll     p                         ll     p
     *                       /     /  \                        \    /  \                      /  \   /  \
     *                     d     lr    r                       f   lr   r                    d   f  lr   r
     *******************************************************************************************************************
     *  左子树的右边高  ll.bf < lr.bf,分情况处理,  l左旋,再p右旋后 lr.bf = EH,以lr为根节点的子树高度下降
     *                           (1)                            (2)                            (3)
     *                     p.bf = RH                        p.bf = EH                       p.bf = EH
     *                     l.bf = EH                        l.bf = LH                       l.bf = EH
     *
     *                            p                              p                             p
     *                          /   \                          /  \                           / \
     *                         l     r                        l    r                        l    r
     *                         / \                           /  \                          / \
     *                       ll  lr                         ll  lr                       ll   lr
     *                           /                               \                           /  \
     *                          d                                 f                         d    f
     *
     *                          ↓↓                               ↓↓                               ↓↓
     *                          ↓↓                               ↓↓                               ↓↓
     *                          ↓↓                               ↓↓                               ↓↓
     *                          ↓↓                               ↓↓                               ↓↓
     *
     *                            lr                           lr                              lr
     *                           /  \                         /  \                           /    \
     *                          l    p                       l    p                         l      p
     *                         / \    \                     /    / \                       / \    / \
     *                        ll  d    r                   ll   f   r                     ll  d  f   r
     *******************************************************************************************************************
     *  左子树的左右一样高  ll.bf == lr.bf
     *  p右旋后  p.bf = LH, l.bf = RH , 其他节点 bf 不变 ,以l为根节点的子树高度不变
     *
     *                              p
     *                            /   \
     *                           l     r
     *                         /  \
     *                       ll    lr
     *                      / \    / \
     *                    d    f  g   k
     *
     *                          ↓↓
     *                          ↓↓
     *                          ↓↓
     *                          ↓↓
     *
     *                               l
     *                            /     \
     *                          ll       p
     *                         / \     /  \
     *                        d   f   lr   r
     *                                / \
     *                               g   k
     * *****************************************************************************************************************
     *
     * 平衡左子树
     */
    private AVLNode<E> balanceLeft(AVLNode p) {
        AVLNode<E> l = (AVLNode) p.left;
        switch (l.bf) {
            case LH:
                p.bf = l.bf = EH;
                p = (AVLNode) rotateRight(p);
                break;
            case RH:
                switch (((AVLNode) l.right).bf) {
                    case LH:
                        p.bf = RH;
                        l.bf = EH;
                        break;
                    case RH:
                        p.bf = EH;
                        l.bf = LH;
                        break;
                    case EH:
                        p.bf = l.bf = EH;
                        break;
                }
                ((AVLNode) l.right).bf = EH;
                rotateLeft(l);
                p = (AVLNode) rotateRight(p);
                break;
            case EH:
                p.bf = LH;
                l.bf = RH;
                p = (AVLNode) rotateRight(p);
                break;
        }
        return p;
    }

    /**
     * 平衡右子树
     */
    private AVLNode<E> balanceRight(AVLNode p) {
        AVLNode<E> r = (AVLNode) p.right;
        switch (r.bf) {
            case LH:
                switch (((AVLNode) r.left).bf) {
                    case LH:
                        p.bf = EH;
                        r.bf = RH;
                        break;
                    case RH:
                        p.bf = LH;
                        r.bf = EH;
                        break;
                    case EH:
                        p.bf = r.bf = EH;
                        break;
                }
                ((AVLNode) r.left).bf = EH;
                rotateRight(r);
                p = (AVLNode) rotateLeft(p);
                break;
            case RH:
                p.bf = r.bf = EH;
                p = (AVLNode) rotateLeft(p);
                break;
            case EH:
                p.bf = RH;
                r.bf = LH;
                p = (AVLNode) rotateLeft(p);
                break;
        }

        return p;
    }

    protected static class AVLNode<E> extends BSTNode<E> {
        int bf = EH;    //平衡因子，只能为1或0或-1

        public AVLNode() {
            super();
        }

        public AVLNode(AVLNode parent, E e) {
            super(e);
            this.parent = parent;
        }
    }
}
