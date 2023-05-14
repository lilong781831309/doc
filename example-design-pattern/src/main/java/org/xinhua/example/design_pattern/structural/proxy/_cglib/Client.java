package org.xinhua.example.design_pattern.structural.proxy._cglib;

/**
 * @Author: lilong
 * @createDate: 2023/4/9 16:37
 * @Description: 测试
 * @Version: 1.0
 */
public class Client {

    public static void main(String[] args) {
        //Calculate calculate = new CalculateImpl();
        Calculate calculate = LogProxy.newProxyInstance(new CalculateImpl());

        calculate.add(1, 2);
        calculate.sub(3, 4);
        calculate.mul(5, 6);
        calculate.div(20, 5);
        calculate.div(20, 0);

    }
}
