package org.xinhua.example.datastruct.tree.single;

import org.xinhua.example.datastruct.collection.ArrayList;
import org.xinhua.example.datastruct.collection.ArrayQueue;
import org.xinhua.example.datastruct.collection.List;
import org.xinhua.example.datastruct.collection.Queue;

/**
 * @Author: lilong
 * @createDate: 2023/4/24 3:22
 * @Description: B+树
 * @Version: 1.0
 */
public class BPlus<K extends Comparable<K>, V> {

    private static final int MINIMUM_MAX_DEGREE = 3;
    private static final int DEFAULT_MAX_DEGREE = 512;
    private final int MAX_KEY_SIZE;
    private final int MIN_KEY_SIZE;
    private Node<K, V> root;
    private LeafNode<K, V> head;

    public BPlus() {
        this(DEFAULT_MAX_DEGREE);
    }

    public BPlus(int maxDegree) {
        if (maxDegree < MINIMUM_MAX_DEGREE) {
            throw new RuntimeException("max degree >= 3");
        }
        this.MAX_KEY_SIZE = maxDegree - 1;
        this.MIN_KEY_SIZE = MAX_KEY_SIZE >>> 1;
    }

    public void print() {
        if (root == null) {
            return;
        }

        Queue<Node<K, V>> queue = new ArrayQueue<>();
        queue.offer(root);
        int level = 0, size = 0, count = 1;
        Node<K, V> node = null;

        while (!queue.empty()) {
            System.out.print("level " + level + " : ");
            size = count;
            count = 0;
            for (int i = 0; i < size; i++) {
                node = queue.poll();
                System.out.print(node.keys + "   ");
                if (node instanceof InnerNode) {
                    InnerNode<K, V> innerNode = (InnerNode) node;
                    int childSize = innerNode.childs.size();
                    for (int j = 0; j < childSize; j++) {
                        queue.offer(innerNode.childs.get(j));
                    }
                    count += childSize;
                }
            }
            level++;
            System.out.println();
        }
    }

    public V query(K key) {
        if (root == null || key == null) {
            return null;
        }
        Node<K, V> node = root;
        int index = binarySearch(node, key);
        while (node instanceof InnerNode) {
            index = index < 0 ? -index - 1 : index + 1;
            node = ((InnerNode) node).getChild(index);
            index = binarySearch(node, key);
        }
        return index >= 0 ? ((LeafNode<K, V>) node).getValue(index) : null;
    }

    public List<V> rangQuery(K minKey, K maxKey) {
        List<V> result = new ArrayList<>();
        if (root == null || minKey == null || maxKey == null || minKey.compareTo(maxKey) > 0) {
            return result;
        }
        Node<K, V> minKeyNode = root;
        Node<K, V> maxKeyNode = root;
        int minIndex = binarySearch(minKeyNode, minKey);
        int maxIndex = binarySearch(maxKeyNode, maxKey);
        while (minKeyNode instanceof InnerNode) {
            minIndex = minIndex < 0 ? -minIndex - 1 : minIndex + 1;
            minKeyNode = ((InnerNode) minKeyNode).getChild(minIndex);
            minIndex = binarySearch(minKeyNode, minKey);
        }
        while (maxKeyNode instanceof InnerNode) {
            maxIndex = maxIndex < 0 ? -maxIndex - 1 : maxIndex + 1;
            maxKeyNode = ((InnerNode) maxKeyNode).getChild(maxIndex);
            maxIndex = binarySearch(maxKeyNode, maxKey);
        }
        LeafNode<K, V> startNode = (LeafNode<K, V>) minKeyNode;
        LeafNode<K, V> endNode = (LeafNode<K, V>) maxKeyNode;
        int start = minIndex >= 0 ? minIndex : -minIndex - 1;
        int end = maxIndex >= 0 ? maxIndex : -maxIndex - 2;

        if (startNode == endNode) {
            for (int i = start; i <= end; i++) {
                result.add(startNode.getValue(i));
            }
            return result;
        } else {
            int size = startNode.keySize();
            for (int i = start; i < size; i++) {
                result.add(startNode.getValue(i));
            }
        }
        startNode = startNode.next;
        while (startNode != endNode) {
            result.addAll(startNode.values);
            startNode = startNode.next;
        }
        for (int i = 0; i <= end; i++) {
            result.add(endNode.getValue(i));
        }
        return result;
    }

    public V insert(K key, V value) {
        if (key == null) {
            return null;
        }
        if (root == null) {
            head = new LeafNode<>();
            head.addKeyValue(key, value);
            root = head;
            return null;
        }
        return insert(null, root, 0, key, value);
    }

