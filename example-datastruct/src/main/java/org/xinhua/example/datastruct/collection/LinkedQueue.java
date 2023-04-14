package org.xinhua.example.datastruct.collection;

/**
 * @Author: lilong
 * @createDate: 2023/4/11 2:14
 * @Description: 队列    链表实现
 * @Version: 1.0
 */
public class LinkedQueue<E> implements Queue<E> {

    protected int size;
    protected Node<E> sentinel = new Node<>();

    public LinkedQueue() {
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    @Override
    public boolean offer(E e) {
        Node<E> last = sentinel.prev;
        Node<E> node = new Node<>(e, last, sentinel);
        last.next = node;
        sentinel.prev = node;
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (empty()) {
            return null;
        }
        Node<E> node = sentinel.next;
        sentinel.next = node.next;
        node.next.prev = sentinel;
        size--;
        return node.e;
    }

    @Override
    public E peek() {
        return sentinel.next.e;
    }

    @Override
    public boolean empty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
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
