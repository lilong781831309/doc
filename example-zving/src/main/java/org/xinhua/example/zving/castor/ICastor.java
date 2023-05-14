package org.xinhua.example.zving.castor;



public interface ICastor {
	/**
	 * @param type 类型
	 * @return 指定类型是否可以由本实例转换
	 */
	public boolean canCast(Class<?> type);

	/**
	 * @param obj 待转换的对象
	 * @param type 目标类型
	 * @return 将对象转换成目标类型
	 */
	public Object cast(Object obj, Class<?> type);
}
