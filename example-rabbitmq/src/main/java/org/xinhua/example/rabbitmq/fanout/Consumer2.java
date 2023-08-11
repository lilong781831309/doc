package org.xinhua.example.rabbitmq.fanout;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xinhua.example.rabbitmq.MyDeliverCallback;
import org.xinhua.example.rabbitmq.Util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer2 {

    private static final Logger logger = LoggerFactory.getLogger(Consumer2.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Util.getChannel();

        String EXCHANGE = "exchange.test.fanout";
        String QUEUE = "queue.test.fanout2";
        String ROUTING_KEY = "routingKey.test.fanout";

        channel.queueDeclare(QUEUE, true, false, false, null);
        channel.queueBind(QUEUE, EXCHANGE, ROUTING_KEY);

        CancelCallback cancelCallback = consumerTag -> logger.info("cancel callback , {}", consumerTag);

        channel.basicConsume(QUEUE, false, new MyDeliverCallback(channel), cancelCallback);
    }

}
