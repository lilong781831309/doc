package org.xinhua.example.zving.castor;

import java.util.Collection;

import org.xinhua.example.zving.utility.StringUtil;


public class DoubleArrayCastor extends AbstractCastor {
	private static DoubleArrayCastor singleton = new DoubleArrayCastor();

	public static DoubleArrayCastor getInstance() {
		return singleton;
	}

	private DoubleArrayCastor() {
	}

	@Override
	public boolean canCast(Class<?> type) {
		return Double[].class == type || double[].class == type;
	}

	@Override
	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof double[]) {
			return obj;
		} else if (obj.getClass().isArray()) {
			Object[] os = (Object[]) obj;
			double[] arr = new double[os.length];
			for (int i = 0; i < os.length; i++) {
				arr[i] = (Double) DoubleCastor.getInstance().cast(os[i], type);
			}
			return arr;
		} else if (obj instanceof String && isNumberArray(obj.toString())) {
			return toDoubleArray(obj.toString());
		} else if (obj instanceof Collection) {
			Collection<?> c = (Collection<?>) obj;
			double[] arr = new double[c.size()];
			int i = 0;
			for (Object o : c) {
				arr[i++] = (Double) DoubleCastor.getInstance().cast(o, type);
			}
			return arr;
		} else {
			return new double[] { (Double) DoubleCastor.getInstance().cast(obj, type) };
		}
	}

	public static boolean isNumberArray(String str) {
		if (!Character.isDigit(str.charAt(0))) {
			return false;
		}
		if (!Character.isDigit(str.charAt(str.length() - 1))) {
			return false;
		}
		for (int i = 1; i < str.length() - 1; i++) {
			char c = str.charAt(i);
			if (c == ' ') {
				for (int j = i + 1; j < str.length(); j++) {
					if (str.charAt(j) == ' ') {
						i++;
					} else {
						break;
					}
				}
				if (str.charAt(++i) != ',') {
					return false;
				}
				for (int j = i + 1; j < str.length(); j++) {
					if (str.charAt(j) == ' ') {
						i++;
					} else {
						break;
					}
				}
			} else if (c == '.') {
				if (!Character.isDigit(str.charAt(i + 1))) {
					return false;
				}
			} else if (c == ',') {
				for (int j = i + 1; j < str.length(); j++) {
					if (str.charAt(j) == ' ') {
						i++;
					} else {
						break;
					}
				}
				if (!Character.isDigit(str.charAt(i + 1))) {
					return false;
				}
			} else if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	public static double[] toDoubleArray(String str) {
		String[] arr = StringUtil.splitEx(str, ",");
		double[] r = new double[arr.length];
		for (int i = 0; i < arr.length; i++) {
			r[i] = Double.parseDouble(arr[i].trim());
		}
		return r;
	}
}
