package org.xinua.example.spring.rabbitmq.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xinua.example.spring.rabbitmq.config.TopicConfig;
import org.xinua.example.spring.rabbitmq.model.MsgBody;

@Component
public class TopicProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendAccurate(MsgBody msgBody) {
        rabbitTemplate.convertAndSend(TopicConfig.EXCHANGE_TOPIC_MSG, TopicConfig.ROUTING_KEY_MSG_ACCURATE, msgBody);
    }

    public void sendSingle(MsgBody msgBody) {
        rabbitTemplate.convertAndSend(TopicConfig.EXCHANGE_TOPIC_MSG, TopicConfig.ROUTING_KEY_MSG_SINGLE, msgBody);
    }

    public void sendAny(MsgBody msgBody) {
        rabbitTemplate.convertAndSend(TopicConfig.EXCHANGE_TOPIC_MSG, TopicConfig.ROUTING_KEY_MSG_ANY, msgBody);
    }

}
