package org.xinhua.example.zving.castor;

import java.util.Date;

import org.xinhua.example.zving.utility.ObjectUtil;

public class LongCastor extends AbstractCastor {
	private static LongCastor singleton = new LongCastor();

	public static LongCastor getInstance() {
		return singleton;
	}

	private LongCastor() {
	}

	@Override
	public boolean canCast(Class<?> type) {
		return Long.class == type || long.class == type;
	}

	@Override
	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof Number) {
			return ((Number) obj).longValue();
		} else if (obj instanceof Date) {
			return ((Date) obj).getTime();
		} else {
			try {
				String str = obj.toString();
				if (ObjectUtil.empty(str) || str.equals("null") || str.equals("undefined")) {
					return 0;
				}
				return Long.parseLong(obj.toString());
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return 0L;
	}

}
