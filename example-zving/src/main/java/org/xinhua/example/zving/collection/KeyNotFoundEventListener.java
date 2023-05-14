package org.xinhua.example.zving.collection;

public interface KeyNotFoundEventListener<K, V> {
	public V onKeyNotFound(K key);
}
