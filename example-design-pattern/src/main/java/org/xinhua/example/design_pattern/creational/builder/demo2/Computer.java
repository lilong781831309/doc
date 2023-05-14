package org.xinhua.example.design_pattern.creational.builder.demo2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: lilong
 * @createDate: 2023/4/8 22:12
 * @Description: 电脑
 * @Version: 1.0
 */

@Getter
@Setter
@ToString
public class Computer {

    public Computer(String cpu, String ram) {
        this.cpu = cpu;
        this.ram = ram;
    }

    private String cpu;//必须
    private String ram;//必须
    private int usbCount;//可选
    private String keyboard;//可选
    private String display;//可选


}
