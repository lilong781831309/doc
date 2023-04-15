package org.xinhua.example.datastruct.tree;

import java.util.Comparator;

/**
 * @Author: lilong
 * @createDate: 2023/4/14 5:24
 * @Description: 二叉搜索树    Node节点不带parent指针
 * @Version: 1.0
 */
public class BsTree<E> extends AbstractBST<E> {

    public BsTree() {
        super();
    }

    public BsTree(Comparator<? super E> comparator) {
        super(comparator);
    }

    /**
     * 插入结点
     */
    @Override
    public void add(E e) {
        if (root == null) {
            root = new Node<>(e);
            size = 1;
            return;
        }
        Node<E> parent = findInsertParent(e);
        if (parent == null) return;
        if (compare(e, parent.e) < 0) {
            parent.left = new Node<>(e);
        } else {
            parent.right = new Node<>(e);
        }
        size++;
    }

    /**
     * 删除指定结点
     */
    @Override
    public void remove(E e) {
        if (root == null) {
            return;
        }

        Node<E> p = root, parent = null;
        int cmp = 0;
        if (comparator != null) {
            while (p != null) {
                cmp = comparator.compare(e, p.e);
                if (cmp < 0) {
                    parent = p;
                    p = p.left;
                } else if (cmp > 0) {
                    parent = p;
                    p = p.right;
                } else {
                    break;
                }
            }
        } else {
            while (p != null) {
                cmp = ((Comparable) e).compareTo((Comparable) (p.e));
                if (cmp < 0) {
                    parent = p;
                    p = p.left;
                } else if (cmp > 0) {
                    parent = p;
                    p = p.right;
                } else {
                    break;
                }
            }
        }

        if (p == null) {
            return;
        }
        if (p.left == null) {
            transplant(parent, p, p.right);
        } else if (p.right == null) {
            transplant(parent, p, p.left);
        } else {
            //替换前驱与前驱父节点
/*            Node rp = p, r = p.left;
            while (r.right != null) {
                rp = r;
                r = r.right;
            }
            if (rp != p) {
                transplant(rp, r, r.left);
                r.left = p.left;
                p.left = r;
            }
            r.right = p.right;
            transplant(parent, p, r);*/

            //替换后继与后继父节点
            Node rp = p, r = p.right;
            while (r.left != null) {
                rp = r;
                r = r.left;
            }
            if (rp != p) {
                transplant(rp, r, r.right);
                r.right = p.right;
                p.right = r;
            }
            r.left = p.left;
            transplant(parent, p, r);

        }
        size--;
    }

    /**
     * 用一棵以v为根的子树来替换一棵以u为根的子树时,结点u的父节点就变为结点v的父节点,并且最后v成为u的父节点的相应孩子
     * p为u的父节点
     */
    private void transplant(Node p, Node u, Node v) {
        if (p == null) {
            root = v;
        } else if (u == p.left) {
            p.left = v;
        } else {
            p.right = v;
        }
    }

}
