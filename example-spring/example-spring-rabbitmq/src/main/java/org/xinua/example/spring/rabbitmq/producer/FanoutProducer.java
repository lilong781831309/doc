package org.xinua.example.spring.rabbitmq.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xinua.example.spring.rabbitmq.config.FanoutConfig;
import org.xinua.example.spring.rabbitmq.model.MsgBody;

@Component
public class FanoutProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(MsgBody msgBody) {
        rabbitTemplate.convertAndSend(FanoutConfig.EXCHANGE_FANOUT_MSG, "", msgBody);
    }

}
