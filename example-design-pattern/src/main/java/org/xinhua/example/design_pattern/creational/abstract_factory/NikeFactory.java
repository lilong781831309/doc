package org.xinhua.example.design_pattern.creational.abstract_factory;

/**
 * @Author: lilong
 * @createDate: 2023/4/7 17:34
 * @Description: 耐克工厂   具体工厂
 *               生产 耐克  产品族
 *               包括多个类型具体产品 NikeClothes NikePants NikeShoe
 * @Version: 1.0
 */
public class NikeFactory implements AbsFactory {


    @Override
    public Clothes createClothes() {
        return new NikeClothes();
    }

    @Override
    public Pants createPants() {
        return new NikePants();
    }

    @Override
    public Shoe createShoe() {
        return new NikeShoe();
    }


    public class NikeClothes implements Clothes {
        @Override
        public String getClothesName() {
            return "耐克衣服";
        }
    }

    public class NikePants implements Pants {
        @Override
        public String getPantsName() {
            return "耐克裤子";
        }
    }

    public class NikeShoe implements Shoe {
        @Override
        public String getShoeName() {
            return "耐克鞋";
        }
    }

}
