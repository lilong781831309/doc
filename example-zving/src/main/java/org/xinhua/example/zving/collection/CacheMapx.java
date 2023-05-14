package org.xinhua.example.zving.collection;

import java.util.Map;

public final class CacheMapx<K, V> extends Mapx<K, V> {
	private static final long serialVersionUID = 201404182029L;
	final int maxCapacity;
	private ExitEventListener<K, V> exitListener;

	public CacheMapx() {
		super(true);
		this.maxCapacity = Integer.MAX_VALUE - 32;
		accessCount = 0;// 用来表明需要更新Entry.lastAccess
	}

	public CacheMapx(int maxCapacity) {
		super(true);
		if (maxCapacity > Integer.MAX_VALUE - 32) {
			maxCapacity = Integer.MAX_VALUE - 32;
		}
		this.maxCapacity = maxCapacity;
		accessCount = 0;// 用来表明需要更新Entry.lastAccess
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		if (size >= maxCapacity + 32) {
			wlock.lock();
			try {
				for (int i = 0; i < 32; i++) {
					Entry<K, V> min = null;
					for (Map.Entry<K, V> entry : entrySet()) {
						Entry<K, V> e = (Entry<K, V>) entry;
						if (min == null || min.lastAccess >= e.lastAccess) {
							min = e;
						}
					}
					removeMapping(min);
					if (exitListener != null) {
						exitListener.onExit(min.getKey(), min.getValue());
					}
				}
			} finally {
				wlock.unlock();
			}
		}
		return false;// 永远都返回false
	}

	/**
	 * 设置换出事件监听器,当键值对换出调用
	 */
	public void setExitEventListener(ExitEventListener<K, V> listener) {
		this.exitListener = listener;
	}

}
