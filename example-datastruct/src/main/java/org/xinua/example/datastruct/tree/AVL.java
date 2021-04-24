package org.xinua.example.datastruct.tree;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * 平衡二叉树
 */
public class AVL<E> {

    private final Comparator<? super E> comparator;
    private static final int LH = 1;    //左高
    private static final int EH = 0;    //等高
    private static final int RH = -1;   //右高
    private Node<E> root;
    private int size;

    public AVL() {
        comparator = null;
    }

    public AVL(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    /**
     * 插入结点
     */
    public void add(E e) {
        if (root == null) {
            root = new Node(null, e);
            size = 1;
        } else {
            Node<E> p = root;
            Node<E> parent = null;
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

            Node node = new Node(parent, e);

            if (cmp < 0) {
                parent.left = node;
            } else if (cmp > 0) {
                parent.right = node;
            }

            p = node;
            while (parent != null) {
                if (p == parent.left) {
                    parent.balance++;
                } else {
                    parent.balance--;
                }
                if (parent.balance == 0) {
                    break;
                }
                if (parent.balance == 2) {
                    //左子树高于右子树; 如果R的左子树的根节点的BF为1时，做右旋；如果R的左子树的根节点的BF为-1时，先左旋然后再右旋
                    balanceLeft(parent);
                    break;
                } else if (parent.balance == -2) {
                    //右子树高于左子树：如果R的右子树的根节点的BF为1时，先右旋后左旋;如果R的右子树的根节点的BF为-1时，做左旋
                    balanceRight(parent);
                    break;
                }
                p = parent;
                parent = parent.parent;
            }

            size++;
        }
    }

    /**
     * 删除指定结点
     */
    public void remove(E e) {
        Node<E> node = search(e);
        if (node == null) {
            return;
        }
        if (node.left != null && node.right != null) {
            //后继节点替换
            //Node<E> successor = successor(node);
            //node.e = successor.e;
            //node = successor;

            //前驱节点替换
            Node<E> predecessor = predecessor(node);
            node.e = predecessor.e;
            node = predecessor;
        }
        if (node.left == null && node.right == null) {
            balanceAfterDelete(node);
            transplant(node, null);
        } else if (node.left == null) {
            transplant(node, node.right);
            balanceAfterDelete(node.right);
        } else if (node.right == null) {
            transplant(node, node.left);
            balanceAfterDelete(node.left);
        } else {
            //经过successor替换后,不存在这种情况
        }
        size--;
    }

    /**
     * 删除之后调整树平衡
     */
    private void balanceAfterDelete(Node<E> u) {
        boolean heightLower = true;
        Node<E> p = u.parent;
        Node<E> pp = null;
        while (p != null && heightLower) {
            pp = p.parent;
            if (u == p.left) {
                p.balance--;
            } else {
                p.balance++;
            }
            if (p.balance == -1 || p.balance == 1) {
                break;
            }
            if (p.balance == 2) {
                heightLower = balanceLeft(p);
            } else if (p.balance == -2) {
                heightLower = balanceRight(p);
            }
            u = p;
            p = pp;
        }
    }

    /**
     * 平衡左子树
     */
    private boolean balanceLeft(Node p) {
        boolean heightLower = true;
        Node<E> l = p.left;
        switch (l.balance) {
            case LH:
                p.balance = l.balance = EH;
                rotateRight(p);
                break;
            case RH:
                switch (l.right.balance) {
                    case LH:
                        p.balance = RH;
                        l.balance = EH;
                        break;
                    case RH:
                        p.balance = EH;
                        l.balance = LH;
                        break;
                    case EH:
                        p.balance = l.balance = EH;
                        break;
                }
                l.right.balance = EH;
                rotateLeft(l);
                rotateRight(p);
                break;
            case EH:
                p.balance = LH;
                l.balance = RH;
                rotateRight(p);
                heightLower = false;
                break;
        }
        return heightLower;
    }

    /**
     * 平衡右子树
     */
    private boolean balanceRight(Node p) {
        boolean heightLower = true;
        Node<E> r = p.right;
        switch (r.balance) {
            case LH:
                switch (r.left.balance) {
                    case LH:
                        p.balance = EH;
                        r.balance = RH;
                        break;
                    case RH:
                        p.balance = LH;
                        r.balance = EH;
                        break;
                    case EH:
                        p.balance = r.balance = EH;
                        break;
                }
                r.left.balance = EH;
                rotateRight(r);
                rotateLeft(p);
                break;
            case RH:
                p.balance = r.balance = EH;
                rotateLeft(p);
                break;
            case EH:
                p.balance = RH;
                r.balance = LH;
                rotateLeft(p);
                heightLower = false;
                break;
        }
        return heightLower;
    }

    /**
     * 左旋操作（右孩子的右子树插入节点）
     */
    private void rotateLeft(Node p) {
        Node r = p.right;
        transplant(p, r);

        p.right = r.left;
        if (r.left != null) {
            r.left.parent = p;
        }

        p.parent = r;
        r.left = p;
    }

    /**
     * 右旋操作（左孩子的左子树插入节点）
     */
    private void rotateRight(Node p) {
        Node l = p.left;
        transplant(p, l);

        p.left = l.right;
        if (l.right != null) {
            l.right.parent = p;
        }

        p.parent = l;
        l.right = p;
    }

    /**
     * 前驱节点
     */
    private Node<E> predecessor(Node p) {
        if (root == null || p == null) {
            return null;
        }
        if (p.left != null) {
            return max(p.left);
        }
        Node<E> x = p;
        Node<E> y = p.parent;
        while (y != null && x == y.left) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    /**
     * 后继节点
     */
    private Node<E> successor(Node p) {
        if (root == null || p == null) {
            return null;
        }
        if (p.right != null) {
            return min(p.right);
        }
        Node<E> x = p;
        Node<E> y = p.parent;
        while (y != null && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    /**
     * 最小值
     */
    public E min() {
        if (root != null) {
            return min(root).e;
        }
        return null;
    }

    /**
     * 最大值
     */
    public E max() {
        if (root != null) {
            return max(root).e;
        }
        return null;
    }

    /**
     * 最小节点
     */
    private Node<E> min(Node<E> p) {
        if (root != null && p != null) {
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }
        return null;
    }


    /**
     * 最大节点
     */
    private Node<E> max(Node<E> p) {
        if (root != null && p != null) {
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }
        return null;
    }


    /**
     * 元素个数
     */
    public int size() {
        return size;
    }

    /**
     * 是否为空
     */
    public boolean empty() {
        return size == 0;
    }

    /**
     * 前序遍历
     */
    public void preOrder(Consumer<? super E> action) {
        if (root != null) {
            Stack<Node<E>> stack = new Stack<>();
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
            Stack<Node<E>> stack = new Stack<>();
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
            Stack<Node<E>> stack = new Stack<>();
            Node<E> p = root;
            Node<E> visit = null;

            while (!stack.empty() || p != null) {
                while (p != null) {
                    stack.push(p);
                    p = p.left;
                }
                p = stack.pop();
                if (p.right == null || p.right == visit) {
                    action.accept(p.e);
                    visit = p;
                    p = null;
                } else {
                    stack.push(p);
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
            Queue<Node<E>> queue = new LinkedList<>();
            queue.offer(root);
            Node<E> p = null;
            while (!queue.isEmpty()) {
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

    /**
     * 搜索
     */
    private Node<E> search(E e) {
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
                    return p;
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
                    return p;
                }
            }
        }

        return null;
    }

    /**
     * 用一棵以v为根的子树来替换一棵以u为根的子树时,结点u的双亲就变为结点v的双亲,并且最后v成为u的双亲的相应孩子
     */
    private void transplant(Node u, Node v) {
        if (u == null || u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else if (u == u.parent.right) {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    private static class Node<E> {
        Node parent, left, right;
        E e;
        int balance = EH;    //平衡因子，只能为1或0或-1

        public Node(Node parent, E e) {
            this.parent = parent;
            this.e = e;
        }
    }
}
