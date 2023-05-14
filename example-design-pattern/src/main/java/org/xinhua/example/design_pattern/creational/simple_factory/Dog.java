package org.xinhua.example.design_pattern.creational.simple_factory;

/**
 * @Author: lilong
 * @createDate: 2023/4/6 21:53
 * @Description: 狗
 * @Version: 1.0
 */
public class Dog implements Animal {

    private String name = "狗";

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
        return "Dog{name='" + name + '}';
    }
}
