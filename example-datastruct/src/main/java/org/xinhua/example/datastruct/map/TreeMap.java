package org.xinhua.example.datastruct.map;

import org.xinhua.example.datastruct.collection.ArrayStack;
import org.xinhua.example.datastruct.collection.Stack;

import java.util.Comparator;
import java.util.function.BiConsumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/27 0:28
 * @Description: 红黑树map
 * @Version: 1.0
 */
public class TreeMap<K, V> implements Map<K, V> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private final Comparator<? super K> comparator;
    private int size;
    private TreeNode root;
    private KeySet keySet;
    private Values values;
    private EntrySet entrySet;

    public TreeMap() {
        this(null);
    }

    public TreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(K key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        TreeNode first = min(root);
        if (value == null) {
            while (first != null) {
                if (first.value == null) {
                    return true;
                }
                first = successor(first);
            }
        } else {
            while (first != null) {
                if (first.value != null && value.equals(first.value)) {
                    return true;
                }
                first = successor(first);
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        TreeNode entry = getEntry(key);
        return entry == null ? null : entry.value;
    }

    @Override
    public V put(K key, V value) {
        return putVal(key, value, false);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return putVal(key, value, true);
    }

    @Override
    public void putAll(Map<K, V> map) {
        Set<Entry<K, V>> entrySet = map.entrySet();
        Iterator<Entry<K, V>> it = entrySet.iterator();
        while (it.hasNext()) {
            Entry<K, V> entry = it.next();
            put(entry.getKey(), entry.getValue());
        }
    }

    private V putVal(K key, V value, boolean putIfAbsent) {
        if (key == null) {
            return null;
        }
        if (root == null) {
            root = new TreeNode(null, key, value);
            root.color = BLACK;
            size = 1;
            return null;
        }
        TreeNode e = root;
        TreeNode p = null;
        int cmp = 0;
        if (comparator != null) {
            while (e != null) {
                p = e;
                cmp = comparator.compare(key, e.key);
                if (cmp < 0) {
                    e = e.left;
                } else if (cmp > 0) {
                    e = e.right;
                } else if (putIfAbsent) {
                    return e.setValue(value);
                } else {
                    return e.value;
                }
            }
        } else {
            while (e != null) {
                p = e;
                cmp = ((Comparable) key).compareTo(((Comparable) e.key));
                if (cmp < 0) {
                    e = e.left;
                } else if (cmp > 0) {
                    e = e.right;
                } else if (putIfAbsent) {
                    return e.setValue(value);
                } else {
                    return e.value;
                }
            }
        }

        TreeNode entry = new TreeNode(p, key, value);
        if (cmp < 0) {
            p.left = entry;
        } else {
            p.right = entry;
        }
        fixupAfterInsert(entry);
        size++;
        return null;
    }

    @Override
    public V remove(K key) {
        TreeNode e = getEntry(key);
        if (e == null) {
            return null;
        }
        V value = e.value;
        deleteEntry(e);
        return value;
    }

    private TreeNode getEntry(K key) {
        if (root == null || key == null) {
            return null;
        }

        TreeNode e = root;
        int cmp;

        if (comparator != null) {
            while (e != null) {
                cmp = comparator.compare(key, e.key);
                if (cmp < 0) {
                    e = e.left;
                } else if (cmp > 0) {
                    e = e.right;
                } else {
                    return e;
                }
            }
        } else {
            while (e != null) {
                cmp = ((Comparable) key).compareTo(((Comparable) e.key));
                if (cmp < 0) {
                    e = e.left;
                } else if (cmp > 0) {
                    e = e.right;
                } else {
                    return e;
                }
            }
        }
        return null;
    }

    private void deleteEntry(TreeNode e) {
        if (e.left != null && e.right != null) {
            TreeNode predecessor = predecessor(e);
            e.key = predecessor.key;
            e.value = predecessor.value;
            e = predecessor;
        }

        TreeNode replace = null;

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
    }

    @Override
    public V replace(K key, V value) {
        TreeNode e = getEntry(key);
        if (e == null) {
            return null;
        }
        return e.setValue(value);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        if (root != null) {
            Stack<TreeNode> stack = new ArrayStack<>();
            TreeNode e = root;
            while (!stack.empty() || e != null) {
                while (e != null) {
                    stack.push(e);
                    e = e.left;
                }
                e = stack.pop();
                action.accept(e.key, e.value);
                e = e.right;
            }
        }
    }

    private void fixupAfterInsert(TreeNode e) {
        TreeNode parent = null;
        TreeNode uncle = null;
        TreeNode grand = null;
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

    private void fixupAfterDelete(TreeNode e) {
        if (isRed(e)) {
            setBlack(e);
            return;
        }

        TreeNode parent = e.parent;
        TreeNode sibling = null;

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

    private boolean isRed(TreeNode e) {
        return e != null && e.color == RED;
    }

    private void setRed(TreeNode e) {
        if (e != null) {
            e.color = RED;
        }
    }

    private void setBlack(TreeNode e) {
        if (e != null) {
            e.color = BLACK;
        }
    }

    private TreeNode min(TreeNode e) {
        if (e == null) {
            return null;
        }
        while (e.left != null) {
            e = e.left;
        }
        return e;
    }

    private TreeNode max(TreeNode e) {
        if (e == null) {
            return null;
        }
        while (e.right != null) {
            e = e.right;
        }
        return e;
    }

    private TreeNode predecessor(TreeNode e) {
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
        TreeNode x = e, y = e.parent;
        while (y != null && x == y.left) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    private TreeNode successor(TreeNode e) {
        if (root == null || e == null) {
            return null;
        }
        if (e.right != null) {
            e = e.right;
            while (e.left != null) {
                e = e.left;
            }
            return e;
        }
        TreeNode x = e, y = e.parent;
        while (y != null && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    private TreeNode rotateLeft(TreeNode e) {
        TreeNode r = e.right;
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
        return r;
    }

    private TreeNode rotateRight(TreeNode e) {
        TreeNode l = e.left;
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
        return l;
    }

    @Override
    public Set<K> keySet() {
        if (keySet == null) {
            keySet = new KeySet();
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new Values();
        }
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet();
        }
        return entrySet;
    }

    class KeySet extends AbstractSet<K> {

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(K k) {
            return getEntry(k) != null;
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public boolean remove(K k) {
            TreeNode entry = getEntry(k);
            if (entry == null) {
                return false;
            }
            int oldSize = size;
            deleteEntry(entry);
            return oldSize == size;
        }
    }

    class KeyIterator extends TreeIterator implements Iterator<K> {
        @Override
        public K next() {
            return nextEntry().key;
        }
    }

    class Values extends AbstractCollection<V> {

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(V v) {
            return containsValue(v);
        }

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public boolean remove(V v) {
            return false;
        }
    }

    class ValueIterator extends TreeIterator implements Iterator<V> {
        @Override
        public V next() {
            return nextEntry().value;
        }
    }

    class EntrySet extends AbstractSet<Entry<K, V>> {

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(Entry<K, V> entry) {
            if (entry == null) {
                return false;
            }
            TreeNode treeNode = getEntry(entry.getKey());
            if (treeNode == null) {
                return false;
            }
            V value = entry.getValue();
            V v = treeNode.getValue();
            return (v == null && value == null) || (v != null && value != null && v.equals(value));
        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public boolean remove(Entry<K, V> entry) {
            if (entry == null) {
                return false;
            }
            int oldSize = size;
            deleteEntry((TreeNode) entry);
            return oldSize == size;
        }
    }

    class EntryIterator extends TreeIterator implements Iterator<Entry<K, V>> {
        @Override
        public Entry<K, V> next() {
            return nextEntry();
        }
    }

    abstract class TreeIterator {

        TreeNode cur;
        TreeNode next;

        public TreeIterator() {
            this.next = min(root);
        }

        public boolean hasNext() {
            return next != null;
        }

        public TreeNode nextEntry() {
            cur = next;
            next = successor(cur);
            return cur;
        }

        public void remove() {
            deleteEntry(cur);
        }
    }

    class TreeNode implements Entry<K, V> {
        TreeNode parent, left, right;
        K key;
        V value;
        boolean color = RED;

        public TreeNode() {
        }

        public TreeNode(TreeNode parent, K key, V value) {
            this.parent = parent;
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}
