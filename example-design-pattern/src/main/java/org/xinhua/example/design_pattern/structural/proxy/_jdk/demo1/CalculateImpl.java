package org.xinhua.example.design_pattern.structural.proxy._jdk.demo1;

/**
 * @Author: lilong
 * @createDate: 2023/4/9 16:15
 * @Description: 实现类
 * @Version: 1.0
 */
public class CalculateImpl implements Calculate {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int sub(int a, int b) {
        return a - b;
    }

    @Override
    public int mul(int a, int b) {
        return a * b;
    }

    @Override
    public int div(int a, int b) {
        return a / b;
    }
}
