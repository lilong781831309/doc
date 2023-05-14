package org.xinhua.example.zving.castor;

import org.xinhua.example.zving.utility.DateUtil;

import java.util.Date;



public class DateCastor extends AbstractCastor {
	private static DateCastor singleton = new DateCastor();

	public static DateCastor getInstance() {
		return singleton;
	}

	private DateCastor() {
	}

	@Override
	public boolean canCast(Class<?> type) {
		return Date.class == type;
	}

	@Override
	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Date) {
			return obj;
		} else if (obj instanceof Long) {
			return new Date((Long) obj);
		} else {
			return DateUtil.parseDateTime(obj.toString());
		}
	}

}
