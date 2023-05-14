package org.xinhua.example.design_pattern.structural.decorator;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * @Author: lilong
 * @createDate: 2023/4/10 2:44
 * @Description: mybits缓存接口  用到的装饰器模式
 * @Version: 1.0
 */
public interface Cache {

    String getId();

    void putObject(Object key, Object value);

    Object getObject(Object key);

    Object removeObject(Object key);

    void clear();

    int getSize();

    default ReadWriteLock getReadWriteLock() {
        return null;
    }
}
