package org.xinhua.example.design_pattern.structural.proxy._static;

/**
 * @Author: lilong
 * @createDate: 2023/4/8 23:22
 * @Description: 代理类
 * @Version: 1.0
 */
public class Proxy {

    private TrainStation trainStation;

    public Proxy(TrainStation trainStation) {
        this.trainStation = trainStation;
    }

    public Ticket buyTicket() {
        System.out.println("代售点收手续费");
        Ticket ticket = trainStation.buyTicket();
        System.out.println("代售点放票");
        return ticket;
    }
}
