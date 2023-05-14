package org.xinhua.example.design_pattern.structural.proxy._static;

/**
 * @Author: lilong
 * @createDate: 2023/4/8 23:25
 * @Description: 火车站
 * @Version: 1.0
 */
public class TrainStation {

    public Ticket buyTicket() {
        System.out.println("火车站买票");
        return new Ticket();
    }

}
