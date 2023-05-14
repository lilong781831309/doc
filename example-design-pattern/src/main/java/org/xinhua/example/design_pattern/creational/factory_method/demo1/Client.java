package org.xinhua.example.design_pattern.creational.factory_method.demo1;

/**
 * @Author: lilong
 * @createDate: 2023/4/7 17:23
 * @Description: 测试
 * @Version: 1.0
 */
public class Client {

    public static void main(String[] args) {
        BikeFactory factory = new MeiTuanBikeFactory();
        //BikeFactory factory = new OfoBikeFactory();
        Bike bike = factory.create();
        bike.driver();
    }

}