    private V insert(InnerNode<K, V> parent, Node<K, V> node, int nodeIndex, K key, V value) {
        int index = binarySearch(node, key);
        if (node instanceof LeafNode) {
            LeafNode<K, V> leafNode = (LeafNode) node;
            if (index >= 0) {
                return leafNode.setValue(index, value);
            } else {
                leafNode.addKeyValue(-index - 1, key, value);
                if (leafNode.keySize() > MAX_KEY_SIZE) {
                    splitLeaf(parent, leafNode, nodeIndex);
                }
                return null;
            }
        }
        index = index < 0 ? -index - 1 : index + 1;
        InnerNode<K, V> innerNode = (InnerNode<K, V>) node;
        V oldValue = insert(innerNode, innerNode.getChild(index), index, key, value);
        if (innerNode.keySize() > MAX_KEY_SIZE) {
            splitInner(parent, innerNode, nodeIndex);
        }
        return oldValue;
    }

    public V delete(K key) {
        if (root == null || key == null) {
            return null;
        }
        return delete(null, root, 0, key);
    }

    private V delete(InnerNode<K, V> parent, Node<K, V> node, int nodeIndex, K key) {
        int index = binarySearch(node, key);
        if (node instanceof LeafNode) {
            if (index < 0) {
                return null;
            } else {
                LeafNode<K, V> leafNode = (LeafNode) node;
                V value = leafNode.removeKeyValue(index);
                if (index == 0 && nodeIndex > 0 && parent != null) {
                    parent.setKey(nodeIndex - 1, leafNode.getKey(0));
                }
                if (leafNode.keySize() < MIN_KEY_SIZE) {
                    fixLeaf(parent, leafNode, nodeIndex);
                }
                return value;
            }
        }
        index = index < 0 ? -index - 1 : index + 1;
        InnerNode<K, V> innerNode = (InnerNode<K, V>) node;
        V value = delete(innerNode, innerNode.getChild(index), index, key);
        if (innerNode.keySize() < MIN_KEY_SIZE) {
            fixInner(parent, innerNode, nodeIndex);
        }
        return value;
    }

    private void fixLeaf(InnerNode<K, V> parent, LeafNode<K, V> leafNode, int nodeIndex) {
        if (parent == null) {
            if (root.keySize() == 0) {
                root = head = null;
            }
            return;
        }
        if (nodeIndex > 0 && parent.getChild(nodeIndex - 1).keySize() > MIN_KEY_SIZE) {
            LeafNode<K, V> left = (LeafNode) parent.getChild(nodeIndex - 1);
            leafNode.addKeyValue(0, left.removeKey(left.keySize() - 1), left.removeValue(left.keySize()));
            parent.setKey(nodeIndex - 1, leafNode.getKey(0));
        } else if (nodeIndex < parent.keySize() && parent.getChild(nodeIndex + 1).keySize() > MIN_KEY_SIZE) {
            LeafNode<K, V> right = (LeafNode) parent.getChild(nodeIndex + 1);
            leafNode.addKeyValue(right.removeKey(0), right.removeValue(0));
            parent.setKey(nodeIndex, right.getKey(0));
        } else {
            int leftIndex = nodeIndex > 0 ? nodeIndex - 1 : nodeIndex;
            LeafNode<K, V> left = (LeafNode) parent.getChild(leftIndex);
            LeafNode<K, V> right = (LeafNode) parent.getChild(leftIndex + 1);
            left.keys.addAll(right.keys);
            left.values.addAll(right.values);
            left.next = right.next;
            parent.removeKey(leftIndex);
            parent.removeChild(leftIndex + 1);
            if (parent == root && parent.keySize() == 0) {
                root = head = left;
            }
        }
    }

    private void fixInner(InnerNode<K, V> parent, InnerNode<K, V> innerNode, int nodeIndex) {
        if (parent == null) {
            return;
        }
        if (nodeIndex > 0 && parent.getChild(nodeIndex - 1).keySize() > MIN_KEY_SIZE) {
            InnerNode<K, V> left = (InnerNode) parent.getChild(nodeIndex - 1);
            innerNode.addKey(0, parent.getKey(nodeIndex - 1));
            innerNode.addChild(0, left.removeChild(left.keySize()));
            parent.setKey(nodeIndex - 1, left.removeKey(left.keySize() - 1));
        } else if (nodeIndex < parent.keySize() && parent.getChild(nodeIndex + 1).keySize() > MIN_KEY_SIZE) {
            InnerNode<K, V> right = (InnerNode) parent.getChild(nodeIndex + 1);
            innerNode.addKey(parent.getKey(nodeIndex));
            innerNode.addChild(right.removeChild(0));
            parent.setKey(nodeIndex, right.removeKey(0));
        } else {
            int leftIndex = nodeIndex > 0 ? nodeIndex - 1 : nodeIndex;
            InnerNode<K, V> left = (InnerNode) parent.getChild(leftIndex);
            InnerNode<K, V> right = (InnerNode) parent.getChild(leftIndex + 1);
            left.addKey(parent.removeKey(leftIndex));
            left.keys.addAll(right.keys);
            left.childs.addAll(right.childs);
            parent.removeChild(leftIndex + 1);
            if (parent == root && parent.keySize() == 0) {
                root = left;
            }
        }
    }

