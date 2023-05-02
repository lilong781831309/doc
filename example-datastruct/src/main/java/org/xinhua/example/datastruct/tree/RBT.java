package org.xinhua.example.datastruct.tree;

import org.xinhua.example.datastruct.collection.ArrayQueue;
import org.xinhua.example.datastruct.collection.ArrayStack;
import org.xinhua.example.datastruct.collection.Queue;
import org.xinhua.example.datastruct.collection.Stack;

import java.util.Comparator;
import java.util.function.BiConsumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/17 17:24
 * @Description: 红黑树
 * @Version: 1.0
 */
public class RBT<K, V> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private final Comparator<? super K> comparator;
    private int size;
    private int rotateLeftCount;
    private int rotateRightCount;
    private Entry<K, V> root;

    public RBT() {
        this(null);
    }

    public RBT(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    protected int cmp(K k1, K k2) {
        return comparator != null ? comparator.compare(k1, k2) : ((Comparable) k1).compareTo(k2);
    }

    public int size() {
        return size;
    }

    public int rotateLeftCount() {
        return rotateLeftCount;
    }

    public int rotateRightCount() {
        return rotateRightCount;
    }

    public V insert(K k, V v) {
        if (k == null) {
            return null;
        }
        if (root == null) {
            root = new Entry<>(null, k, v);
            root.color = BLACK;
            size = 1;
            return null;
        }
        Entry<K, V> e = root;
        Entry<K, V> p = null;
        int cmp = 0;
        while (e != null) {
            p = e;
            if ((cmp = cmp(k, e.k)) < 0) {
                e = e.left;
            } else if (cmp > 0) {
                e = e.right;
            } else {
                V value = e.v;
                e.v = v;
                return value;
            }
        }

        Entry entry = new Entry<>(p, k, v);
        if (cmp < 0) {
            p.left = entry;
        } else {
            p.right = entry;
        }
        fixupAfterInsert(entry);
        size++;
        return null;
    }

    public V delete(K k) {
        if (root == null || k == null) {
            return null;
        }
        Entry<K, V> e = root;
        int cmp = 0;

        while (e != null) {
            if ((cmp = cmp(k, e.k)) < 0) {
                e = e.left;
            } else if (cmp > 0) {
                e = e.right;
            } else {
                break;
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
            setBlack(root);
        } else if (replace == null) {
            fixupAfterDelete(e);
            if (e == e.parent.left) {
                e.parent.left = null;
            } else {
                e.parent.right = null;
            }
        } else {
            if (e == e.parent.left) {
                e.parent.left = replace;
            } else {
                e.parent.right = replace;
            }
            replace.parent = e.parent;
            fixupAfterDelete(replace);
        }
        size--;

        return value;
    }

    private void fixupAfterInsert(Entry e) {
        Entry parent = null;
        Entry uncle = null;
        Entry grand = null;
        while (isRed(e.parent)) {
            parent = e.parent;
            grand = parent.parent;
            uncle = parent == grand.left ? grand.right : grand.left;
            if (isRed(uncle)) {
                setBlack(parent);
                setBlack(uncle);
                setRed(grand);
                e = grand;
            } else if (parent == grand.left) {
                if (e == parent.left) {
                    setBlack(parent);
                    setRed(grand);
                    rotateRight(grand);
                } else {
                    setBlack(e);
                    setRed(grand);
                    rotateLeft(parent);
                    rotateRight(grand);
                }
                break;
            } else {
                if (e == parent.left) {
                    setBlack(e);
                    setRed(grand);
                    rotateRight(parent);
                    rotateLeft(grand);
                } else {
                    setBlack(parent);
                    setRed(grand);
                    rotateLeft(grand);
                }
                break;
            }
        }
        setBlack(root);
    }

    private void fixupAfterDelete(Entry e) {
        if (isRed(e)) {
            setBlack(e);
            return;
        }

        Entry parent = e.parent;
        Entry sibling = null;

        while (parent != null) {
            if (e == parent.left) {
                sibling = parent.right;
                if (isRed(sibling)) {
                    setRed(parent);
                    setBlack(sibling);
                    rotateLeft(parent);
                    continue;
                } else if (isRed(sibling.right)) {
                    sibling.color = parent.color;
                    setBlack(parent);
                    setBlack(sibling.right);
                    rotateLeft(parent);
                    break;
                } else if (isRed(sibling.left)) {
                    sibling.left.color = parent.color;
                    setBlack(parent);
                    rotateRight(sibling);
                    rotateLeft(parent);
                    break;
                }
            } else {
                sibling = parent.left;
                if (isRed(sibling)) {
                    setRed(parent);
                    setBlack(sibling);
                    rotateRight(parent);
                    continue;
                } else if (isRed(sibling.left)) {
                    sibling.color = parent.color;
                    setBlack(parent);
                    setBlack(sibling.left);
                    rotateRight(parent);
                    break;
                } else if (isRed(sibling.right)) {
                    sibling.right.color = parent.color;
                    setBlack(parent);
                    rotateLeft(sibling);
                    rotateRight(parent);
                    break;
                }
            }

            if (isRed(parent)) {
                setRed(sibling);
                setBlack(parent);
                break;
            } else {
                setRed(sibling);
                e = e.parent;
                parent = parent.parent;
            }
        }
        setBlack(root);
    }

    private boolean isRed(Entry e) {
        return e != null && e.color == RED;
    }

    private void setRed(Entry e) {
        if (e != null) {
            e.color = RED;
        }
    }

    private void setBlack(Entry e) {
        if (e != null) {
            e.color = BLACK;
        }
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
        boolean color = RED;

        public Entry() {
        }

        public Entry(Entry parent, K k, V v) {
            this.parent = parent;
            this.k = k;
            this.v = v;
        }
    }

}
