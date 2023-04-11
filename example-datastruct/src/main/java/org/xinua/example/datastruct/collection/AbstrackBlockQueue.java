package org.xinua.example.datastruct.collection;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: lilong
 * @createDate: 2023/4/11 17:47
 * @Description: 阻塞队列抽象类
 * @Version: 1.0
 */
public abstract class AbstrackBlockQueue<E> implements BlockQueue<E> {

    protected static final int DEFAULT_CAPACITY = 1024;

    protected int capacity;

    protected AtomicInteger size = new AtomicInteger();

    protected ReentrantLock takeLock = new ReentrantLock();

    protected ReentrantLock putLock = new ReentrantLock();

    protected Condition notEmpty = takeLock.newCondition();

    protected Condition notFull = putLock.newCondition();

    public AbstrackBlockQueue() {
        this(DEFAULT_CAPACITY);
    }

    public AbstrackBlockQueue(int initCapacity) {
        if (initCapacity <= 0) {
            initCapacity = DEFAULT_CAPACITY;
        }
        this.capacity = initCapacity;
    }

    @Override
    public boolean offer(E e) {
        int c;
        putLock.lock();
        try {
            if (full()) {
                return false;
            }

            enQueue(e);
            c = size.getAndIncrement();

            if (c < capacity) {
                notFull.signal();
            }
        } finally {
            putLock.unlock();
        }

        if (c == 1) {
            signalNotEmpty();
        }

        return true;
    }


    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        int c;
        putLock.lockInterruptibly();
        try {
            long t = unit.toNanos(timeout);
            while (full()) {
                if (t <= 0) {
                    return false;
                }
                t = notFull.awaitNanos(t);
            }

            enQueue(e);
            c = size.getAndIncrement();

            if (c < capacity) {
                notFull.signal();
            }
        } finally {
            putLock.unlock();
        }

        if (c == 1) {
            signalNotEmpty();
        }

        return true;
    }

    @Override
    public void put(E e) throws InterruptedException {
        int c;
        putLock.lockInterruptibly();
        try {
            while (full()) {
                notFull.await();
            }

            enQueue(e);
            c = size.getAndIncrement();

            if (c < capacity) {
                notFull.signal();
            }
        } finally {
            putLock.unlock();
        }

        if (c == 1) {
            signalNotEmpty();
        }
    }

    @Override
    public E poll() {
        E e = null;
        int c;
        takeLock.lock();
        try {
            if (empty()) {
                return null;
            }

            e = deQueue();
            c = size.getAndDecrement();

            if (c > 0) {
                notEmpty.signal();
            }
        } finally {
            takeLock.unlock();
        }

        if (c == capacity - 1) {
            signalNotFull();
        }

        return e;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E e = null;
        int c;
        takeLock.lockInterruptibly();
        try {
            long t = unit.toNanos(timeout);
            while (empty()) {
                if (t <= 0) {
                    return null;
                }
                t = notEmpty.awaitNanos(t);
            }

            e = deQueue();
            c = size.getAndDecrement();

            if (c > 0) {
                notEmpty.signal();
            }
        } finally {
            takeLock.unlock();
        }

        if (c == capacity - 1) {
            signalNotFull();
        }

        return e;
    }

    @Override
    public E take() throws InterruptedException {
        E e = null;
        int c;
        takeLock.lockInterruptibly();
        try {
            while (empty()) {
                notEmpty.await();
            }

            e = deQueue();
            c = size.getAndDecrement();

            if (c > 0) {
                notEmpty.signal();
            }
        } finally {
            takeLock.unlock();
        }

        if (c == capacity - 1) {
            signalNotFull();
        }

        return e;
    }

    @Override
    public boolean empty() {
        return size.get() == 0;
    }

    @Override
    public int size() {
        return size.get();
    }

    protected boolean full() {
        return size.get() == capacity;
    }

    protected abstract void enQueue(E e);

    protected abstract E deQueue();

    protected void signalNotEmpty() {
        takeLock.lock();
        try {
            notEmpty.signalAll();
        } finally {
            takeLock.unlock();
        }
    }

    protected void signalNotFull() {
        putLock.lock();
        try {
            notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

}
