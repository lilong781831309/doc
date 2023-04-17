package org.xinhua.example.datastruct.tree;

import java.util.Comparator;

/**
 * @Author: lilong
 * @createDate: 2023/4/16 19:25
 * @Description: 红黑树
 * @Version: 1.0
 */
public class RBT<E> extends BST<E> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public RBT() {
    }

    public RBT(Comparator<? super E> comparator) {
        super(comparator);
    }

    @Override
    public void add(E e) {
        if (e == null) return;
        if (root == null) {
            root = new RBTNode<>(BLACK, e);
            size = 1;
            return;
        }


        RBTNode<E> p = (RBTNode) root;
        RBTNode<E> parent = null;
        int cmp = 0;
        if (comparator != null) {
            while (p != null) {
                parent = p;
                cmp = comparator.compare(e, p.e);
                if (cmp < 0) {
                    p = (RBTNode) p.left;
                } else if (cmp > 0) {
                    p = (RBTNode) p.right;
                } else {
                    return;
                }
            }
        } else {
            while (p != null) {
                parent = p;
                cmp = ((Comparable) e).compareTo(p.e);
                if (cmp < 0) {
                    p = (RBTNode) p.left;
                } else if (cmp > 0) {
                    p = (RBTNode) p.right;
                } else {
                    return;
                }
            }
        }
        RBTNode<E> node = new RBTNode(parent, e);

        if (cmp < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }

        fixupAfterAdd(node);
        size++;
    }

    @Override
    public void remove(E e) {
        if (root == null || e == null) return;
        RBTNode<E> node = (RBTNode) search(e);
        if (node == null) return;

        if (node.left != null && node.right != null) {
            RBTNode<E> predecessor = (RBTNode) predecessor(node);
            node.e = predecessor.e;
            node = predecessor;
        }

        if (node.left == null && node.right == null) {
            fixupAfterRemove(node, null);
            transplant(node, null);
        } else if (node.left == null) {
            transplant(node, (RBTNode) node.right);
            fixupAfterRemove(node, (RBTNode) node.right);
        } else {
            transplant(node, (RBTNode) node.left);
            fixupAfterRemove(node, (RBTNode) node.left);
        }
        size--;
    }


    /**
     *******************************************************************************************************************
     * 1、add.color = RED && parent.color = RED && uncle.color = RED
     *------------------------------------------------------------------------------------------------------------------
     *    parent.color = BLACK、uncle.color = BLACK、grand.color = RED、p = grand、parent = p.parent
     *
     *                   root                        root
     *                  /    \                     /      \
     *            grand（B）   r                grand(R)   r
     *              /     \                     /      \
     *        parent(R)  uncle(R)         parent(B)    uncle(B)
     *           /   \                      /     \
     *        add(R) other(B)           add(R)   other(B)
     *
     *******************************************************************************************************************
     * 2、add.color = RED && parent.color = RED && uncle.color = BLACK
     *------------------------------------------------------------------------------------------------------------------
     *  2.1、    parent == grand.left          rotateRight(grand)            parent.color = BLACK
     *       && parent == grand.left                                         grand.color = RED
     *
     *                   root                        root                         root
     *                  /    \                     /      \                     /      \
     *            grand（B）   r                parent(R)   r                parent(B)    r
     *              /     \                     /      \                     /      \
     *        parent(R)  uncle(B)           add(R)    grand(B)           add(R)   grand(R)
     *           /   \                              /       \                    /       \
     *        add(R) other(B)                     other(B)  uncle(B)          other(B) uncle(B)
     *------------------------------------------------------------------------------------------------------------------
     *   2.2、    parent == grand.left       rotateLeft(parent)      rotateRight(grand)           add.color = BLACK
     *        && parent == grand.right                                                            grand.color = RED
     *
     *                   root                        root                   root                     root
     *                  /    \                     /      \               /      \                 /      \
     *            grand（B）   r                grand(B)   r          add(R)       r           add(B)       r
     *              /     \                     /      \              /     \                   /     \
     *        parent(R)  uncle(B)         add(R)    uncle(B)     parent(R)  grand(B)       parent(R)  grand(R)
     *           /   \                      /                     /           \               /         \
     *      other(B) add(R)              parent(R)            other(B)       uncle(B)      other(B)    uncle(B)
     *                                   /
     *                               other(B)
     *------------------------------------------------------------------------------------------------------------------
     *  2.3、    parent == grand.right          rotateLeft(grand)             parent.color = BLACK
     *       && parent == grand.right                                         grand.color = RED
     *
     *              root                           root                           root
     *            /    \                        /      \                       /      \
     *          l   grand（B）                 l      parent(R)               l     parent(B)
     *               /     \                        /      \                      /      \
     *          uncle(B) parent(R)             grand(B)   add(R)              grand(R)   add(R)
     *                   /    \                  /    \                       /       \
     *               other(B)  add(R)        uncle(B)  other(B)            uncle(B)  other(B)
     *------------------------------------------------------------------------------------------------------------------
     *   2.4、    parent == grand.right       rotateRight(parent)      rotateLeft(grand)         add.color = BLACK
     *        && parent == grand.left                                                            grand.color = RED
     *
     *              root                       root                      root                      root
     *            /    \                     /      \                   /     \                   /     \
     *          l   grand（B）              l     grand(B)             l     add(R)              l      add(B)
     *               /     \                    /      \                    /    \                    /    \
     *          uncle(B) parent(R)           uncle(B) add(R)           grand(B) parent(R)          grand(R) parent(R)
     *                   /    \                         \                /         \               /         \
     *             add(R)   other(B)                 parent(R)        uncle(B)   other(B)       uncle(B)   other(B)
     *                                                    \
     *                                                   other(B)
     *******************************************************************************************************************
     */
    private void fixupAfterAdd(RBTNode<E> p) {
        RBTNode parent = (RBTNode) p.parent;
        RBTNode uncle = null;
        RBTNode grand = null;
        while (isRed(parent)) {
            grand = (RBTNode) parent.parent;
            uncle = getSibling(parent);
            //父节点和叔叔节点都是红色,则祖父节点为黑色,将父节点和叔叔节点变黑,祖父节点变红,从祖父节点开始往上重复
            if (isRed(uncle)) {
                setRed(grand);
                setBlack(parent);
                setBlack(uncle);
                p = grand;
                parent = (RBTNode) p.parent;
            } else {
                if (parent == grand.left) {
                    if (p == parent.left) {
                        setRed(grand);
                        setBlack(parent);
                        rotateRight(grand);
                    } else {
                        setRed(grand);
                        setBlack(p);
                        rotateLeft(parent);
                        rotateRight(grand);
                    }
                } else {
                    if (p == parent.right) {
                        setRed(grand);
                        setBlack(parent);
                        rotateLeft(grand);
                    } else {
                        setBlack(p);
                        setRed(grand);
                        rotateRight(parent);
                        rotateLeft(grand);
                    }
                }
                break;
            }
        }

        setBlack((RBTNode) root);
    }


    /**
     *******************************************************************************************************************
     * 1、删除节点为红色,不处理
     *******************************************************************************************************************
     * 2、删除节点为黑色,其替代子节点为红色,将替代子节点变黑色
     *******************************************************************************************************************
     *    替代子节点为黑色,则将替代节点当做双黑节点处理
     * 3、删除节点的兄弟节点为红色
     *------------------------------------------------------------------------------------------------------------------
     *  3.1、删除节点为父节点的左孩子,旋转着色后转为情况4
     *                                        rotateLeft(parent)            sibling.color = BLACK
     *                                                                      parent.color = RED
     *
     *            grand（B）                     grand(B)                    grand(B)
     *              /     \                     /     \                     /    \
     *       parent(B) uncle(B)          sibling(R)   uncle(B)       sibling(B)  uncle(B)
     *           /    \                       /    \                      /    \
     *        p(B)  sibling(R)           parent(B)  y(B)             parent(R)  y(B)
     *               /   \                  /   \                      /    \
     *            x(B)  y(B)             p(B)    x(B)                p(B)   x(B)
     *------------------------------------------------------------------------------------------------------------------
     *   3.2、 删除节点为父节点的右孩子,处理方式与3.1相反
     *******************************************************************************************************************
     * 4、删除节点的兄弟节点为黑色
     *------------------------------------------------------------------------------------------------------------------
     *  4.1、删除节点为父节点的左孩子
     *------------------------------------------------------------------------------------------------------------------
     *   4.1.1、 删除节点的兄弟节点的右孩子为红色
     *       redChild == sibling.right        rotateLeft(parent)            redChild.color = BLACK
     *                                                                      sibling.color = parent.color
     *                                                                      parent.color = BLACK
     *
     *            grand（B）                     grand(B)                    grand(B)
     *              /     \                     /     \                     /    \
     *       parent(R/B) uncle(B)        sibling(B)   uncle(B)       sibling(R/B)  uncle(B)
     *           /    \                       /    \                      /    \
     *        p(B) sibling(B)          parent(R/B) redChild(R)     parent(B)  redChild(B)
     *               /   \                  /   \                      /    \
     *          x(R/B) redChild(R)       p(B) x(R/B)                p(B)   x(R/B)
     *------------------------------------------------------------------------------------------------------------------
     *   4.1.2、 删除节点的兄弟节点的左孩子为红色
     *       redChild == sibling.left     rotateRight(sibling)   rotateLeft(parent)      redChild.color = parent.color
     *                                                                                   parent.color = BLACK
     *
     *             grand（B）                  grand(B)                     grand(B)                grand(B)
     *               /     \                   /    \                      /    \                 /      \
     *        parent(R/B) uncle(B)      parent(R/B)  uncle(B)      redChild(R)  uncle(B)   redChild(R/B) uncle(B)
     *            /    \                     /    \                     /    \                  /     \
     *         p(B)   sibling(B)          p(B)   redChild(R)      parent(R/B) sibling(B)    parent(B) sibling(B)
     *                /      \                     \                  /        \             /          \
     *         redChild(R) x(R/B)               sibling(B)          p(B)       x(R/B)     p(B)          x(R/B)
     *                                              \
     *                                             x(R/B)
     *------------------------------------------------------------------------------------------------------------------
     *   4.1.3、 删除节点的兄弟节点的左右孩子都为黑色
     *------------------------------------------------------------------------------------------------------------------
     *     4.1.3.1、父节点为红色，设置父节点为黑色，兄弟节点为红色，流程结束
     *                                         parent.color = BLACK
     *                                         sibling.color = RED
     *
     *             grand（B）                   grand(B)
     *               /     \                   /    \
     *        parent(R)  uncle(B)       parent(B)  uncle(B)
     *            /    \                     /   \
     *         p(B)   sibling(B)          p(B)  sibling(R)
     *                /   \                      /   \
     *              x(B)   y(B)                x(B)  x(B)
     *------------------------------------------------------------------------------------------------------------------
     *     4.1.3.2、父节点为黑色,则将父节点当做双黑节点处理,往上处理
     *------------------------------------------------------------------------------------------------------------------
     *  4.2、删除节点为父节点的右孩子,处理方式与左孩子相反
     *******************************************************************************************************************
     */
    private void fixupAfterRemove(RBTNode<E> remove, RBTNode<E> replace) {
        RBTNode parent = (RBTNode) remove.parent;
        if (parent == null || isRed(remove)) return;
        if (isRed(replace)) {
            setBlack(replace);
            return;
        }
        RBTNode p = remove;
        RBTNode sibling = getSibling(p);
        while (p != root) {
            if (isRed(sibling)) {
                if (sibling == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
                setRed(parent);
                setBlack(sibling);
                sibling = getSibling(p);
            } else if (sibling == parent.left) {
                if (isRed((RBTNode) sibling.left)) {
                    RBTNode leftChild = (RBTNode) sibling.left;
                    rotateRight(parent);
                    sibling.color = parent.color;
                    setBlack(parent);
                    setBlack(leftChild);
                    break;
                } else if (isRed((RBTNode) sibling.right)) {
                    RBTNode rightChild = (RBTNode) sibling.right;
                    rotateLeft(sibling);
                    rotateRight(parent);
                    rightChild.color = parent.color;
                    setBlack(parent);
                    break;
                }
            } else {
                if (isRed((RBTNode) sibling.left)) {
                    RBTNode leftChild = (RBTNode) sibling.left;
                    rotateRight(sibling);
                    rotateLeft(parent);
                    leftChild.color = parent.color;
                    setBlack(parent);
                    break;
                } else if (isRed((RBTNode) sibling.right)) {
                    RBTNode rightChild = (RBTNode) sibling.right;
                    rotateLeft(parent);
                    sibling.color = parent.color;
                    setBlack(parent);
                    setBlack(rightChild);
                    break;
                }
            }

            if (isRed(parent)) {
                setBlack(parent);
                setRed(sibling);
                break;
            } else {
                setRed(sibling);
                p = parent;
                sibling = getSibling(p);
                parent = (RBTNode) parent.parent;
            }
        }
        setBlack((RBTNode) root);
    }

    private RBTNode<E> getSibling(RBTNode<E> p) {
        if (p == null || p.parent == null) {
            return null;
        }
        if (p != p.parent.left) {
            return (RBTNode) p.parent.left;
        } else {
            return (RBTNode) p.parent.right;
        }
    }

    private boolean isRed(RBTNode<E> p) {
        return p != null && p.color == RED;
    }

    private void setRed(RBTNode<E> p) {
        if (p != null) p.color = RED;
    }

    private void setBlack(RBTNode<E> p) {
        if (p != null) p.color = BLACK;
    }

    static class RBTNode<E> extends BSTNode<E> {
        boolean color = RED;

        public RBTNode() {
            super();
        }

        public RBTNode(E e) {
            super(e);
        }

        public RBTNode(boolean color, E e) {
            super(e);
            this.color = color;
        }

        public RBTNode(RBTNode parent, E e) {
            super(parent, e);
        }
    }

}
