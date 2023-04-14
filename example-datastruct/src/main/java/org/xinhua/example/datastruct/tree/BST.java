package org.xinhua.example.datastruct.tree;

import org.xinhua.example.datastruct.collection.ArrayQueue;
import org.xinhua.example.datastruct.collection.ArrayStack;
import org.xinhua.example.datastruct.collection.Queue;
import org.xinhua.example.datastruct.collection.Stack;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/13 20:05
 * @Description: 二叉搜索树            Node节点不带parent指针
 * @Version: 1.0
 */
public class BST<E> {
    private final Comparator<? super E> comparator;
    private Node<E> root;
    private int size;

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
            root = new Node<>(e);
            size = 1;
            return;
        }

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
                cmp = ((Comparable) e).compareTo((Comparable) (p.e));
                if (cmp < 0) {
                    p = p.left;
                } else if (cmp > 0) {
                    p = p.right;
                } else {
                    return;
                }
            }
        }

        Node<E> node = new Node<>(e);

        if (cmp < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        size++;
    }

    /**
     * 删除指定结点
     */
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
    public Node<E> search(E e) {
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
    private Node<E> predecessor(Node<E> p) {
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
    private Node<E> successor(Node<E> p) {
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

    static class Node<E> {
        Node left, right;
        E e;

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
