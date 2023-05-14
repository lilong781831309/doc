package org.xinhua.example.design_pattern.creational.simple_factory;

/**
 * @Author: lilong
 * @createDate: 2023/4/6 21:56
 * @Description:
 * @Version: 1.0
 */
public class Test {
    public static void main(String[] args) {
        BeanFactory beanFactory = new SimpleBeanFactory();
        User user = (User) beanFactory.getBean("user");
        Animal animal = (Animal) beanFactory.getBean("animal");

        System.out.println(user);
        System.out.println(animal);
    }
}
