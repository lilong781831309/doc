package org.xinhua.example.datastruct.tree.single;

import org.xinhua.example.datastruct.collection.ArrayList;
import org.xinhua.example.datastruct.collection.List;

import java.util.Comparator;

/**
 * @Author: lilong
 * @createDate: 2023/4/20 0:29
 * @Description: B树
 * @Version: 1.0
 */
public class BTree<K, V> {

    private static final int MINIMUM_ORDER = 3;
    private static final int MAXIMUM_ORDER = 1024;
    private static final int DEFAULT_ORDER = 256;
    private final Comparator<? super K> comparator;
    private final int order;
    private int size;
    private int splitCount;
    private int mergeCount;
    private Node<K, V> root;

    public BTree() {
        this(DEFAULT_ORDER, null);
    }

    public BTree(int order) {
        this(order, null);
    }

    public BTree(Comparator<? super K> comparator) {
        this(DEFAULT_ORDER, comparator);
    }

    public BTree(int order, Comparator<? super K> comparator) {
        if (order < MINIMUM_ORDER) order = MINIMUM_ORDER;
        if (order > MAXIMUM_ORDER) order = MAXIMUM_ORDER;
        this.order = order;
        this.comparator = comparator;
    }

    public int size() {
        return size;
    }

    public int splitCount() {
        return splitCount;
    }

    public int mergeCount() {
        return mergeCount;
    }

    public V insert(K k, V v) {
        if (k == null) {
            return null;
        }
        if (root == null) {
            root = new Node<>(k, v);
            size = 1;
            return null;
        }
        return insert(root, k, v);
    }

    private V insert(Node<K, V> node, K k, V v) {
        int index = binarySearch(node, k);
        if (index >= 0) {
            return node.setV(index, v);
        }

        V value = null;
        index = -index - 1;

        if (node.isLeaf) {
            node.addEntry(index, k, v);
            size++;
        } else {
            value = insert(node.getChild(index), k, v);
        }
        if (node.keySize() == order) {
            split(node);
        }
        return value;
    }

    private void split(Node<K, V> node) {
        Node<K, V> parent = node.parent;
        Node<K, V> left = new Node<>();
        Node<K, V> right = new Node<>();

        int keySize = node.keySize();
        int childSize = node.childSize();
        int k = (keySize - 1) >> 1;

        for (int i = 0; i < k; i++) {
            left.addEntry(node.getEntry(i));
        }
        for (int i = k + 1; i < keySize; i++) {
            right.addEntry(node.getEntry(i));
        }
        //if (!node.isLeaf)
        for (int i = 0; i <= k && i < childSize; i++) {
            left.addChild(node.getChild(i));
        }
        //if (!node.isLeaf)
        for (int i = k + 1; i < childSize; i++) {
            right.addChild(node.getChild(i));
        }

        if (parent == null) {
            parent = new Node<>();
            parent.addEntry(node.getEntry(k));
            parent.addChild(left);
            parent.addChild(right);
            root = parent;
        } else {
            int index = parent.indexOfChild(node);
            parent.addEntry(index, node.getEntry(k));
            parent.setChild(index, left);
            parent.addChild(index + 1, right);
        }
        parent.isLeaf = false;
        left.isLeaf = right.isLeaf = node.isLeaf;
        splitCount++;
    }

    public V delete(K k) {
        if (root == null || k == null) {
            return null;
        }

        Node<K, V> node = root;
        int index = binarySearch(node, k);

        while (index < 0 && !node.isLeaf) {
            node = node.getChild(-index - 1);
            index = binarySearch(node, k);
        }

        if (index < 0) {
            return null;
        }

        V value = node.getV(index);

        //删除前驱
        if (!node.isLeaf) {
            Node<K, V> predecessor = node.getChild(index);
            int pos = predecessor.keySize();
            while (!predecessor.isLeaf) {
                predecessor = predecessor.getChild(pos);
                pos = predecessor.keySize();
            }
            pos--;

            node.setEntry(index, predecessor.getEntry(pos));

            node = predecessor;
            index = pos;
        }
        delete(node, index);
        return value;
    }

