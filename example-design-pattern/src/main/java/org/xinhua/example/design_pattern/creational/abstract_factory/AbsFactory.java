package org.xinhua.example.design_pattern.creational.abstract_factory;

/**
 * @Author: lilong
 * @createDate: 2023/4/7 17:39
 * @Description: 装饰品工厂  抽象工厂
 *               生产 多个 类型
 * @Version: 1.0
 */
public interface AbsFactory {

    Clothes createClothes();

    Pants createPants();

    Shoe createShoe();
}
