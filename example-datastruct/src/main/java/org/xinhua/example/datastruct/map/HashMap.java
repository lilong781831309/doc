package org.xinhua.example.datastruct.map;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/27 4:47
 * @Description: 哈希map
 * @Version: 1.0
 */
public class HashMap<K, V> implements Map<K, V> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int TREEIFY_THRESHOLD = 8;
    private static final int UNTREEIFY_THRESHOLD = 6;
    private static final int MIN_TREEIFY_CAPACITY = 64;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private float loadFactor;
    private KeySet keySet;
    private Values values;
    private EntrySet entrySet;

    public HashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int initCapacity) {
        this(initCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int initCapacity, float loadFactor) {
        if (initCapacity <= 0) {
            initCapacity = DEFAULT_INITIAL_CAPACITY;
        } else if (initCapacity > MAXIMUM_CAPACITY) {
            initCapacity = MAXIMUM_CAPACITY;
        }
        this.threshold = tableSizeFor(initCapacity);
        this.loadFactor = loadFactor;
    }

    private int tableSizeFor(int capacity) {
        int n = capacity - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n > MAXIMUM_CAPACITY ? MAXIMUM_CAPACITY : n + 1;
    }

    private Node<K, V>[] resize() {
        if (table == null) {
            table = new Node[threshold];
            threshold = (int) (threshold * loadFactor);
            return table;
        }

        int oldCapacity = table.length;
        if (oldCapacity >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return table;
        }

        int newCapacity = oldCapacity << 1;
        int newThreshold = threshold << 1;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (int i = 0; i < oldCapacity; i++) {
            Node node = table[i];
            if (node != null) {
                table[i] = null;
                if (node.next == null) {
                    newTable[node.hash & (newCapacity - 1)] = node;
                } else if (node instanceof TreeNode) {
                    splitTree(newTable, (TreeNode) node);
                } else {
                    splitList(newTable, node);
                }
            }
        }

        threshold = newThreshold;
        table = newTable;
        return newTable;
    }

    private void splitList(Node<K, V>[] newTable, Node node) {
        int newCapacity = newTable.length;
        int oldCapacity = newCapacity >> 1;
        Node loHead = null, loTail = null, hiHead = null, hiTail = null;
        while (node != null) {
            if ((node.hash & oldCapacity) == 0) {
                if (loHead == null) {
                    loHead = loTail = node;
                } else {
                    loTail.next = node;
                    loTail = node;
                }
            } else {
                if (hiHead == null) {
                    hiHead = hiTail = node;
                } else {
                    hiTail.next = node;
                    hiTail = node;
                }
            }
            node = node.next;
        }
        if (loTail != null) {
            loTail.next = null;
            newTable[loHead.hash & (oldCapacity - 1)] = loHead;
        }
        if (hiTail != null) {
            hiTail.next = null;
            newTable[hiHead.hash & (newCapacity - 1)] = hiHead;
        }
    }

    private void splitTree(Node<K, V>[] newTable, TreeNode node) {
        TreeNode loHead = null, loTail = null, hiHead = null, hiTail = null;
        int newCapacity = newTable.length;
        int oldCapacity = newCapacity >> 1;
        int loCount = 0, hiCount = 0;
        while (node != null) {
            if ((node.hash & oldCapacity) == 0) {
                if (loHead == null) {
                    loHead = loTail = node;
                } else {
                    loTail.next = node;
                    node.prev = loTail;
                    loTail = node;
                }
                loCount++;
            } else {
                if (hiHead == null) {
                    hiHead = hiTail = node;
                } else {
                    hiTail.next = node;
                    node.prev = hiTail;
                    hiTail = node;
                }
                hiCount++;
            }
            node = (TreeNode) node.next;
        }
        if (loTail != null) {
            loTail.next = null;
            newTable[loHead.hash & (oldCapacity - 1)] = loHead;
            if (loCount < UNTREEIFY_THRESHOLD) {
                untreeify(newTable, loHead);
            } else {
                treeify(newTable, loHead);
            }
        }
        if (hiTail != null) {
            hiTail.next = null;
            newTable[hiHead.hash & (newCapacity - 1)] = hiHead;
            if (hiCount < UNTREEIFY_THRESHOLD) {
                untreeify(newTable, hiHead);
            } else {
                treeify(newTable, hiHead);
            }
        }

    }

    private int hash(K k) {
        if (k == null) return 0;
        int h = k.hashCode();
        return h ^ (h >>> 16);
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
        if (table == null || table.length == 0) {
            return false;
        }
        for (Node node : table) {
            Node e = node;
            while (e != null) {
                if (Objects.equals(value, e.value)) {
                    return true;
                }
                e = e.next;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    private Node<K, V> getNode(K key) {
        if (table == null || table.length == 0) {
            return null;
        }
        int hash = hash(key);
        Node<K, V> e = table[hash & (table.length - 1)];
        if (e == null) {
            return null;
        } else if (e instanceof TreeNode) {
            return getTreeNode((TreeNode) e, hash, key);
        } else {
            while (e != null) {
                if (hash == e.hash && (key == e.key || key.equals(e.key))) {
                    return e;
                }
                e = e.next;
            }
            return null;
        }
    }

    private Node<K, V> getTreeNode(TreeNode root, int hash, K key) {
        TreeNode<K, V> p = root, s;
        int cmp;
        while (p != null) {
            if (hash < p.hash) {
                p = p.left;
            } else if (hash > p.hash) {
                p = p.right;
            } else if (key == p.key || key.equals(p.key)) {
                return p;
            } else if ((cmp = System.identityHashCode(key) - System.identityHashCode(p.key)) == 0 && (s = search(p.right, hash, key)) != null) {
                return s;
            } else if (cmp <= 0) {
                p = p.left;
            } else {
                p = p.right;
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
        if (table == null || table.length == 0) {
            resize();
        }
        int hash = hash(key);
        int index = hash & (table.length - 1);
        Node<K, V> node = table[index];
        Node<K, V> e = null;

        if (node == null) {
            table[index] = new Node<>(hash, key, value, null);
        } else if (node instanceof TreeNode) {
            e = putTreeVal(table, index, hash, key, value);
        } else {
            Node<K, V> head = node, tail = node;
            e = node;
            int count = 1;
            while (e != null) {
                count++;
                if (hash == e.hash && (key == e.key || key.equals(e.key))) {
                    break;
                }
                tail = e;
                e = e.next;
            }
            if (e == null) {
                tail.next = new Node<>(hash, key, value, null);
                if (count >= TREEIFY_THRESHOLD) {
                    treeifyBin(table, head);
                }
            }
        }

        if (e != null) {
            return putIfAbsent ? e.value : e.setValue(value);
        } else {
            if (++size > threshold) {
                resize();
            }
            return null;
        }
    }

    private TreeNode putTreeVal(Node[] tab, int index, int hash, K key, V value) {
        TreeNode<K, V> root = (TreeNode) tab[index], parent = null, p = root, s;
        int cmp = 0;
        while (p != null) {
            parent = p;
            if ((cmp = hash - p.hash) < 0) {
                p = p.left;
            } else if (cmp > 0) {
                p = p.right;
            } else if (key == p.key || key.equals(p.key)) {
                return p;
            } else if ((cmp = System.identityHashCode(key) - System.identityHashCode(p.key)) == 0 && (s = search(p.right, hash, key)) != null) {
                return s;
            } else if (cmp <= 0) {
                p = p.left;
            } else {
                p = p.right;
            }
        }

        TreeNode<K, V> pn = (TreeNode) parent.next;
        TreeNode e = new TreeNode(hash, key, value, pn);
        if (cmp <= 0) {
            parent.left = e;
        } else {
            parent.right = e;
        }
        e.parent = e.prev = parent;
        parent.next = e;
        if (pn != null) {
            pn.prev = e;
        }
        root = fixupAfterInsert(root, e);
        moveRootToHead(root, (TreeNode) tab[index]);
        tab[index] = root;
        return null;
    }

    private TreeNode search(TreeNode p, int hash, K key) {
        if (p == null) {
            return null;
        }
        if (hash == p.hash && (key == p.key || key.equals(p.key))) {
            return p;
        }
        if (System.identityHashCode(key) - System.identityHashCode(p.key) != 0) {
            return null;
        }
        TreeNode e = search(p.left, hash, key);
        if (e != null) {
            return e;
        }
        return search(p.right, hash, key);
    }

    private void treeifyBin(Node<K, V>[] tab, Node<K, V> first) {
        if (tab.length < MIN_TREEIFY_CAPACITY) {
            resize();
        } else {
            Node<K, V> p = first;
            TreeNode<K, V> head = null, tail = null, e = null;
            while (p != null) {
                e = new TreeNode<>(p.hash, p.key, p.value, null);
                if (head == null) {
                    head = tail = e;
                } else {
                    tail.next = e;
                    e.prev = tail;
                    tail = e;
                }
                p = p.next;
            }
            treeify(tab, head);
        }
    }

    private void treeify(Node<K, V>[] tab, TreeNode<K, V> head) {
        TreeNode<K, V> root = head, e = (TreeNode) head.next, parent, p;
        setBlack(root);
        root.parent = root.left = root.right = null;

        int cmp = 0;
        while (e != null) {
            e.parent = e.left = e.right = null;
            parent = null;
            p = root;
            while (p != null) {
                parent = p;
                if ((cmp = e.hash - p.hash) == 0) {
                    cmp = System.identityHashCode(e.key) - System.identityHashCode(p.key);
                }
                if (cmp <= 0) {
                    p = p.left;
                } else if (cmp > 0) {
                    p = p.right;
                }
            }
            if (cmp <= 0) {
                parent.left = e;
            } else {
                parent.right = e;
            }
            e.parent = parent;
            root = fixupAfterInsert(root, e);
            e = (TreeNode) e.next;
        }
        moveRootToHead(root, head);
        tab[root.hash & (tab.length - 1)] = root;
    }

    private void untreeify(Node<K, V>[] tab, TreeNode<K, V> root) {
        Node p = root, head = null, tail = null, e = null;
        while (p != null) {
            e = new Node(p.hash, p.key, p.value, null);
            if (head == null) {
                head = tail = e;
            } else {
                tail.next = e;
                tail = e;
            }
            p = p.next;
        }
        tab[head.hash & (tab.length - 1)] = head;
    }

    private void moveRootToHead(TreeNode<K, V> root, TreeNode<K, V> head) {
        if (root != head) {
            TreeNode<K, V> rp = root.prev;
            TreeNode<K, V> rn = (TreeNode) root.next;
            if (rp != null) {
                rp.next = rn;
            }
            if (rn != null) {
                rn.prev = rp;
            }
            head.prev = root;
            root.next = head;
            root.prev = null;
        }
    }

    private TreeNode fixupAfterInsert(TreeNode root, TreeNode e) {
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
                    root = rotateRight(root, grand);
                } else {
                    setBlack(e);
                    setRed(grand);
                    root = rotateLeft(root, parent);
                    root = rotateRight(root, grand);
                }
                break;
            } else {
                if (e == parent.left) {
                    setBlack(e);
                    setRed(grand);
                    root = rotateRight(root, parent);
                    root = rotateLeft(root, grand);
                } else {
                    setBlack(parent);
                    setRed(grand);
                    root = rotateLeft(root, grand);
                }
                break;
            }
        }
        setBlack(root);
        return root;
    }

    private TreeNode fixupAfterDelete(TreeNode root, TreeNode e) {
        if (isRed(e)) {
            setBlack(e);
            return root;
        }

        TreeNode parent = e.parent;
        TreeNode sibling = null;

        while (parent != null) {
            if (e == parent.left) {
                sibling = parent.right;
                if (isRed(sibling)) {
                    setRed(parent);
                    setBlack(sibling);
                    root = rotateLeft(root, parent);
                    continue;
                } else if (isRed(sibling.right)) {
                    sibling.color = parent.color;
                    setBlack(parent);
                    setBlack(sibling.right);
                    root = rotateLeft(root, parent);
                    break;
                } else if (isRed(sibling.left)) {
                    sibling.left.color = parent.color;
                    setBlack(parent);
                    root = rotateRight(root, sibling);
                    root = rotateLeft(root, parent);
                    break;
                }
            } else {
                sibling = parent.left;
                if (isRed(sibling)) {
                    setRed(parent);
                    setBlack(sibling);
                    root = rotateRight(root, parent);
                    continue;
                } else if (isRed(sibling.left)) {
                    sibling.color = parent.color;
                    setBlack(parent);
                    setBlack(sibling.left);
                    root = rotateRight(root, parent);
                    break;
                } else if (isRed(sibling.right)) {
                    sibling.right.color = parent.color;
                    setBlack(parent);
                    root = rotateLeft(root, sibling);
                    root = rotateRight(root, parent);
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
        return root;
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

    private TreeNode predecessor(TreeNode e) {
        if (e == null) {
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
        if (e == null) {
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

    private TreeNode rotateLeft(TreeNode root, TreeNode e) {
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
        return root;
    }

    private TreeNode rotateRight(TreeNode root, TreeNode e) {
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
        return root;
    }

    @Override
    public void putAll(Map<K, V> map) {
        if (map != null) {
            int msize = map.size();
            if (table == null) {
                int mthreshold = tableSizeFor(msize);
                threshold = mthreshold < MAXIMUM_CAPACITY ? mthreshold : MAXIMUM_CAPACITY;
            } else if (size + msize > threshold) {
                resize();
            }
            Set<Entry<K, V>> entrySet = map.entrySet();
            entrySet.forEach(entry -> {
                put(entry.getKey(), entry.getValue());
            });
        }
    }

    @Override
    public V remove(K key) {
        int hash = hash(key);
        int index = hash & (table.length - 1);
        Node<K, V> node = removeNode(hash, index, key);
        return node == null ? null : node.value;
    }

    public V remove(K key, V value) {
        int hash = hash(key);
        int index = hash & (table.length - 1);
        Node<K, V> node = removeNode(hash, index, key, value, true);
        return node == null ? null : node.value;
    }

    private Node removeNode(int hash, int index, K key) {
        return removeNode(hash, index, key, null, false);
    }

    private Node removeNode(int hash, int index, K key, V value, boolean matchValue) {
        if (table == null || table.length == 0) {
            return null;
        }
        Node<K, V> delete = null;
        Node<K, V> e = table[index];

        if (e == null) {
            return null;
        } else if (e instanceof TreeNode) {
            delete = getTreeNode((TreeNode) e, hash, key);
            if (delete != null && (!matchValue || Objects.equals(value, delete.value))) {
                table[index] = removeTreeNode((TreeNode) e, (TreeNode) delete);
                size--;
            }
        } else {
            Node<K, V> prev = null;
            while (e != null && !(hash == e.hash && (key == e.key || key.equals(e.key)))) {
                prev = e;
                e = e.next;
            }
            if (e != null && (!matchValue || Objects.equals(value, e.value))) {
                delete = e;
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    table[index] = e.next;
                }
                size--;
            }
        }
        return delete;
    }

    private TreeNode removeTreeNode(TreeNode root, TreeNode e) {
        if (e.left != null && e.right != null) {
            TreeNode predecessor = predecessor(e);
            e.hash = predecessor.hash;
            e.key = predecessor.key;
            e.value = predecessor.value;
            e = predecessor;
        }
        TreeNode ep = e.prev;
        TreeNode en = (TreeNode) e.next;
        if (ep != null) ep.next = en;
        if (en != null) en.prev = ep;

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
            root = fixupAfterDelete(root, e);
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
            root = fixupAfterDelete(root, replace);
        }
        return root;
    }

    @Override
    public V replace(K key, V value) {
        Node<K, V> node = getNode(key);
        if (node != null) {
            return node.setValue(value);
        }
        return null;
    }

    @Override
    public void clear() {
        if (size > 0) {
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
        }
        size = 0;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        for (int i = 0; i < table.length; i++) {
            Node<K, V> e = table[i];
            while (e != null) {
                action.accept(e.key, e.value);
                e = e.next;
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
        public boolean remove(K key) {
            int hash = hash(key);
            int index = hash & (table.length - 1);
            Node node = removeNode(hash, index, key);
            return node != null;
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
            if (table == null || table.length == 0) {
                return false;
            }
            for (Node node : table) {
                Node<K, V> e = node;
                while (e != null) {
                    if (Objects.equals(value, e.value)) {
                        int index = e.hash & (table.length - 1);
                        return removeNode(e.hash, index, e.key) != null;
                    }
                    e = e.next;
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
            if (entry == null || !(entry instanceof Node)) {
                return false;
            }
            K key = entry.getKey();
            int hash = hash(key);
            int index = hash & (table.length - 1);
            return removeNode(hash, index, key, entry.getValue(), true) != null;
        }
    }

    class KeyIterator extends HashIterator implements Iterator<K> {
        @Override
        public K next() {
            return nextNode().key;
        }
    }

    class ValueIterator extends HashIterator implements Iterator<V> {
        @Override
        public V next() {
            return nextNode().value;
        }
    }

    class EntryIterator extends HashIterator implements Iterator<Entry<K, V>> {
        @Override
        public Entry<K, V> next() {
            return nextNode();
        }
    }

    abstract class HashIterator {

        Node<K, V> cur;
        Node<K, V> next;
        int index;

        public HashIterator() {
            if (table != null && table.length > 0) {
                while (index < table.length && (next = table[index]) == null) {
                    index++;
                }
            }
        }

        public boolean hasNext() {
            return next != null;
        }

        public Node<K, V> nextNode() {
            cur = next;
            next = next.next;
            if (next == null && index < table.length - 1) {
                while (++index < table.length && (next = table[index]) == null) {
                }
            }
            return cur;
        }

        public void remove() {
            removeNode(cur.hash, cur.hash & (table.length - 1), cur.key);
        }
    }

    class Node<K, V> implements Entry<K, V> {
        int hash;
        K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
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
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    class TreeNode<K, V> extends Node<K, V> {
        TreeNode<K, V> parent, left, right, prev;
        boolean color = RED;

        public TreeNode(int hash, K key, V value, Node<K, V> next) {
            super(hash, key, value, next);
        }
    }

}
