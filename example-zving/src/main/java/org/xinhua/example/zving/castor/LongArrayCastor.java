package org.xinhua.example.zving.castor;

import java.util.Collection;

public class LongArrayCastor extends AbstractCastor {
	private static LongArrayCastor singleton = new LongArrayCastor();

	public static LongArrayCastor getInstance() {
		return singleton;
	}

	private LongArrayCastor() {
	}

	@Override
	public boolean canCast(Class<?> type) {
		return Long[].class == type || long[].class == type;
	}

	@Override
	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof long[]) {
			return obj;
		} else if (obj.getClass().isArray()) {
			Object[] os = (Object[]) obj;
			long[] arr = new long[os.length];
			for (int i = 0; i < os.length; i++) {
				arr[i] = (Long) LongCastor.getInstance().cast(os[i], type);
			}
			return arr;
		} else if (obj instanceof String && DoubleArrayCastor.isNumberArray(obj.toString())) {
			double[] ds = DoubleArrayCastor.toDoubleArray(obj.toString());
			long[] arr = new long[ds.length];
			for (int i = 0; i < ds.length; i++) {
				arr[i] = new Double(ds[i]).longValue();
			}
			return arr;
		} else if (obj instanceof Collection) {
			Collection<?> c = (Collection<?>) obj;
			long[] arr = new long[c.size()];
			int i = 0;
			for (Object o : c) {
				arr[i++] = (Long) LongCastor.getInstance().cast(o, type);
			}
			return arr;
		} else {
			return new long[] { (Long) LongCastor.getInstance().cast(obj, type) };
		}
	}

}
