package org.xinhua.example.rabbitmq.topic;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xinhua.example.rabbitmq.MyDeliverCallback;
import org.xinhua.example.rabbitmq.Util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class OrderLogConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderLogConsumer.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Util.getChannel();

        String QUEUE = "queue.test.topic.log.order";

        channel.queueDeclare(QUEUE, true, false, false, null);

        CancelCallback cancelCallback = consumerTag -> logger.info("cancel callback , {}", consumerTag);

        channel.basicConsume(QUEUE, false, new MyDeliverCallback(channel), cancelCallback);
    }

}
