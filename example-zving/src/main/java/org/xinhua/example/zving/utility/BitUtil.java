package org.xinhua.example.zving.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 位运算
 */
public class BitUtil {
    /**
     * 将集合中的数字执行位或
     *
     * @param collection
     * @return
     */
    public static int bitOr(Collection<?> collection) {
        int result = 0;
        if (collection != null) {
            for (Object object : collection) {
                result = result | Primitives.getInteger(object);
            }
        }
        return result;
    }

    /**
     * 将字符串数组转换成数字，并进行位或
     *
     * @param values
     * @return
     */
    public static int bitOr(String... values) {
        int result = 0;
        if (values != null) {
            for (String object : values) {
                result = result | Primitives.getInteger(object);
            }
        }
        return result;
    }

    /**
     * 判断bit位是非开启
     *
     * @param bit 判断位
     * @param value 判断值
     * @return 如果value中bit位为1 则为true
     */
    public static boolean isPowOn(int bit, int value) {
        return (bit & value) == bit;
    }

    /**
     * 开启指定bit位置
     *
     * @param bit
     * @param value
     * @return
     */
    public static int powOn(int bit, int value) {
        return bit | value;
    }

    /**
     * 断开指定位置
     *
     * @param bit
     * @param value
     * @return
     */
    public static int powOff(int bit, int value) {
        return ~bit & value;
    }

    /**
     * 从低位向高位与passNum比较，返回passNum为1并且value不为1的位
     * 如果都相等，返回null
     *
     * @param value
     * @param passNum
     * @param bit 比较的位数
     * @return
     */
    public static List<Integer> bitCompare(int value, int passNum, int bit) {
        int and = value & passNum;
        if (and == passNum) {
            return null;
        } else {
            List<Integer> result = new ArrayList<Integer>(3);
            int onPow;// 开关状态为1
            int passPow;// 同行的开关状态
            int pow;// 当前开关
            for (int i = 0; i < bit; i++) {
                onPow = 1 << i;
                passPow = passNum & onPow;
                pow = value & onPow;
                if (passPow == onPow && pow != onPow) {
                    result.add(onPow);
                }
            }
            return result.size() == 0 ? null : result;
        }
    }

}
