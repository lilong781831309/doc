package org.xinhua.example.design_pattern.creational.builder.demo1;

/**
 * @Author: lilong
 * @createDate: 2023/4/8 22:00
 * @Description: String 构建者
 * @Version: 1.0
 */
public class MyStringBuilder {

    private String value = "";

    public MyStringBuilder append(byte b) {
        value += String.valueOf(b);
        return this;
    }

    public MyStringBuilder append(char c) {
        value += String.valueOf(c);
        return this;
    }

    public MyStringBuilder append(String str) {
        value += ((str == null) ? "null" : str);
        return this;
    }

    public MyStringBuilder append(int i) {
        value += String.valueOf(i);
        return this;
    }

    public MyStringBuilder append(long l) {
        value += String.valueOf(l);
        return this;
    }

    public MyStringBuilder append(Object object) {
        value += String.valueOf(object);
        return this;
    }

    public String toString() {
        return value;
    }

}
