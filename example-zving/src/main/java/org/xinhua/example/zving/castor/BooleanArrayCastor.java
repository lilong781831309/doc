package org.xinhua.example.zving.castor;

import java.util.Collection;


public class BooleanArrayCastor extends AbstractCastor {
	private static BooleanArrayCastor singleton = new BooleanArrayCastor();

	private BooleanArrayCastor() {
	}

	public static BooleanArrayCastor getInstance() {
		return singleton;
	}

	@Override
	public boolean canCast(Class<?> type) {
		return Boolean[].class == type || boolean[].class == type;
	}

	@Override
	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof boolean[]) {
			return obj;
		} else if (obj.getClass().isArray()) {
			Object[] os = (Object[]) obj;
			boolean[] arr = new boolean[os.length];
			for (int i = 0; i < os.length; i++) {
				arr[i] = (Boolean) BooleanCastor.getInstance().cast(os[i], type);
			}
			return arr;
		} else if (obj instanceof Collection) {
			Collection<?> c = (Collection<?>) obj;
			boolean[] arr = new boolean[c.size()];
			int i = 0;
			for (Object o : c) {
				arr[i++] = (Boolean) BooleanCastor.getInstance().cast(o, type);
			}
			return arr;
		} else {
			return new boolean[] { (Boolean) BooleanCastor.getInstance().cast(obj, type) };
		}
	}

}
