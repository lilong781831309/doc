package org.xinhua.example.design_pattern.creational.factory_method.demo1;

/**
 * @Author: lilong
 * @createDate: 2023/4/7 17:16
 * @Description: 美团自行车工厂
 * @Version: 1.0
 */
public class MeiTuanBikeFactory implements BikeFactory {
    @Override
    public Bike create() {
        return new MeiTuanBike();
    }

    public class MeiTuanBike implements Bike {

        @Override
        public void driver() {
            System.out.println("美团自行车出行");
        }

    }

}
