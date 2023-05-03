package org.xinhua.example.datastruct.map;

import org.xinhua.example.datastruct.collection.*;

import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiConsumer;

/**
 * @Author: lilong
 * @createDate: 2023/5/2 15:11
 * @Description: 跳表
 * @Version: 1.0
 */
public class SkipListMap<K, V> implements Map<K, V> {
    private static final int MAX_LEVEL = 32;
    private final Comparator<? super K> comparator;
    private Random random = new Random();
    private Index<K, V> head;
    private int size;
    private int level;
    private KeySet keySet;
    private Values values;
    private EntrySet entrySet;

    public SkipListMap() {
        this(null);
    }

    public SkipListMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    private int cmp(K k1, K k2) {
        return comparator != null ? comparator.compare(k1, k2) : ((Comparable) k1).compareTo(k2);
    }

    public void print() {
        Stack<List<String>> stack = new ArrayStack();
        Stack<Index> indexStack = new ArrayStack();
        Index p = head;
        while (p != null) {
            indexStack.push(p);
            p = p.down;
        }

        Node node = head.node;
        Node q = node;
        int maxWidth = 0;
        List<String> list = new ArrayList();
        while (q != null) {
            String s = q.value.toString();
            if (s.length() > maxWidth) {
                maxWidth = s.length();
            }
            list.add(s);
            q = q.next;
        }
        stack.push(list);

        while (!indexStack.empty()) {
            list = new ArrayList();
            Index index = indexStack.pop();
            q = node;
            while (q != null && index != null) {
                if (q == index.node) {
                    list.add(q.value.toString());
                    index = index.right;
                } else {
                    list.add("");
                }
                q = q.next;
            }
            stack.push(list);
        }

        String cell = "%-" + (maxWidth + 2) + "s";
        while (!stack.empty()) {
            List<String> stringList = stack.pop();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < stringList.size(); i++) {
                sb.append(cell);
            }
            System.out.println(String.format(sb.toString(), stringList.toArray()));
        }
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
        return getNode(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        if (head == null) {
            return false;
        }
        Node<K, V> node = head.node;
        if (value == null) {
            while (node != null) {
                if (node.value == null) {
                    return true;
                }
                node = node.next;
            }
        } else {
            while (node != null) {
                if (value.equals(node.value)) {
                    return true;
                }
                node = node.next;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        Node<K, V> node = getNode(key);
        return node != null ? node.value : null;
    }

    private Node<K, V> getNode(K key) {
        Index<K, V> q = head, p = q;
        int c;
        while (true) {
            if ((c = cmp(key, p.node.key)) == 0) {
                return p.node;
            } else if (c > 0 && p.right != null) {
                p = (q = p).right;
            } else if (q.down == null) {
                break;
            } else {
                q = q.down;
                p = q.right;
            }
        }
        Node<K, V> node = q.node;
        while (node != null) {
            if ((c = cmp(key, node.key)) == 0) {
                return node;
            } else if (c > 0) {
                node = node.next;
            } else {
                break;
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        return putVal(key, value, false);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return putVal(key, value, true);
    }

    private V putVal(K key, V value, boolean putIfAbsent) {
        if (key == null) {
            return null;
        }
        if (head == null || cmp(key, head.node.key) < 0) {
            return addHead(key, value);
        }
        //查找新节点插入位置,并记录前一个索引
        Stack<Index> stack = new ArrayStack<>();
        Index<K, V> q = head, p = q;
        int c;
        while (true) {
            if ((c = cmp(key, p.node.key)) == 0) {
                return putIfAbsent ? p.node.value : p.node.setValue(value);
            } else if (c > 0 && p.right != null) {
                p = (q = p).right;
            } else if (q.down == null) {
                stack.push(q);
                break;
            } else {
                stack.push(q);
                q = q.down;
                p = q.right;
            }
        }
        //插入新节点
        Node<K, V> prev = null, next = q.node;
        while (next != null && cmp(key, next.key) > 0) {
            prev = next;
            next = next.next;
        }
        Node<K, V> node = new Node<>(key, value, next);
        prev.next = node;
        //增加索引
        int randomLevel = randomLevel(MAX_LEVEL);
        int maxLevel = randomLevel < level ? randomLevel : level;
        int levels = 0;
        Index index = null;
        while (!stack.empty() && levels++ < maxLevel) {
            Index preIndex = stack.pop();
            index = new Index(node, preIndex.right, index);
            preIndex.right = index;
        }
        //最上层增加索引
        if (randomLevel > level && level < MAX_LEVEL) {
            index = new Index(node, null, index);
            head = new Index<>(head.node, index, head);
            level++;
        }
        size++;
        return null;
    }

    private V addHead(K key, V value) {
        if (head == null) {
            Node<K, V> node = new Node<>(key, value, null);
            head = new Index<>(node, null, null);
            level = size = 1;
            return null;
        }
        //插入新节点
        Node<K, V> node = new Node<>(key, value, head.node);
        Index newHead = new Index(node, head, null);
        Index p = head.down, q = newHead;
        //添加索引
        while (p != null) {
            Index index = new Index(node, p, null);
            q.down = index;
            p = p.down;
            q = index;
        }
        //最上层添加索引
        if (level < MAX_LEVEL && randomLevel(1) > 0) {
            newHead = new Index(node, null, newHead);
            level++;
        }
        head = newHead;
        size++;
        return null;
    }

    private int randomLevel(int maxLevel) {
        int level = 0;
        while (level < maxLevel) {
            if (random.nextInt(2) == 0) {
                break;
            }
            level++;
        }
        return level;
    }

    @Override
    public void putAll(Map<K, V> map) {
        if (map != null) {
            map.forEach((k, v) -> {
                put(k, v);
            });
        }
    }

    @Override
    public V remove(K key) {
        Node<K, V> remove = remove(key, null, false);
        return remove == null ? null : remove.value;
    }

    private Node<K, V> remove(K key, V value) {
        return remove(key, value, true);
    }

    private Node<K, V> remove(K key, V value, boolean matchValue) {
        if (head == null || key == null) {
            return null;
        }
        if (cmp(key, head.node.key) == 0) {
            return removeHead(value, matchValue);
        }
        Index<K, V> q = head, p = q, index = null;
        int c;
        while (true) {
            if ((c = cmp(key, p.node.key)) == 0) {
                index = p;
                break;
            } else if (c > 0 && p.right != null) {
                p = (q = p).right;
            } else if (q.down == null) {
                break;
            } else {
                q = q.down;
                p = q.right;
            }
        }
        Node<K, V> prev = predecessor(q, key);
        if (prev == null) {
            return null;
        }
        Node<K, V> delete = prev.next;
        if (matchValue && !Objects.equals(value, delete.value)) {
            return null;
        }
        if (index != null) {
            deleteIndex(q, key);
        }
        prev.next = delete.next;
        size--;
        return delete;
    }

    private void deleteIndex(Index<K, V> q, K key) {
        Index<K, V> p = q.right;
        int c;
        while (true) {
            while ((c = cmp(key, p.node.key)) > 0 && p.right != null) {
                p = (q = p).right;
            }
            if (c == 0) {
                q.right = p.right;
            }
            if (q.down == null) {
                break;
            } else {
                q = q.down;
                p = q.right;
            }
        }
        tryReduceLevel();
    }

    private void tryReduceLevel() {
        Index p = head;
        Index d = head.down;
        if (d == null) {
            return;
        }
        if (p.right == null && d.right == null) {
            head = d;
        } else if (p.right != null && d.right != null && p.right.right == null && d.right.right == null) {
            head = d;
        }
    }

    private Node<K, V> predecessor(Index<K, V> q, K key) {
        Index<K, V> p = q.right;
        while (p != null) {
            if (cmp(p.node.key, key) < 0 && p.right != null) {
                p = (q = p).right;
            } else if (q.down == null) {
                break;
            } else {
                q = q.down;
                p = q.right;
            }
        }
        Node<K, V> node = q.node, pre = null;
        while (node != null) {
            if (cmp(node.key, key) < 0) {
                pre = node;
                node = node.next;
            } else {
                break;
            }
        }
        return pre;
    }

    private Node<K, V> removeHead(V value, boolean matchValue) {
        Node<K, V> delete = head.node;
        if (matchValue && !Objects.equals(value, delete.value)) {
            return null;
        }
        Node<K, V> next = delete.next;
        if (next == null) {
            head = null;
            size = level = 0;
            return delete;
        }
        Index p = head;
        //减少一层索引
        if (p.right == null && level > 1 && randomLevel(1) == 0) {
            p = p.down;
            level--;
        }
        //最上层有next节点的索引,替换head
        if (p.right != null && p.right.node == next) {
            head = p.right;
            size--;
            return delete;
        }
        //每一层如果没有next节点的索引则添加
        Index newHead = new Index<>(next, p.right, null);
        Index q = newHead;
        p = p.down;
        while (p != null) {
            if (p.right == null || p.right.node != next) {
                Index index = new Index(next, p.right, null);
                q.down = index;
                p = p.down;
                q = index;
            } else {
                q.down = p.right;
                break;
            }
        }
        head = newHead;
        size--;
        return delete;
    }

    @Override
    public V replace(K key, V value) {
        Node<K, V> node = getNode(key);
        return node != null ? node.setValue(value) : null;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        if (head != null) {
            Node<K, V> node = head.node;
            while (node != null) {
                action.accept(node.key, node.value);
                node = node.next;
            }
        }
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
        public boolean contains(K key) {
            return getNode(key) != null;
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public boolean remove(K k) {
            return remove(k);
        }
    }

    class Values extends AbstractCollection<V> {

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(V value) {
            return containsValue(value);
        }

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public boolean remove(V value) {
            if (head == null || value == null) {
                return false;
            }
            Node<K, V> node = head.node;
            if (value == null) {
                while (node != null) {
                    if (node.value == null) {
                        Node<K, V> remove = SkipListMap.this.remove(node.key, null);
                        return remove != null;
                    }
                    node = node.next;
                }
            } else {
                while (node != null) {
                    if (value.equals(node.value)) {
                        Node<K, V> remove = SkipListMap.this.remove(node.key, value);
                        return remove != null;
                    }
                    node = node.next;
                }
            }
            return false;
        }
    }

    class EntrySet extends AbstractSet<Entry<K, V>> {

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(Entry<K, V> entry) {
            if (entry == null || !(entry instanceof Node)) {
                return false;
            }
            Node<K, V> node = getNode(entry.getKey());
            return node != null && node.equals(entry);
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
            Node<K, V> remove = SkipListMap.this.remove(entry.getKey(), entry.getValue());
            return remove != null;
        }
    }

    class KeyIterator extends ListIterator implements Iterator<K> {
        @Override
        public K next() {
            return nextNode().key;
        }
    }

    class ValueIterator extends ListIterator implements Iterator<V> {
        @Override
        public V next() {
            return nextNode().value;
        }
    }

    class EntryIterator extends ListIterator implements Iterator<Entry<K, V>> {
        @Override
        public Entry<K, V> next() {
            return nextNode();
        }
    }

    class ListIterator {
        Node<K, V> cur;
        Node<K, V> next;

        public ListIterator() {
            if (head != null) {
                next = head.node;
            }
        }

        public boolean hasNext() {
            return next != null;
        }

        public Node<K, V> nextNode() {
            cur = next;
            next = next.next;
            return cur;
        }

        public void remove() {
            SkipListMap.this.remove(cur.key, cur.value);
        }
    }

    static class Index<K, V> {
        Node<K, V> node;
        Index<K, V> right, down;

        public Index(Node<K, V> node, Index right, Index down) {
            this.node = node;
            this.right = right;
            this.down = down;
        }
    }

    static class Node<K, V> implements Entry<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
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
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }
}
