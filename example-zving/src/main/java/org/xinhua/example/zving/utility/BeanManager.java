package org.xinhua.example.zving.utility;

import org.xinhua.example.zving.collection.ConcurrentMapx;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class BeanManager {
	private static ConcurrentMapx<Class<?>, BeanDescription> managerMap = new ConcurrentMapx<Class<?>, BeanDescription>();

	/**
	 * @return clazz对应的Bean描述信息实例
	 */
	public static BeanDescription getBeanDescription(Class<?> clazz) {
		BeanDescription ret = managerMap.get(clazz);
		if (ret == null) {
			ret = loadDescription(clazz);
		}
		return ret;
	}

	private static BeanDescription loadDescription(Class<?> pClass) {
		BeanDescription ret = managerMap.get(pClass);
		if (ret == null) {
			ret = new BeanDescription(pClass);
			managerMap.put(pClass, ret);
		}
		return ret;
	}

	protected static Method getPublicMethod(Method m) {
		if (m == null) {
			return null;
		}
		Class<?> clazz = m.getDeclaringClass();
		if (Modifier.isPublic(clazz.getModifiers())) {
			return m;
		}
		Method ret = getPublicMethod(clazz, m);
		if (ret != null) {
			return ret;
		} else {
			return m;
		}
	}

	private static Method getPublicMethod(Class<?> clazz, Method pMethod) {
		if (Modifier.isPublic(clazz.getModifiers())) {
			try {
				Method m;
				try {
					m = clazz.getDeclaredMethod(pMethod.getName(), pMethod.getParameterTypes());
				} catch (java.security.AccessControlException ex) {
					m = clazz.getMethod(pMethod.getName(), pMethod.getParameterTypes());
				}
				if (Modifier.isPublic(m.getModifiers())) {
					return m;
				}
			} catch (NoSuchMethodException exc) {
			}
		}
		Class<?>[] interfaces = clazz.getInterfaces();
		if (interfaces != null) {
			for (Class<?> itf : interfaces) {
				Method m = getPublicMethod(itf, pMethod);
				if (m != null) {
					return m;
				}
			}
		}
		Class<?> superclass = clazz.getSuperclass();
		if (superclass != null) {
			Method m = getPublicMethod(superclass, pMethod);
			if (m != null) {
				return m;
			}
		}
		return null;
	}

}
