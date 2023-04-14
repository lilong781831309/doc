package org.xinhua.example.spring.rabbitmq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xinhua.example.spring.rabbitmq.model.MsgBody;
import org.xinhua.example.spring.rabbitmq.producer.DirectProducer;

import java.util.Date;

@RestController
@RequestMapping("/direct")
public class DirectController {

    @Autowired
    DirectProducer directProducer;

    @GetMapping("/msg1")
    public Object msg1(String content) {
        directProducer.send1(new MsgBody(content,new Date()));
        return "success";
    }

    @GetMapping("/msg2")
    public Object msg2(String content) {
        directProducer.send2(new MsgBody(content,new Date()));
        return "success";
    }

    @GetMapping("/msg3")
    public Object msg3(String content) {
        directProducer.send3(new MsgBody(content,new Date()));
        return "success";
    }

}
