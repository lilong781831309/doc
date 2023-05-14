package org.xinhua.example.zving.json;

import org.xinhua.example.zving.utility.BeanUtil;
import org.xinhua.example.zving.utility.DateUtil;
import org.xinhua.example.zving.utility.FastStringBuilder;
import org.xinhua.example.zving.utility.Primitives;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;


public class JSON {

    public static String toJSONString(Object value) {
        return toJSONString(value, true);
    }

    public static String toJSONString(Object value, boolean toUnicode) {
        FastStringBuilder sb = new FastStringBuilder();
        toJSONString(value, sb, toUnicode);
        return sb.toStringAndClose();
    }

    public static void toJSONString(Object value, FastStringBuilder sb) {
        toJSONString(value, sb, true);
    }

    public static void toJSONString(Object value, FastStringBuilder sb, boolean toUnicode) {
        if (value == null) {
            sb.append("null");
            return;
        }
        if (value instanceof String) {
            sb.append('\"');
            escape((String) value, sb, toUnicode);
            sb.append('\"');
            return;
        }
        if (value instanceof Double) {
            Double d = (Double) value;
            if (d.isInfinite()) {
                sb.append("Number.NaN");
            } else if (d.isNaN()) {
                sb.append("Infinity");
            } else {
                sb.append(value);
            }
            return;
        }
        if (value instanceof Float) {
            Float d = (Float) value;
            if (d.isInfinite()) {
                sb.append("Number.NaN");
            } else if (d.isNaN()) {
                sb.append("Infinity");
            } else {
                sb.append(value);
            }
            return;
        }
        if (Primitives.isPrimitives(value)) {
            sb.append(value);
            return;
        }
        if (value.getClass().isArray()) {
            arrayToJSONString(value, sb, toUnicode);
            return;
        }
        if (value instanceof Map) {
            toJSONString((Map<?, ?>) value, sb, toUnicode);
            return;
        }
        if (value instanceof Collection) {
            Collection<?> c = (Collection<?>) value;
            toJSONString(c, sb, toUnicode);
            return;
        }
        if (value instanceof Date) {
            Date c = (Date) value;
            sb.append(DateUtil.toString(c));
            return;
        }
        if (value instanceof JSONAware) {
            sb.append(((JSONAware) value).toJSONString());
            return;
        }
        // 最后作为JavaBean
        beanToJSONString(value, sb, toUnicode);
    }

    private static void toJSONString(Collection<?> c, FastStringBuilder sb, boolean toUnicode) {
        if (c == null) {
            sb.append("null");
            return;
        }
        boolean first = true;
        sb.append('[');
        for (Object value : c) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
                if (c instanceof JSONArray && ((JSONArray) c).isWrapElements()) {
                    sb.append('\n');
                }
            }

