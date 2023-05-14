package org.xinhua.example.zving.utility;

import org.xinhua.example.zving.collection.ConcurrentMapx;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class BeanDescription {
	Class<?> beanClass;
	ConcurrentMapx<String, BeanProperty> propertyMap;
	ReentrantLock lock = new ReentrantLock();

	/**
	 * 构造器
	 * 
	 * @param clazz Bean的class
	 */
	BeanDescription(Class<?> clazz) {
		beanClass = clazz;
	}

	/**
	 * @return Bean的class
	 */
	public Class<?> getBeanClass() {
		return beanClass;
	}

	/**
	 * @return Bean属性Map
	 */
	public Map<String, BeanProperty> getPropertyMap() {
		checkInitialized();
		return propertyMap;
	}

	void checkInitialized() {
		if (propertyMap == null) {
			lock.lock();
			try {
				if (propertyMap == null) {
					BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
					propertyMap = new ConcurrentMapx<String, BeanProperty>();
					PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
					for (int i = 0; pds != null && i < pds.length; i++) {
						PropertyDescriptor pd = pds[i];
						Method readMethod = BeanManager.getPublicMethod(pd.getReadMethod());
						Method writeMethod = BeanManager.getPublicMethod(pd.getWriteMethod());
						if (readMethod == null && writeMethod == null) {
							continue;
						}
						BeanProperty property = new BeanProperty(readMethod, writeMethod);
						propertyMap.put(pd.getName(), property);
					}
					for (Field f : beanClass.getFields()) {
						if (!Modifier.isPublic(f.getModifiers())) {
							continue;
						}
						if (Modifier.isStatic(f.getModifiers())) {
							continue;
						}
						BeanProperty property = new BeanProperty(f);
						if (!propertyMap.containsKey(property.getName())) {
							propertyMap.put(f.getName(), property);
						}
					}
				}
			} catch (IntrospectionException exc) {
				exc.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}

	/**
	 * 查找Bean属性
	 * 
	 * @param name 属性名称
	 * @return Bean属性实例
	 */
	public BeanProperty getProperty(String name) {
		if (name == null || name.length() == 0) {
			return null;
		}
		checkInitialized();
		BeanProperty bip = propertyMap.get(name);
		if (bip == null) {// 没找到则尝试查找小写的方法
			bip = propertyMap.get(name.substring(0, 1).toLowerCase() + name.substring(1));
		}
		return bip;
	}

}
