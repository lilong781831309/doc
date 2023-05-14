package org.xinhua.example.design_pattern.creational.builder.demo2;

/**
 * @Author: lilong
 * @createDate: 2023/4/8 22:15
 * @Description: 苹果电脑建造者
 * @Version: 1.0
 */
public class MacComputerBuilder implements ComputerBuilder {
    private Computer computer;

    public MacComputerBuilder(String cpu, String ram) {
        computer = new Computer(cpu, ram);
    }

    @Override
    public ComputerBuilder setUsbCount(int usbCount) {
        computer.setUsbCount(usbCount);
        return this;
    }

    @Override
    public ComputerBuilder setKeyboard(String keyboard) {
        computer.setKeyboard(keyboard);
        return this;
    }

    @Override
    public ComputerBuilder setDisplay(String display) {
        computer.setDisplay(display);
        return this;
    }

    @Override
    public Computer build() {
        System.out.println("建造苹果电脑开始");
        System.out.println("建造苹果电脑完成");
        return computer;
    }

}
