package org.xinhua.example.design_pattern.creational.simple_factory;

/**
 * @Author: lilong
 * @createDate: 2023/4/6 21:53
 * @Description: 猫
 * @Version: 1.0
 */
public class Cat implements Animal {

    private String name = "猫";

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void eat() {
    }

    @Override
    public void sleep() {
    }

    @Override
    public String toString() {
        return "Cat{" + "name='" + name + '\'' + '}';
    }
}
