package org.xinhua.example.datastruct.map;

import java.util.function.BiConsumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/27 0:03
 * @Description: Map接口
 * @Version: 1.0
 */
public interface Map<K, V> {

    int size();

    boolean isEmpty();

    boolean containsKey(K key);

    boolean containsValue(V value);

    V get(K key);

    V put(K key, V value);

    V putIfAbsent(K key, V value);

    void putAll(Map<K, V> map);

    V remove(K key);

    V replace(K key, V value);

    void clear();

    Set<K> keySet();

    Collection<V> values();

    Set<Entry<K, V>> entrySet();

    boolean equals(Object o);

    int hashCode();

    void forEach(BiConsumer<? super K, ? super V> action);

    public interface Entry<K, V> {

        K getKey();

        V getValue();

        V setValue(V value);

        boolean equals(Object o);

        int hashCode();

    }

}
