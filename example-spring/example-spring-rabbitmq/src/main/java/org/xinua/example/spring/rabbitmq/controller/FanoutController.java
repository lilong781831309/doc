package org.xinua.example.spring.rabbitmq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xinua.example.spring.rabbitmq.model.MsgBody;
import org.xinua.example.spring.rabbitmq.producer.FanoutProducer;

import java.util.Date;

@RestController
@RequestMapping("/fanout")
public class FanoutController {

    @Autowired
    FanoutProducer fanoutProducer;

    @GetMapping("/msg")
    public Object msg(String content) {
        fanoutProducer.send(new MsgBody(content,new Date()));
        return "success";
    }

}
