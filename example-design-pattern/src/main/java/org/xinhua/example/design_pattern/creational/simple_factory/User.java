package org.xinhua.example.design_pattern.creational.simple_factory;

/**
 * @Author: lilong
 * @createDate: 2023/4/6 21:54
 * @Description: 用户
 * @Version: 1.0
 */
public class User {

    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{name='" + name + ", address='" + address + '}';
    }
}
