package org.xinhua.example.design_pattern.creational.abstract_factory;

/**
 * @Author: lilong
 * @createDate: 2023/4/7 17:34
 * @Description: 阿迪达斯工厂     具体工厂
 *              生产 阿迪达斯 产品族
 *              包括多个类型具体产品 AdidasClothes AdidasPants AdidasShoe
 * @Version: 1.0
 */
public class AdidasFactory implements AbsFactory {

    @Override
    public Clothes createClothes() {
        return new AdidasClothes();
    }

    @Override
    public Pants createPants() {
        return new AdidasPants();
    }

    @Override
    public Shoe createShoe() {
        return new AdidasShoe();
    }

    public class AdidasClothes implements Clothes {
        @Override
        public String getClothesName() {
            return "阿迪达斯衣服";
        }
    }

    public class AdidasPants implements Pants {
        @Override
        public String getPantsName() {
            return "阿迪达斯裤子";
        }
    }

    public class AdidasShoe implements Shoe {
        @Override
        public String getShoeName() {
            return "阿迪达斯鞋";
        }
    }


}
