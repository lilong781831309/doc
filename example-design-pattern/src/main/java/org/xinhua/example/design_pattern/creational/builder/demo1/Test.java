package org.xinhua.example.design_pattern.creational.builder.demo1;

/**
 * @Author: lilong
 * @createDate: 2023/4/8 17:17
 * @Description: 测试
 * @Version: 1.0
 */
public class Test {

    private String name;

    public Test(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        MyStringBuilder sb = new MyStringBuilder();
        sb.append(1);
        sb.append("aaa");
        sb.append(345);
        System.out.println(sb.toString());
    }

}
