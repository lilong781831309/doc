package org.xinhua.example.zving.collection;

public abstract class ExitEventListener<K, V> {
	/**
	 * 键值对从容器中清除时会调用此方法
	 * 
	 * @param key
	 * @param value
	 */
	public abstract void onExit(K key, V value);

}
