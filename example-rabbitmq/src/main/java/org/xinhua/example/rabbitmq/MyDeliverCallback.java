package org.xinhua.example.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MyDeliverCallback implements DeliverCallback {

    private static final Logger logger = LoggerFactory.getLogger(MyDeliverCallback.class);
    private Channel channel;

    public MyDeliverCallback(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void handle(String consumerTag, Delivery delivery) throws IOException {
        logger.warn("deliver callback , consumerTag : {}", consumerTag);
        logger.warn("deliver callback , Delivery._envelope : {}", delivery.getEnvelope());
        logger.warn("deliver callback , Delivery._properties : {}", delivery.getProperties());
        logger.warn("deliver callback , Delivery._body : {}", new String(delivery.getBody()));
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }
}
