package org.xinhua.example.datastruct.tree;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author: lilong
 * @createDate: 2023/4/16 7:41
 * @Description: 工具类
 * @Version: 1.0
 */
public class Util {

    static Random random = new Random();

    public static List<Integer> getShuffleIntegers(int bound, int count) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < count; i++) {
            set.add(random.nextInt(bound));
        }

        List<Integer> list = new ArrayList<>(set);
        Collections.shuffle(list);

        return list;
    }

    public static Field getField(Class clazz, String fieldName) {
        while (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    if (field.getName().equals(fieldName)) {
                        field.setAccessible(true);
                        return field;
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

}