            if (value == null) {
                sb.append("null");
                continue;
            }
            toJSONString(value, sb, toUnicode);
        }
        sb.append(']');
    }

    private static void arrayToJSONString(Object arr, FastStringBuilder sb, boolean toUnicode) {
        if (arr == null) {
            sb.append("null");
        }
        int size = Array.getLength(arr);
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append(',');
            }
            Object value = Array.get(arr, i);
            if (value == null) {
                sb.append("null");
                continue;
            }
            toJSONString(value, sb, toUnicode);
        }
        sb.append(']');
    }

    private static void beanToJSONString(Object bean, FastStringBuilder sb, boolean toUnicode) {
        Map<String, Object> map = BeanUtil.toMap(bean, true);
        map.put("@Class", bean.getClass().getName());
        toJSONString(map, sb, toUnicode);
    }

    private static void toJSONString(Map<?, ?> map, FastStringBuilder sb, boolean toUnicode) {
        if (map == null) {
            sb.append("null");
            return;
        }

        boolean first = true;
        sb.append('{');
        Object value = null;
        for (Entry<?, ?> entry : map.entrySet()) {
            value = entry.getValue();
            if (value == map) {// 如果元素是自身
                continue;
            }
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            toJSONString(String.valueOf(entry.getKey()), value, sb, toUnicode);
        }
        sb.append('}');
    }

    private static void toJSONString(String key, Object value, FastStringBuilder sb, boolean toUnicode) {
        sb.append('\"');
        if (key == null) {
            sb.append("null");
        } else {
            escape(key, sb, toUnicode);
        }
        sb.append('\"').append(':');
        toJSONString(value, sb, toUnicode);
    }

    static void escape(String s, FastStringBuilder sb, boolean toUnicode) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (toUnicode
                            && (ch >= '\u0000' && ch <= '\u001F' || ch >= '\u007F' && ch <= '\u009F' || ch >= '\u2000' && ch <= '\u20FF')) {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
            }
        }
    }

    public static Object parse(String json) {
        if (json == null) {
            return json;
        }
        DefaultJSONParser parser = new DefaultJSONParser(json);
        Object v = parser.parse();
        parser.close();
        if (v instanceof JSONObject) {
            return tryReverse((JSONObject) v);
        }
        return v;
    }

    public static Object tryReverse(JSONObject obj) {
        String type = obj.getString("@type");
        if (type == null) {
            String className = obj.getString("@Class");
            if (className != null) {
                try {
                    Object bean = BeanUtil.create(Class.forName(className));
                    BeanUtil.fill(bean, obj);
                    return bean;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if ("Date".equals(type)) {
            return new Date(obj.getLong("time"));
        }
        return obj;
    }

    /**
     * 从JSON字符串中得到一个JSONArray
     *
     * @param json
     * @return
     */
    public static JSONArray parseJSONArray(String json) {
        if (json == null) {
            return null;
        }
        DefaultJSONParser parser = new DefaultJSONParser(json);
        JSONArray v = new JSONArray();
        parser.parseArray(v);
        parser.close();
        return v;
    }

    /**
     * 从JSON字符串中得到一个JSONObject
     *
     * @param json
     * @return
     */
    public static JSONObject parseJSONObject(String json) {
        if (json == null) {
            return null;
        }
        DefaultJSONParser parser = new DefaultJSONParser(json);
        JSONObject v = new JSONObject();
        parser.parseObject(v);
        parser.close();
        return v;
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseBean(String json, Class<T> clazz) {// NO_UCD
        Object v = parse(json);
        if (clazz.isAssignableFrom(v.getClass())) {
            return (T) v;
        }
        if (v instanceof JSONObject) {
            try {
                T t = clazz.newInstance();
                BeanUtil.fill(t, (JSONObject) v);
                return t;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 格式化输出一段JSON文本
     */
    public static String format(String json) {
        Object obj = parse(json);
        if (obj instanceof JSONArray) {
            return format((JSONArray) obj);
        } else {
            return format((JSONObject) obj);
        }
    }

    /**
     * 格式化输出一个JSONObject对象
     */
    public static String format(JSONObject jo) {
        FastStringBuilder sb = new FastStringBuilder();
        format(jo, sb, "");
        return sb.toString();
    }

    /**
     * 格式化输出一个JSONObject对象，所有属性之前都会加指定前缀（一般是空格或者制表符）
     */
    private static void format(JSONObject jo, FastStringBuilder sb, String prefix) {
        sb.append(prefix);
        sb.append("{\n");
        boolean first = true;
        String nextPrefix = prefix + "\t";
        for (Entry<String, Object> e : jo.entrySet()) {
            if (first) {
                first = false;
                sb.append(nextPrefix);
            } else {
                sb.append(',');
                sb.append('\n');
                sb.append(nextPrefix);
            }
            sb.append('\"');
            sb.append(e.getKey());
            sb.append("\":");
            Object v = e.getValue();
            if (v instanceof JSONArray) {
                sb.append('\n');
                format((JSONArray) v, sb, nextPrefix);
            } else if (v instanceof JSONObject) {
                sb.append('\n');
                format((JSONObject) v, sb, nextPrefix);
            } else if (v instanceof String || v instanceof Date) {
                sb.append('\"');
                sb.append(v);
                sb.append('\"');
                sb.append(',');
            } else {
                sb.append(v);
            }
        }
        sb.append('\n');
        sb.append(prefix);
        sb.append("}");
    }

    /**
     * 格式化输出一个JSONArray对象
     */
    public static String format(JSONArray jo) {
        FastStringBuilder sb = new FastStringBuilder();
        format(jo, sb, "");
        return sb.toString();
    }

    /**
     * 格式化输出一个JSONArray对象，所有属性之前都会加指定前缀（一般是空格或者制表符）
     */
    private static void format(JSONArray jo, FastStringBuilder sb, String prefix) {
        sb.append(prefix);
        sb.append("[\n");
        boolean first = true;
        String nextPrefix = prefix + "\t";
        for (Object v : jo) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
                sb.append('\n');
            }
            if (v instanceof JSONArray) {
                format((JSONArray) v, sb, nextPrefix);
            } else if (v instanceof JSONObject) {
                format((JSONObject) v, sb, nextPrefix);
            } else if (v instanceof String || v instanceof Date) {
                sb.append(nextPrefix);
                sb.append('\"');
                sb.append(v);
                sb.append('\"');
            } else {
                sb.append(nextPrefix);
                sb.append(v);
            }
        }
        sb.append('\n');
        sb.append(prefix);
        sb.append("]");
    }
}
