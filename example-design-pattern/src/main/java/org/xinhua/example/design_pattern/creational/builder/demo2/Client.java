package org.xinhua.example.design_pattern.creational.builder.demo2;

/**
 * @Author: lilong
 * @createDate: 2023/4/8 22:29
 * @Description: 客户端
 * @Version: 1.0
 */
public class Client {

    public static void main(String[] args) {

        ComputerBuilder builder = new MacComputerBuilder("苹果cpu", "苹果内存")
                .setUsbCount(8)
                .setKeyboard("苹果键盘")
                .setDisplay("苹果显示器");

        ComputerBuilder builder2 = new MacComputerBuilder("联想cpu", "联想内存")
                .setUsbCount(6)
                .setKeyboard("联想键盘")
                .setDisplay("联想显示器");

        ComputerDirector director = new ComputerDirector(builder2);
        Computer computer = director.createComputer();
        System.out.println(computer);

    }

}
