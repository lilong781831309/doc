package org.xinhua.example.datastruct.tree.single;

import org.xinhua.example.datastruct.collection.ArrayList;
import org.xinhua.example.datastruct.collection.List;

import java.util.Comparator;
import java.util.function.BiConsumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/20 0:29
 * @Description: B树    Node不含parent指针,递归实现
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


    public V insert(K key, V value) {
        if (key == null) {
            return null;
        }

        V oldValue = null;
        if (root == null) {
            root = new Node<>();
            root.addEntry(key, value);
        } else {
            oldValue = insert(null, root, key, value, 0);
        }
        return oldValue;
    }

    private V insert(Node<K, V> parent, Node<K, V> node, K key, V value, int nodeIdex) {
        int index = binarySearch(node, key);
        if (index >= 0) {
            return node.setValue(index, value);
        }

        index = -index - 1;
        V oldValue = null;

        if (node.isLeaf) {
            node.addEntry(index, new Entry<>(key, value));
        } else {
            oldValue = insert(node, node.getChild(index), key, value, index);
        }

        if (node.keySize() > MAX_KEY_SIZE) {
            split(parent, node, nodeIdex);
        }
        return oldValue;
    }

    private void split(Node<K, V> parent, Node<K, V> node, int nodeIdex) {
        int keySize = node.keySize();
        int childSize = node.childSize();
        int k = (keySize - 1) >> 1;

        Node<K, V> right = new Node<>();

        if (parent == null) {
            parent = new Node<>();
            parent.addEntry(node.getEntry(k));
            parent.addChild(node);
            parent.addChild(right);
            root = parent;
            root.isLeaf = false;
        } else {
            parent.addEntry(nodeIdex, node.getEntry(k));
            parent.addChild(nodeIdex + 1, right);
        }

        for (int i = k + 1; i < keySize; i++) {
            right.addEntry(node.getEntry(i));
        }
        for (int i = k + 1; i < childSize; i++) {
            right.addChild(node.getChild(i));
        }

        right.isLeaf = node.isLeaf;
        node.removeEntry(k, keySize);
        node.removeChild(k + 1, childSize);
    }

    public V delete(K key) {
        if (root == null || key == null) {
            return null;
        }
        return delete(null, root, key, 0);
    }

    private V delete(Node<K, V> parent, Node<K, V> node, K key, int nodeIndex) {
        V oldValue = null;
        int index = binarySearch(node, key);

        if (node.isLeaf) {
            if (index < 0) {
                return null;
            }
            oldValue = node.removeEntry(index).value;
        } else {
            if (index < 0) {
                index = -index - 1;
            } else {
                Node<K, V> predecessor = node.getChild(index);
                while (!predecessor.isLeaf) {
                    predecessor = predecessor.getChild(predecessor.keySize());
                }
                Entry<K, V> entry = predecessor.getEntry(predecessor.keySize() - 1);
                node.setEntry(index, entry);
                key = entry.key;
            }
            oldValue = delete(node, node.getChild(index), key, index);
        }

        if (node.keySize() < MIN_KEY_SIZE) {
            fixAfterDelete(parent, node, nodeIndex);
        }

        return oldValue;
    }

    private void fixAfterDelete(Node<K, V> parent, Node<K, V> node, int index) {
        if (parent == null) {
            return;
        }
        if (index > 0 && parent.getChild(index - 1).keySize() > MIN_KEY_SIZE) {
            Node<K, V> leftSibling = parent.getChild(index - 1);
            Entry<K, V> leftEntry = leftSibling.removeEntry(leftSibling.keySize() - 1);
            Entry<K, V> parentEntry = parent.setEntry(index - 1, leftEntry);
            node.addEntry(0, parentEntry);
            if (!leftSibling.isLeaf) {
                node.addChild(0, leftSibling.removeChild(leftSibling.childSize() - 1));
            }
        } else if (index < parent.keySize() && parent.getChild(index + 1).keySize() > MIN_KEY_SIZE) {
            Node<K, V> rightSibling = parent.getChild(index + 1);
            Entry<K, V> leftEntry = rightSibling.removeEntry(0);
            Entry<K, V> parentEntry = parent.setEntry(index, leftEntry);
            node.addEntry(parentEntry);
            if (!rightSibling.isLeaf) {
                node.addChild(rightSibling.removeChild(0));
            }
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
                if (root.childSize() > 0) {
                    root = root.getChild(0);
                } else {
                    root = null;
                }
            }
        }
    }

    public void inOrder(BiConsumer<K, V> consumer) {
        if (root == null) return;
        inOrder(root, consumer);
    }

    private void inOrder(Node<K, V> node, BiConsumer<K, V> consumer) {
        int keySize = node.keySize();
        if (node.isLeaf) {
            for (int i = 0; i < keySize; i++) {
                consumer.accept(node.getKey(i), node.getValue(i));
            }
        } else {
            for (int i = 0; i < keySize; i++) {
                inOrder(node.getChild(i), consumer);
                consumer.accept(node.getKey(i), node.getValue(i));
            }
            inOrder(node.getChild(keySize), consumer);
        }
    }

    private int binarySearch(Node<K, V> node, K key) {
        int cmp, low = 0, mid, high = node.keySize() - 1;
        if (comparator != null) {
            while (low <= high) {
                mid = (low + high) >> 1;
                cmp = comparator.compare(key, node.getKey(mid));
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
                mid = (low + high) >> 1;
                cmp = ((Comparable) key).compareTo(((Comparable) node.getKey(mid)));
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
        boolean isLeaf = true;

        public int keySize() {
            return entries.size();
        }

        public int childSize() {
            return childs.size();
        }

        public Node() {
            this.entries = new ArrayList<>();
            this.childs = new ArrayList<>();
        }

        public K getKey(int index) {
            return entries.get(index).key;
        }

        public V getValue(int index) {
            return entries.get(index).value;
        }

        public V setValue(int index, V value) {
            V oldValue = entries.get(index).value;
            entries.get(index).value = value;
            return oldValue;
        }

        public Entry<K, V> getEntry(int index) {
            return entries.get(index);
        }

        public Entry<K, V> setEntry(int index, Entry<K, V> entry) {
            return entries.set(index, entry);
        }

        public void addEntry(int index, Entry<K, V> entry) {
            entries.add(index, entry);
        }

        public void addEntry(Entry<K, V> entry) {
            entries.add(entry);
        }

        public void addEntry(K key, V value) {
            addEntry(new Entry<>(key, value));
        }

        public Entry<K, V> removeEntry(int index) {
            return entries.removeAt(index);
        }

        public void removeEntry(int start, int end) {
            entries.removeBetween(start, end);
        }

        public Node<K, V> getChild(int index) {
            return childs.get(index);
        }

        public Node<K, V> setChild(int index, Node<K, V> child) {
            return childs.set(index, child);
        }

        public void addChild(int index, Node<K, V> child) {
            childs.add(index, child);
        }

        public void addChild(Node<K, V> child) {
            childs.add(child);
        }

        public Node<K, V> removeChild(int index) {
            return childs.removeAt(index);
        }

        public void removeChild(int start, int end) {
            childs.removeBetween(start, end);
        }
    }

    static class Entry<K, V> {
        K key;
        V value;

        public Entry() {
        }

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}

