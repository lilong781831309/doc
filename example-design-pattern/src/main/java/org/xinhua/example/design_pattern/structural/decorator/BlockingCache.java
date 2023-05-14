package org.xinhua.example.design_pattern.structural.decorator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author: lilong
 * @createDate: 2023/4/10 2:45
 * @Description: mybits 缓存实现类 BlockingCache
 * @Version: 1.0
 */
public class BlockingCache implements Cache {

    private long timeout;
    private final Cache delegate;
    private final ConcurrentHashMap<Object, CountDownLatch> locks;

    public BlockingCache(Cache delegate) {
        this.delegate = delegate;
        this.locks = new ConcurrentHashMap<>();
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public int getSize() {
        return delegate.getSize();
    }

    @Override
    public void putObject(Object key, Object value) {
        try {
            delegate.putObject(key, value);
        } finally {
            releaseLock(key);
        }
    }

    @Override
    public Object getObject(Object key) {
        acquireLock(key);
        Object value = delegate.getObject(key);
        if (value != null) {
            releaseLock(key);
        }
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        releaseLock(key);
        return null;
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    private void acquireLock(Object key) {
        CountDownLatch newLatch = new CountDownLatch(1);
        while (true) {
            CountDownLatch latch = locks.putIfAbsent(key, newLatch);
            if (latch == null) {
                break;
            }
            try {
                if (timeout > 0) {
                    boolean acquired = latch.await(timeout, TimeUnit.MILLISECONDS);
                    if (!acquired) {
                        throw new RuntimeException();
                    }
                } else {
                    latch.await();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void releaseLock(Object key) {
        CountDownLatch latch = locks.remove(key);
        if (latch == null) {
            throw new IllegalStateException();
        }
        latch.countDown();
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
