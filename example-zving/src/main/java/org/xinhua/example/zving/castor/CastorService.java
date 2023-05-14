package org.xinhua.example.zving.castor;

import org.xinhua.example.zving.utility.ObjectUtil;

import java.util.Date;

public class CastorService {

    public static Object toType(Object obj, Class<?> type) {
        if (obj == null) {
            if (ObjectUtil.in(type, Integer.class, int.class, Long.class, long.class, Float.class, float.class, Double.class, double.class)) {
                return 0;
            }
            return obj;
        }
        if (type.isInstance(obj)) {
            return obj;
        }
        if (type == String.class) {
            return obj.toString();
        }
        if (type == Integer.class || type == int.class) {
            return IntCastor.getInstance().cast(obj, type);
        } else if (type == Long.class || type == long.class) {
            return LongCastor.getInstance().cast(obj, type);
        } else if (type == Float.class || type == float.class) {
            return FloatCastor.getInstance().cast(obj, type);
        } else if (type == Double.class || type == double.class) {
            return DoubleCastor.getInstance().cast(obj, type);
        } else if (type == Boolean.class || type == boolean.class) {
            return BooleanCastor.getInstance().cast(obj, type);
        } else if (type == Date.class) {
            return DateCastor.getInstance().cast(obj, type);
        } else if (type == String[].class) {
            return StringArrayCastor.getInstance().cast(obj, type);
        } else if (type == int[].class || type == Integer[].class) {
            return StringArrayCastor.getInstance().cast(obj, type);
        } else if (type == long[].class || type == Long[].class) {
            return LongArrayCastor.getInstance().cast(obj, type);
        } else if (type == float[].class || type == Float[].class) {
            return FloatArrayCastor.getInstance().cast(obj, type);
        } else if (type == double[].class || type == Double[].class) {
            return DoubleArrayCastor.getInstance().cast(obj, type);
        } else if (type == boolean[].class || type == Boolean[].class) {
            return BooleanArrayCastor.getInstance().cast(obj, type);
        } else {
            throw new RuntimeException(type.getClass().getName());
        }
    }

}
