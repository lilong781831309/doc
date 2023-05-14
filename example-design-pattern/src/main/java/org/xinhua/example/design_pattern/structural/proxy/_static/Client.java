package org.xinhua.example.design_pattern.structural.proxy._static;

/**
 * @Author: lilong
 * @createDate: 2023/4/8 23:29
 * @Description: 测试
 * @Version: 1.0
 */
public class Client {

    public static void main(String[] args) {
        Proxy proxy = new Proxy(new TrainStation());
        Ticket ticket = proxy.buyTicket();
    }
}
