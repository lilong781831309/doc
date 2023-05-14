package org.xinhua.example.design_pattern.creational.builder.demo2;

/**
 * @Author: lilong
 * @createDate: 2023/4/8 22:26
 * @Description: 电脑建造指挥者
 * @Version: 1.0
 */
public class ComputerDirector {

    private ComputerBuilder builder;

    public ComputerDirector(ComputerBuilder builder) {
        this.builder = builder;
    }

    public Computer createComputer() {
        System.out.println("指挥者 指挥建造者将电脑部件摆放好");
        Computer computer = builder.build();
        System.out.println("指挥者 指挥建造者完成收尾工作");
        return computer;
    }

}
