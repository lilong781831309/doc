package org.xinhua.example.datastruct.collection;

/**
 * @Author: lilong
 * @createDate: 2023/4/11 2:27
 * @Description: 栈    链表实现
 * @Version: 1.0
 */
public class LinkedStack<E> implements Stack<E> {

    private int size;
    private Node<E> head = null;

    public LinkedStack() {
    }

    @Override
    public boolean push(E e) {
        Node<E> node = new Node<>(e, head);
        head = node;
        size++;
        return true;
    }

    @Override
    public E pop() {
        if (empty()) {
            return null;
        }
        E e = head.e;
        head = head.next;
        size--;
        return e;
    }

    @Override
    public E peek() {
        if (empty()) {
            return null;
        }
        return head.e;
    }

    @Override
    public boolean empty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    protected class Node<E> {
        E e;
        Node<E> next;

        public Node() {
        }

        public Node(E e, Node<E> next) {
            this.e = e;
            this.next = next;
        }
    }
}
