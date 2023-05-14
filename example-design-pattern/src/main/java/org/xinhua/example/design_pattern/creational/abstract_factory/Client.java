package org.xinhua.example.design_pattern.creational.abstract_factory;

/**
 * @Author: lilong
 * @createDate: 2023/4/7 17:49
 * @Description: TODO
 * @Version: 1.0
 */
public class Client {

    public static void main(String[] args) {
        //AbsFactory factory = new AdidasFactory();
        AbsFactory factory = new NikeFactory();

        Clothes clothes = factory.createClothes();
        Pants pants = factory.createPants();
        Shoe shoe = factory.createShoe();

        System.out.println(clothes.getClothesName());
        System.out.println(pants.getPantsName());
        System.out.println(shoe.getShoeName());
    }
}
