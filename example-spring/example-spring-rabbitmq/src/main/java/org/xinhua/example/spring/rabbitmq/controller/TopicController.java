package org.xinhua.example.spring.rabbitmq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xinhua.example.spring.rabbitmq.model.MsgBody;
import org.xinhua.example.spring.rabbitmq.producer.TopicProducer;

import java.util.Date;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    TopicProducer topicProducer;

    @GetMapping("/accurate")
    public Object msg1(String content) {
        topicProducer.sendAccurate(new MsgBody(content,new Date()));
        return "success";
    }

    @GetMapping("/single")
    public Object msg2(String content) {
        topicProducer.sendSingle(new MsgBody(content,new Date()));
        return "success";
    }

    @GetMapping("/any")
    public Object msg3(String content) {
        topicProducer.sendAny(new MsgBody(content,new Date()));
        return "success";
    }

}
