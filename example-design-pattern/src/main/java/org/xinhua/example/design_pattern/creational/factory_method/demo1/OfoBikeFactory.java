package org.xinhua.example.design_pattern.creational.factory_method.demo1;

/**
 * @Author: lilong
 * @createDate: 2023/4/7 17:16
 * @Description: ofo自行车工厂
 * @Version: 1.0
 */
public class OfoBikeFactory implements BikeFactory {
    @Override
    public Bike create() {
        return new OfoBike();
    }

    public class OfoBike implements Bike {

        @Override
        public void driver() {
            System.out.println("ofo自行车出行");
        }

    }

}
