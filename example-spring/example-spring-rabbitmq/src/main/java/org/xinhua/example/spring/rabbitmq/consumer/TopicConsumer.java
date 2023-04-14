package org.xinhua.example.spring.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.xinhua.example.spring.rabbitmq.config.TopicConfig;
import org.xinhua.example.spring.rabbitmq.model.MsgBody;

@Component
public class TopicConsumer {

    @RabbitListener(queues = {TopicConfig.QUEUE_TOPIC_MSG_ACCURATE})
    public void processAccurate(MsgBody msgBody) {
        System.out.println("accurate===" + msgBody);
    }

    @RabbitListener(queues = {TopicConfig.QUEUE_TOPIC_MSG_SINGLE})
    public void processSingle(MsgBody msgBody) {
        System.out.println("single===" + msgBody);
    }

    @RabbitListener(queues = {TopicConfig.QUEUE_TOPIC_MSG_ANY})
    public void processAny(MsgBody msgBody) {
        System.out.println("any===" + msgBody);
    }

}
