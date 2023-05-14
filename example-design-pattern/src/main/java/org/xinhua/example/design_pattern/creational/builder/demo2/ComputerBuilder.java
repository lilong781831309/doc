package org.xinhua.example.design_pattern.creational.builder.demo2;

/**
 * @Author: lilong
 * @createDate: 2023/4/8 22:12
 * @Description: 电脑建造者
 * @Version: 1.0
 */
public interface ComputerBuilder {

    ComputerBuilder setUsbCount(int usbCount);

    ComputerBuilder setKeyboard(String keyboard);

    ComputerBuilder setDisplay(String display);

    Computer build();

}
