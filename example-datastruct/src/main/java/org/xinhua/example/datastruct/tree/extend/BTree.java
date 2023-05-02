package org.xinhua.example.datastruct.tree.extend;

import org.xinhua.example.datastruct.collection.ArrayList;
import org.xinhua.example.datastruct.collection.ArrayStack;
import org.xinhua.example.datastruct.collection.List;
import org.xinhua.example.datastruct.collection.Stack;

import java.util.Comparator;
import java.util.function.BiConsumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/21 0:29
 * @Description: B树        Node含parent指针,非递归实现
 * @Version: 1.0
 */
public class BTree<K, V> {

    private static final int MINIMUM_MAX_DEGREE = 3;
    private static final int DEFAULT_MAX_DEGREE = 512;
    private final int MAX_KEY_SIZE;
    private final int MIN_KEY_SIZE;
    private final Comparator<? super K> comparator;
    private Node<K, V> root;

    public BTree() {
        this(DEFAULT_MAX_DEGREE, null);
    }

    public BTree(int maxDegree) {
        this(maxDegree, null);
    }

    public BTree(Comparator<? super K> comparator) {
        this(DEFAULT_MAX_DEGREE, comparator);
    }

    public BTree(int maxDegree, Comparator<? super K> comparator) {
        if (maxDegree < MINIMUM_MAX_DEGREE) {
            throw new RuntimeException("max degree >= 3");
        }
        this.MAX_KEY_SIZE = maxDegree - 1;
        this.MIN_KEY_SIZE = MAX_KEY_SIZE >>> 1;
        this.comparator = comparator;
    }

    protected int cmp(K k1, K k2) {
        return comparator != null ? comparator.compare(k1, k2) : ((Comparable) k1).compareTo(k2);
    }

    public V insert(K k, V v) {
        if (k == null) {
            return null;
        }
        if (root == null) {
            root = new Node<>(k, v);
            return null;
        }

        V value = null;
        Stack<Node<K, V>> stack = new ArrayStack<>(50);
        Node<K, V> node = root;
        int index = 0;

        while (true) {
            index = binarySearch(node, k);
            if (index >= 0) {
                value = node.setV(index, v);
                break;
            } else {
                index = -index - 1;
                stack.push(node);
                if (node.isLeaf) {
                    node.addEntry(index, k, v);
                    break;
                } else {
                    node = node.getChild(index);
                }
            }
        }

        while (!stack.empty()) {
            node = stack.pop();
            if (node.keySize() > MAX_KEY_SIZE) {
                split(node);
            }
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
        for (int i = 0; i <= k && i < childSize; i++) {
            left.addChild(node.getChild(i));
        }
        for (int i = k + 1; i < childSize; i++) {
            right.addChild(node.getChild(i));
        }

        left.isLeaf = right.isLeaf = node.isLeaf;

        if (parent == null) {
            parent = new Node<>();
            parent.addEntry(node.getEntry(k));
            parent.addChild(left);
            parent.addChild(right);
            root = parent;
            root.isLeaf = false;
        } else {
            int index = parent.indexOfChild(node);
            parent.addEntry(index, node.getEntry(k));
            parent.setChild(index, left);
            parent.addChild(index + 1, right);
        }
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
            while (!predecessor.isLeaf) {
                predecessor = predecessor.getChild(predecessor.keySize());
            }
            node.setEntry(index, predecessor.getEntry(predecessor.keySize() - 1));
            node = predecessor;
            index = predecessor.keySize() - 1;
        }

        if (node == root) {
            if (root.keySize() == 1) {
                root = null;
            } else {
                root.removeEntry(index);
            }
        } else {
            node.removeEntry(index);
            fixAfterDelete(node);
        }

        return value;
    }

    private void fixAfterDelete(Node<K, V> node) {
        while (node != root && node.keySize() < MIN_KEY_SIZE) {
            Node<K, V> parent = node.parent;
            int index = parent.indexOfChild(node);
            if (index > 0 && parent.getChild(index - 1).keySize() > MIN_KEY_SIZE) {
                Node<K, V> leftSibling = parent.getChild(index - 1);
                Entry<K, V> leftEntry = leftSibling.removeEntry(leftSibling.keySize() - 1);
                Entry<K, V> parentEntry = parent.setEntry(index - 1, leftEntry);
                node.addEntry(0, parentEntry);
                if (!leftSibling.isLeaf) {
                    node.addChild(0, leftSibling.removeChild(leftSibling.childSize() - 1));
                }
                break;
            } else if (index < parent.keySize() && parent.getChild(index + 1).keySize() > MIN_KEY_SIZE) {
                Node<K, V> rightSibling = parent.getChild(index + 1);
                Entry<K, V> rightEntry = rightSibling.removeEntry(0);
                Entry<K, V> parentEntry = parent.setEntry(index, rightEntry);
                node.addEntry(parentEntry);
                if (!rightSibling.isLeaf) {
                    node.addChild(rightSibling.removeChild(0));
                }
                break;
            } else {
                int leftIndex = index > 0 ? index - 1 : index;
                Node<K, V> left = parent.getChild(leftIndex);
                Node<K, V> right = parent.removeChild(leftIndex + 1);

                left.addEntry(parent.removeEntry(leftIndex));

                int size = right.keySize();
                for (int i = 0; i < size; i++) {
                    left.addEntry(right.getEntry(i));
                }
                size = right.childSize();
                for (int i = 0; i < size; i++) {
                    left.addChild(right.getChild(i));
                }
                if (root.keySize() == 0) {
                    root = left;
                    root.parent = null;
                    break;
                }
                node = left.parent;
            }
        }
    }

    private int binarySearch(Node<K, V> node, K k) {
        int cmp = 0, low = 0, mid = 0, high = node.keySize() - 1;
        while (low <= high) {
            mid = (low + high) >>> 1;
            cmp = cmp(k, node.getK(mid));
            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    public void inOrder(BiConsumer<K, V> consumer) {
        if (root == null) return;
        inOrder(root, consumer);
    }

    private void inOrder(Node<K, V> node, BiConsumer<K, V> consumer) {
        int keySize = node.keySize();
        if (node.isLeaf) {
            for (int i = 0; i < keySize; i++) {
                consumer.accept(node.getK(i), node.getV(i));
            }
        } else {
            for (int i = 0; i < keySize; i++) {
                inOrder(node.getChild(i), consumer);
                consumer.accept(node.getK(i), node.getV(i));
            }
            inOrder(node.getChild(keySize), consumer);
        }
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