    private void delete(Node<K, V> node, int index) {
        if (node == root) {
            if (root.keySize() == 1) {
                root = null;
            } else {
                root.removeEntry(index);
            }
        } else {
            node.removeEntry(index);
            int k = order / 2 + order % 2 - 1;
            while (node != root && node.keySize() < k) {
                Node<K, V> parent = node.parent;
                int i = parent.indexOfChild(node);
                if (i > 0 && parent.getChild(i - 1).keySize() > k) {
                    Node<K, V> leftSibling = parent.getChild(i - 1);
                    Entry<K, V> leftEntry = leftSibling.removeEntry(leftSibling.keySize() - 1);
                    Entry<K, V> parentEntry = parent.setEntry(i - 1, leftEntry);
                    node.addEntry(0, parentEntry);
                    if (!leftSibling.isLeaf) {
                        node.addChild(0, leftSibling.removeChild(leftSibling.childSize() - 1));
                    }
                    break;
                } else if (i < parent.keySize() && parent.getChild(i + 1).keySize() > k) {
                    Node<K, V> rightSibling = parent.getChild(i + 1);
                    Entry<K, V> rightEntry = rightSibling.removeEntry(0);
                    Entry<K, V> parentEntry = parent.setEntry(i, rightEntry);
                    node.addEntry(parentEntry);
                    if (!rightSibling.isLeaf) {
                        node.addChild(rightSibling.removeChild(0));
                    }
                    break;
                } else {
                    if (i > 0) {
                        node = merge(parent.getChild(i - 1), node, i - 1);
                    } else {
                        node = merge(node, parent.getChild(i + 1), i);
                    }
                    if (node.parent == root && node.parent.keySize() == 0) {
                        root = node;
                        node.parent = null;
                        break;
                    }
                }
                node = node.parent;
            }
        }
        size--;
    }

    private Node<K, V> merge(Node<K, V> left, Node<K, V> right, int parentIndex) {
        Node<K, V> node = new Node<K, V>();
        Node<K, V> parent = left.parent;

        parent.removeChild(parentIndex);
        parent.setChild(parentIndex, node);

        int size = left.keySize();
        for (int i = 0; i < size; i++) {
            node.addEntry(left.getEntry(i));
        }

        node.addEntry(parent.removeEntry(parentIndex));

        size = right.keySize();
        for (int i = 0; i < size; i++) {
            node.addEntry(right.getEntry(i));
        }

        size = left.childSize();
        for (int i = 0; i < size; i++) {
            node.addChild(left.getChild(i));
        }

        size = right.childSize();
        for (int i = 0; i < size; i++) {
            node.addChild(right.getChild(i));
        }

        node.isLeaf = left.isLeaf;
        mergeCount++;
        return node;
    }

    private int binarySearch(Node<K, V> node, K k) {
        int cmp = 0, low = 0, mid = 0, high = node.keySize() - 1;
        if (comparator != null) {
            while (low <= high) {
                mid = (low + high) >>> 1;
                cmp = comparator.compare(k, node.getK(mid));
                if (cmp < 0) {
                    high = mid - 1;
                } else if (cmp > 0) {
                    low = mid + 1;
                } else {
                    return mid;
                }
            }
        } else {
            while (low <= high) {
                mid = (low + high) >>> 1;
                cmp = ((Comparable) k).compareTo((Comparable) node.getK(mid));
                if (cmp < 0) {
                    high = mid - 1;
                } else if (cmp > 0) {
                    low = mid + 1;
                } else {
                    return mid;
                }
            }
        }
        return -(low + 1);
    }

    static class Node<K, V> {
        List<Entry<K, V>> entries;
        List<Node<K, V>> childs;
        Node<K, V> parent;
        boolean isLeaf = true;

        public Node() {
            entries = new ArrayList<>();
            childs = new ArrayList<>();
        }

        public Node(K k, V v) {
            this();
            entries.add(new Entry(k, v));
        }

        public int keySize() {
            return entries.size();
        }

        public int childSize() {
            return childs.size();
        }

        public Entry<K, V> getEntry(int index) {
            return entries.get(index);
        }

        public Entry<K, V> setEntry(int index, Entry<K, V> entry) {
            return entries.set(index, entry);
        }

        public void addEntry(Entry<K, V> entry) {
            entries.add(entry);
        }

        public void addEntry(int index, Entry<K, V> entry) {
            entries.add(index, entry);
        }

        public void addEntry(int index, K k, V v) {
            addEntry(index, new Entry<>(k, v));
        }

        public Entry<K, V> removeEntry(int index) {
            return entries.removeAt(index);
        }

        public K getK(int index) {
            return entries.get(index).k;
        }

        public V getV(int index) {
            return entries.get(index).v;
        }

        public V setV(int index, V v) {
            Entry<K, V> entry = entries.get(index);
            V oldV = entry.v;
            entry.v = v;
            return oldV;
        }

        public Node<K, V> getChild(int index) {
            return childs.get(index);
        }

        public Node<K, V> setChild(int index, Node<K, V> node) {
            node.parent = this;
            return childs.set(index, node);
        }

        public void addChild(Node<K, V> node) {
            node.parent = this;
            childs.add(node);
        }

        public void addChild(int index, Node<K, V> node) {
            node.parent = this;
            childs.add(index, node);
        }

        public Node<K, V> removeChild(int index) {
            return childs.removeAt(index);
        }

        public int indexOfChild(Node<K, V> node) {
            return childs.indexOf(node);
        }
    }

    static class Entry<K, V> {
        K k;
        V v;

        public Entry() {
        }

        public Entry(K k, V v) {
            this.k = k;
            this.v = v;
        }
    }

}

