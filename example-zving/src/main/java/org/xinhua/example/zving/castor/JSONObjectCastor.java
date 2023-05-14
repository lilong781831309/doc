package org.xinhua.example.zving.castor;

import org.xinhua.example.zving.json.JSON;
import org.xinhua.example.zving.json.JSONObject;
import org.xinhua.example.zving.utility.ObjectUtil;

import java.util.Map;


public class JSONObjectCastor extends AbstractCastor {
	private static JSONObjectCastor singleton = new JSONObjectCastor();

	public static JSONObjectCastor getInstance() {
		return singleton;
	}

	private JSONObjectCastor() {
	}

	@Override
	public boolean canCast(Class<?> type) {
		return type.isAssignableFrom(Map.class) || type == String.class;
	}

	@Override
	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof JSONObject) {
			return obj;
		} else if (obj instanceof Map) {
			JSONObject jo = new JSONObject();
			Map<?, ?> map = (Map<?, ?>) obj;
			jo.putAll(ObjectUtil.toStringObjectMap(map));
			return jo;
		} else {
			String str = obj.toString();
			if (str.startsWith("{") && str.endsWith("}")) {
				return JSON.parseJSONObject(str);
			}
			return null;
		}
	}

}
