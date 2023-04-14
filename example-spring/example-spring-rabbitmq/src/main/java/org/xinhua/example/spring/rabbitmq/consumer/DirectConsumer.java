package org.xinhua.example.spring.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.xinhua.example.spring.rabbitmq.config.DirectConfig;
import org.xinhua.example.spring.rabbitmq.model.MsgBody;

import java.io.IOException;
import java.util.Map;

@Component
public class DirectConsumer {

    @RabbitListener(queues = {DirectConfig.QUEUE_DIRECT_MSG1})
    public void process1(@Headers Map<String, Object> headers, @Payload MsgBody msgBody, Message message, Channel channel) {
        try {
            System.out.println("process1===headers===" + headers);
            System.out.println("process1===msgBody===" + msgBody);
            System.out.println("process1===message===" + message);

            long deliveryTag =  message.getMessageProperties().getDeliveryTag();
            channel.basicAck(deliveryTag,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = {DirectConfig.QUEUE_DIRECT_MSG2})
    public void process2(@Headers Map<String, Object> headers, @Payload MsgBody msgBody, Message message, Channel channel) {
        System.out.println("process2===msgBody===" + msgBody);
    }

    @RabbitListener(queues = {DirectConfig.QUEUE_DIRECT_MSG3})
    public void process3(@Headers Map<String, Object> headers, @Payload MsgBody msgBody, Message message, Channel channel) {
        System.out.println("process3===msgBody===" + msgBody);
    }

}
