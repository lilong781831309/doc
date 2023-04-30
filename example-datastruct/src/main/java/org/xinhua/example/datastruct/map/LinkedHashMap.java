package org.xinhua.example.datastruct.map;


import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/29 20:07
 * @Description: 有序哈希map
 * @Version: 1.0
 */
public class LinkedHashMap<K, V> extends HashMap<K, V> {

    private Node<K, V> head, tail;

    public LinkedHashMap() {
        super();
    }

    public LinkedHashMap(int initCapacity) {
        super(initCapacity);
    }

    public LinkedHashMap(int initCapacity, float loadFactor) {
        super(initCapacity, loadFactor);
    }

    @Override
    public boolean containsValue(V value) {
        Node<K, V> p = head;
        while (p != null) {
            if (Objects.equals(value, p.value)) {
                return true;
            }
            p = (p instanceof LinkedNode) ? ((LinkedNode) p).after : ((LinkedTreeNode) p).after;
        }
        return false;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        Node<K, V> p = head;
        while (p != null) {
            action.accept(p.key, p.value);
            p = (p instanceof LinkedNode) ? ((LinkedNode) p).after : ((LinkedTreeNode) p).after;
        }
    }

    @Override
    public void clear() {
        super.clear();
        head = tail = null;
    }

    @Override
    protected void afterPut(Node node) {
        if (tail == null) {
            head = tail = node;
        } else {
            if (tail instanceof LinkedNode) {
                ((LinkedNode) tail).after = node;
            } else {
                ((LinkedTreeNode) tail).after = node;
            }
            if (node instanceof LinkedNode) {
                ((LinkedNode) node).before = tail;
            } else {
                ((LinkedTreeNode) node).before = tail;
            }
            tail = node;
        }
    }

    @Override
    protected void afterDelete(Node delete) {
        Node<K, V> pb = delete instanceof LinkedNode ? ((LinkedNode) delete).before : ((LinkedTreeNode) delete).before;
        Node<K, V> pa = delete instanceof LinkedNode ? ((LinkedNode) delete).after : ((LinkedTreeNode) delete).after;
        if (pb == null) {
            head = pa;
        } else if (pb instanceof LinkedNode) {
            ((LinkedNode) pb).after = pa;
        } else {
            ((LinkedTreeNode) pb).after = pa;
        }
        if (pa == null) {
            tail = pb;
        } else if (pa instanceof LinkedNode) {
            ((LinkedNode) pa).before = pb;
        } else {
            ((LinkedTreeNode) pa).before = pb;
        }
    }

    @Override
    protected Node newNode(int hash, Object key, Object value, Node next) {
        return new LinkedNode(hash, key, value, next);
    }

    @Override
    protected TreeNode newTreeNode(int hash, Object key, Object value, Node next) {
        return new LinkedTreeNode(hash, key, value, next);
    }

    @Override
    protected TreeNode replaceWithTreeNode(Node p) {
        LinkedTreeNode node = new LinkedTreeNode(p.hash, p.key, p.value, null);

        Node<K, V> pb = p instanceof LinkedNode ? ((LinkedNode) p).before : ((LinkedTreeNode) p).before;
        Node<K, V> pa = p instanceof LinkedNode ? ((LinkedNode) p).after : ((LinkedTreeNode) p).after;
        node.before = pb;
        node.after = pa;

        if (pb != null) {
            if (pb instanceof LinkedNode) {
                ((LinkedNode) pb).after = node;
            } else {
                ((LinkedTreeNode) pb).after = node;
            }
        }
        if (pa != null) {
            if (pa instanceof LinkedNode) {
                ((LinkedNode) pa).before = node;
            } else {
                ((LinkedTreeNode) pb).before = node;
            }
        }
        if (p == head) {
            head = node;
        }
        if (p == tail) {
            tail = node;
        }
        return node;
    }

    @Override
    public Set<K> keySet() {
        if (keySet == null) {
            keySet = new LinkedKeySet();
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new LinkedValues();
        }
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new LinkedEntrySet();
        }
        return entrySet;
    }

    class LinkedKeySet extends KeySet {
        @Override
        public Iterator<K> iterator() {
            return new LinkedKeyIterator();
        }
    }

    class LinkedValues extends Values {
        @Override
        public Iterator<V> iterator() {
            return new LinkedValueIterator();
        }
    }

    class LinkedEntrySet extends EntrySet {
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new LinkedEntryIterator();
        }
    }

    class LinkedKeyIterator extends LinkedHashIterator implements Iterator<K> {
        @Override
        public K next() {
            Entry<K, V> node = (Node) nextNode();
            return node.getKey();
        }
    }

    class LinkedValueIterator extends LinkedHashIterator implements Iterator<V> {
        @Override
        public V next() {
            Entry<K, V> node = nextNode();
            return node.getValue();
        }
    }

    class LinkedEntryIterator extends LinkedHashIterator implements Iterator<Entry<K, V>> {
        @Override
        public Entry<K, V> next() {
            return nextNode();
        }
    }

    abstract class LinkedHashIterator extends HashIterator {

        Node<K, V> cur;
        Node<K, V> next;

        public LinkedHashIterator() {
            next = head;
        }

        @Override
        public Node<K, V> nextNode() {
            cur = next;
            next = (next instanceof LinkedNode) ? ((LinkedNode) next).after : ((LinkedTreeNode) next).after;
            return cur;
        }

    }

    class LinkedNode<K, V> extends Node {

        Node<K, V> before, after;

        public LinkedNode(int hash, K key, V value, Node next) {
            super(hash, key, value, next);
        }
    }

    class LinkedTreeNode<K, V> extends TreeNode {

        Node<K, V> before, after;

        public LinkedTreeNode(int hash, K key, V value, Node next) {
            super(hash, key, value, next);
        }
    }

}
