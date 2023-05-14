package org.xinhua.example.zving.castor;


import org.xinhua.example.zving.utility.NumberUtil;


public class BooleanCastor extends AbstractCastor {
	private static BooleanCastor singleton = new BooleanCastor();

	public static BooleanCastor getInstance() {
		return singleton;
	}

	private BooleanCastor() {
	}

	@Override
	public boolean canCast(Class<?> type) {
		return Boolean.class == type || boolean.class == type;
	}

	@Override
	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Number) {
			return ((Number) obj).doubleValue() > 0;
		} else if (obj instanceof Boolean) {
			return ((Boolean) obj).booleanValue();
		} else if (obj instanceof String) {
			if (obj.equals("") || obj.equals("false") || obj.equals("null")) {
				return false;
			}
			if (NumberUtil.isNumber((String) obj)) {
				return Double.parseDouble((String) obj) > 0;
			}
			return true;
		} else {
			return false;
		}
	}

}