    private void splitLeaf(InnerNode<K, V> parent, LeafNode<K, V> leafNode, int nodeIndex) {
        LeafNode<K, V> left = leafNode;
        LeafNode<K, V> right = new LeafNode();
        if (parent == null) {
            parent = new InnerNode();
            parent.addKey(left.getKey(MIN_KEY_SIZE));
            parent.addChild(left);
            parent.addChild(right);
            root = parent;
        } else {
            parent.addKey(nodeIndex, left.getKey(MIN_KEY_SIZE));
            parent.addChild(nodeIndex + 1, right);
            right.next = left.next;
        }
        left.next = right;
        for (int i = MIN_KEY_SIZE; i <= MAX_KEY_SIZE; i++) {
            right.addKeyValue(left.getKey(i), left.getValue(i));
        }
        left.removeKeyValue(MIN_KEY_SIZE, MAX_KEY_SIZE);
    }

    private void splitInner(InnerNode<K, V> parent, InnerNode<K, V> innerNode, int nodeIndex) {
        InnerNode<K, V> left = innerNode;
        InnerNode<K, V> right = new InnerNode();
        if (parent == null) {
            parent = new InnerNode();
            parent.addKey(left.getKey(MIN_KEY_SIZE));
            parent.addChild(left);
            parent.addChild(right);
            root = parent;
        } else {
            parent.addKey(nodeIndex, left.getKey(MIN_KEY_SIZE));
            parent.addChild(nodeIndex + 1, right);
        }
        for (int i = MIN_KEY_SIZE + 1; i <= MAX_KEY_SIZE; i++) {
            right.addKey(left.getKey(i));
        }
        for (int i = MIN_KEY_SIZE + 1; i <= MAX_KEY_SIZE + 1; i++) {
            right.addChild(left.getChild(i));
        }
        left.removeKey(MIN_KEY_SIZE, MAX_KEY_SIZE);
        left.removeChild(MIN_KEY_SIZE + 1, MAX_KEY_SIZE + 1);
    }

    private int binarySearch(Node<K, V> node, K key) {
        int cmp, low = 0, mid, high = node.keySize() - 1;
        while (low <= high) {
            mid = (low + high) >>> 1;
            cmp = key.compareTo(node.getKey(mid));
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

    static class Node<K, V> {
        List<K> keys = new ArrayList<>();

        int keySize() {
            return keys.size();
        }

        K getKey(int index) {
            return keys.get(index);
        }

        K setKey(int index, K key) {
            return keys.set(index, key);
        }

        void addKey(K key) {
            keys.add(key);
        }

        void addKey(int index, K key) {
            keys.add(index, key);
        }

        K removeKey(int index) {
            return keys.removeAt(index);
        }

        void removeKey(int start, int end) {
            keys.removeBetween(start, end);
        }
    }

    static class InnerNode<K, V> extends Node<K, V> {
        List<Node<K, V>> childs = new ArrayList<>();

        Node<K, V> getChild(int index) {
            return childs.get(index);
        }

        void addChild(Node child) {
            childs.add(child);
        }

        void addChild(int index, Node child) {
            childs.add(index, child);
        }

        Node<K, V> removeChild(int index) {
            return childs.removeAt(index);
        }

        void removeChild(int start, int end) {
            childs.removeBetween(start, end);
        }
    }

    static class LeafNode<K, V> extends Node<K, V> {
        LeafNode<K, V> next;
        List<V> values = new ArrayList<>();

        V getValue(int index) {
            return values.get(index);
        }

        V setValue(int index, V value) {
            return values.set(index, value);
        }

        V removeValue(int index) {
            return values.removeAt(index);
        }

        void addKeyValue(K key, V value) {
            keys.add(key);
            values.add(value);
        }

        void addKeyValue(int index, K key, V value) {
            keys.add(index, key);
            values.add(index, value);
        }

        V removeKeyValue(int index) {
            keys.removeAt(index);
            return values.removeAt(index);
        }

        void removeKeyValue(int start, int end) {
            keys.removeBetween(start, end);
            values.removeBetween(start, end);
        }
    }

}
