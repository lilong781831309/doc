package org.xinhua.example.zving.json;

import org.xinhua.example.zving.castor.LongCastor;

import java.util.ArrayList;


public class JSONArray extends ArrayList<Object> implements JSONAware {
	private static final long serialVersionUID = 3957988303675231981L;
	boolean wrapElements = false;// 输出成JSON中元素之间换行

	public boolean isWrapElements() {
		return wrapElements;
	}

	public void setWrapElements(boolean wrapElements) {
		this.wrapElements = wrapElements;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	@Override
	public String toJSONString() {
		return JSON.toJSONString(this);
	}

	public JSONObject getJSONObject(int i) {
		return (JSONObject) get(i);
	}

	public long getLong(int i) {
		return (Long) LongCastor.getInstance().cast(get(i), null);
	}

	public String getString(int i) {
		Object obj = get(i);
		return obj == null ? null : obj.toString();
	}

	public int length() {
		return size();
	}

	public JSONArray getJSONArray(int i) {
		return (JSONArray) get(i);
	}

}
