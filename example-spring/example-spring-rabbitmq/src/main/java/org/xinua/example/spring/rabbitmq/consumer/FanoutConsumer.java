package org.xinua.example.spring.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.xinua.example.spring.rabbitmq.config.FanoutConfig;
import org.xinua.example.spring.rabbitmq.model.MsgBody;

@Component
public class FanoutConsumer {

    @RabbitListener(queues = {FanoutConfig.QUEUE_FANOUT_MSG1})
    public void process1(MsgBody msgBody) {
        System.out.println("process1===" + msgBody);
    }

    @RabbitListener(queues = {FanoutConfig.QUEUE_FANOUT_MSG2})
    public void process2(MsgBody msgBody) {
        System.out.println("process2===" + msgBody);
    }

    @RabbitListener(queues = {FanoutConfig.QUEUE_FANOUT_MSG3})
    public void process3(MsgBody msgBody) {
        System.out.println("process3===" + msgBody);
    }

}
