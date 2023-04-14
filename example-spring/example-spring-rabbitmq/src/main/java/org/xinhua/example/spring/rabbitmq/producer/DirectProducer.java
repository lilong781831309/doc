package org.xinhua.example.spring.rabbitmq.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xinhua.example.spring.rabbitmq.config.DirectConfig;
import org.xinhua.example.spring.rabbitmq.model.MsgBody;

@Component
public class DirectProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send1(MsgBody msgBody) {
        rabbitTemplate.convertAndSend(DirectConfig.EXCHANGE_DIRECT_MSG, DirectConfig.ROUTING_KEY_MSG1, msgBody);
    }

    public void send2(MsgBody msgBody) {
        rabbitTemplate.convertAndSend(DirectConfig.EXCHANGE_DIRECT_MSG, DirectConfig.ROUTING_KEY_MSG2, msgBody);
    }

    public void send3(MsgBody msgBody) {
        rabbitTemplate.convertAndSend(DirectConfig.EXCHANGE_DIRECT_MSG, DirectConfig.ROUTING_KEY_MSG3, msgBody);
    }

}
