package org.xinua.example.datastruct.collection;

/**
 * @Author: lilong
 * @createDate: 2023/4/11 2:31
 * @Description: 双端队列    链表实现
 * @Version: 1.0
 */
public class LinkedDeQueue<E> extends LinkedQueue<E> implements Deque<E> {

    public LinkedDeQueue() {
        super();
    }

    @Override
    public boolean offerFirst(E e) {
        Node<E> first = sentinel.next;
        Node<E> node = new Node<>(e, sentinel, first);
        sentinel.next = node;
        first.prev = node;
        size++;
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        return super.offer(e);
    }

    @Override
    public E pollFirst() {
        return super.poll();
    }

    @Override
    public E pollLast() {
        if (empty()) {
            return null;
        }
        Node<E> last = sentinel.prev;
        last.prev.next = last.next;
        last.next.prev = last.prev;
        last.prev = last.next = null;
        size--;
        return last.e;
    }

    @Override
    public E peekFirst() {
        return super.peek();
    }

    @Override
    public E peekLast() {
        if (empty()) {
            return null;
        }
        return sentinel.prev.e;
    }

}
