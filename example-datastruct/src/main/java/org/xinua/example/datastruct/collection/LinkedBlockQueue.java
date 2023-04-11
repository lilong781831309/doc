package org.xinua.example.datastruct.collection;

/**
 * @Author: lilong
 * @createDate: 2023/4/11 17:38
 * @Description: 阻塞队列  链表实现
 * @Version: 1.0
 */
public class LinkedBlockQueue<E> extends AbstrackBlockQueue<E> {

    protected Node<E> sentinel = new Node<>();

    public LinkedBlockQueue() {
        this(DEFAULT_CAPACITY);
    }

    public LinkedBlockQueue(int initCapacity) {
        super(initCapacity);
        this.sentinel.prev = sentinel;
        this.sentinel.next = sentinel;
    }

    @Override
    protected void enQueue(E e) {
        Node<E> last = sentinel.prev;
        Node<E> node = new Node<>(e, last, sentinel);
        last.next = node;
        sentinel.prev = node;
    }

    @Override
    protected E deQueue() {
        Node<E> node = sentinel.next;
        sentinel.next = node.next;
        node.next.prev = sentinel;
        return node.e;
    }


    @Override
    public E peek() {
        takeLock.lock();
        try {
            if (empty()) {
                return null;
            }
            return sentinel.next.e;
        } finally {
            takeLock.unlock();
        }
    }

    protected static class Node<E> {
        E e;
        Node<E> prev;
        Node<E> next;

        public Node() {
        }

        public Node(E e, Node<E> prev, Node<E> next) {
            this.e = e;
            this.prev = prev;
            this.next = next;
        }
    }

}
