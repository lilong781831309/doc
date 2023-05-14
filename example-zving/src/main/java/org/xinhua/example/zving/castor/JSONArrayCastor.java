package org.xinhua.example.zving.castor;

import org.xinhua.example.zving.json.JSON;
import org.xinhua.example.zving.json.JSONArray;

import java.util.Collection;

public class JSONArrayCastor extends AbstractCastor {
	private static JSONArrayCastor singleton = new JSONArrayCastor();

	public static JSONArrayCastor getInstance() {
		return singleton;
	}

	private JSONArrayCastor() {
	}

	@Override
	public boolean canCast(Class<?> type) {
		return type.isAssignableFrom(Collection.class) || type == String.class || type.isArray();
	}

	@Override
	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof JSONArray) {
			return obj;
		} else if (obj.getClass().isArray()) {
			Object[] os = (Object[]) obj;
			JSONArray arr = new JSONArray();
			for (int i = 0; i < os.length; i++) {
				arr.add(os[i]);
			}
			return arr;
		} else if (obj instanceof Collection) {
			JSONArray arr = new JSONArray();
			Collection<?> c = (Collection<?>) obj;
			arr.addAll(c);
			return arr;
		} else {
			String str = obj.toString();
			if (str.startsWith("[") && str.endsWith("]")) {
				return JSON.parseJSONArray(str);
			}
			return null;
		}
	}

}
