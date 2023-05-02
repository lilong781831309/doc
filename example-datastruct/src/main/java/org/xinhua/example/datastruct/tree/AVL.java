package org.xinhua.example.datastruct.tree;

import org.xinhua.example.datastruct.collection.ArrayQueue;
import org.xinhua.example.datastruct.collection.ArrayStack;
import org.xinhua.example.datastruct.collection.Queue;
import org.xinhua.example.datastruct.collection.Stack;

import java.util.Comparator;
import java.util.function.BiConsumer;

public class AVL<K, V> {

    private final Comparator<? super K> comparator;
    private int size;
    private int rotateLeftCount;
    private int rotateRightCount;
    private Entry<K, V> root;

    public int size() {
        return size;
    }

    public int rotateLeftCount() {
        return rotateLeftCount;
    }

    public int rotateRightCount() {
        return rotateRightCount;
    }

    public AVL() {
        this(null);
    }

    public AVL(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    public V insert(K k, V v) {
        if (k == null) {
            return null;
        }
        if (root == null) {
            root = new Entry<>(k, v);
            size = 1;
            return null;
        }
        Entry<K, V> e = root;
        Entry<K, V> p = null;
        int cmp = 0;

        if (comparator != null) {
            while (e != null) {
                p = e;
                cmp = comparator.compare(k, e.k);
                if (cmp < 0) {
                    e = e.left;
                } else if (cmp > 0) {
                    e = e.right;
                } else {
                    V value = e.v;
                    e.v = v;
                    return value;
                }
            }
        } else {
            while (e != null) {
                p = e;
                cmp = ((Comparable) k).compareTo(((Comparable) e.k));
                if (cmp < 0) {
                    e = e.left;
                } else if (cmp > 0) {
                    e = e.right;
                } else {
                    V value = e.v;
                    e.v = v;
                    return value;
                }
            }
        }

        Entry<K, V> entry = new Entry<>(p, k, v);
        if (cmp < 0) {
            p.left = entry;
        } else {
            p.right = entry;
        }
        rebalance(p);
        size++;
        return null;
    }

    public V delete(K k) {
        if (root == null || k == null) {
            return null;
        }
        Entry<K, V> e = root;
        int cmp = 0;

        if (comparator != null) {
            while (e != null) {
                cmp = comparator.compare(k, e.k);
                if (cmp < 0) {
                    e = e.left;
                } else if (cmp > 0) {
                    e = e.right;
                } else {
                    break;
                }
            }
        } else {
            while (e != null) {
                cmp = ((Comparable) k).compareTo(((Comparable) e.k));
                if (cmp < 0) {
                    e = e.left;
                } else if (cmp > 0) {
                    e = e.right;
                } else {
                    break;
                }
            }
        }

        if (e == null) {
            return null;
        }

        if (e.left != null && e.right != null) {
            Entry<K, V> predecessor = predecessor(e);
            e.k = predecessor.k;
            e.v = predecessor.v;
            e = predecessor;
        }

        V value = e.v;
        Entry replace = null;

        if (e.left != null) {
            replace = e.left;
        } else if (e.right != null) {
            replace = e.right;
        }
        if (e == root) {
            root = replace;
        } else if (e == e.parent.left) {
            e.parent.left = replace;
        } else {
            e.parent.right = replace;
        }
        if (replace != null) {
            replace.parent = e.parent;
        }
        rebalance(e.parent);
        size--;

        return value;
    }

    private void rebalance(Entry e) {
        while (e != null) {
            int oldlHight = e.hight;
            int bf = bf(e);
            if (bf == 2) {
                if (bf(e.left) < 0) {
                    rotateLeft(e.left);
                }
                e = rotateRight(e);
            } else if (bf == -2) {
                if (bf(e.right) > 0) {
                    rotateRight(e.right);
                }
                e = rotateLeft(e);
            }
            updateHight(e);
            if (e.hight == oldlHight) {
                break;
            }
            e = e.parent;
        }
    }

    private int hight(Entry e) {
        return e == null ? 0 : e.hight;
    }

    private void updateHight(Entry e) {
        int lh = hight(e.left);
        int rh = hight(e.right);
        e.hight = lh > rh ? lh + 1 : rh + 1;
    }

    private int bf(Entry e) {
        return hight(e.left) - hight(e.right);
    }

    private Entry predecessor(Entry e) {
        if (root == null || e == null) {
            return null;
        }
        if (e.left != null) {
            e = e.left;
            while (e.right != null) {
                e = e.right;
            }
            return e;
        }
        Entry x = e, y = e.parent;
        while (y != null && x == y.left) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    private Entry rotateLeft(Entry e) {
        Entry r = e.right;
        if (e.parent == null) {
            root = r;
        } else if (e == e.parent.left) {
            e.parent.left = r;
        } else {
            e.parent.right = r;
        }
        r.parent = e.parent;
        e.right = r.left;
        if (r.left != null) {
            r.left.parent = e;
        }
        r.left = e;
        e.parent = r;
        updateHight(e);
        updateHight(r);
        rotateLeftCount++;
        return r;
    }

    private Entry rotateRight(Entry e) {
        Entry l = e.left;
        if (e.parent == null) {
            root = l;
        } else if (e == e.parent.left) {
            e.parent.left = l;
        } else {
            e.parent.right = l;
        }
        l.parent = e.parent;
        e.left = l.right;
        if (l.right != null) {
            l.right.parent = e;
        }
        l.right = e;
        e.parent = l;
        updateHight(e);
        updateHight(l);
        rotateRightCount++;
        return l;
    }

    public void preOrder(BiConsumer<? super K, ? super V> action) {
        if (root != null) {
            Stack<Entry<K, V>> stack = new ArrayStack<>();
            stack.push(root);
            Entry<K, V> e;
            while (!stack.empty()) {
                e = stack.pop();
                action.accept(e.k, e.v);
                if (e.right != null) {
                    stack.push(e.right);
                }
                if (e.left != null) {
                    stack.push(e.left);
                }
            }
        }
    }

    public void inOrder(BiConsumer<? super K, ? super V> action) {
        if (root != null) {
            Stack<Entry<K, V>> stack = new ArrayStack<>();
            Entry<K, V> e = root;
            while (!stack.empty() || e != null) {
                while (e != null) {
                    stack.push(e);
                    e = e.left;
                }
                e = stack.pop();
                action.accept(e.k, e.v);
                e = e.right;
            }
        }
    }

    public void postOrder(BiConsumer<? super K, ? super V> action) {
        if (root != null) {
            Stack<Entry<K, V>> stack = new ArrayStack<>();
            Entry<K, V> e = root;
            Entry<K, V> visit = null;
            while (!stack.empty() || e != null) {
                while (e != null) {
                    stack.push(e);
                    e = e.left;
                }
                e = stack.peek();
                if (e.right == null || e.right == visit) {
                    visit = stack.pop();
                    action.accept(e.k, e.v);
                    e = null;
                } else {
                    e = e.right;
                }
            }
        }
    }

    public void levelOrder(BiConsumer<? super K, ? super V> action) {
        if (root != null) {
            Queue<Entry<K, V>> queue = new ArrayQueue<>();
            queue.offer(root);
            Entry<K, V> e = null;
            while (!queue.empty()) {
                e = queue.poll();
                action.accept(e.k, e.v);
                if (e.left != null) {
                    queue.offer(e.left);
                }
                if (e.right != null) {
                    queue.offer(e.right);
                }
            }
        }
    }

    static class Entry<K, V> {
        Entry<K, V> parent, left, right;
        K k;
        V v;
        int hight = 1;

        public Entry() {
        }

        public Entry(K k, V v) {
            this.k = k;
            this.v = v;
        }

        public Entry(Entry parent, K k, V v) {
            this(k, v);
            this.parent = parent;
        }
    }
}
