package org.xinhua.example.rabbitmq.direct;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xinhua.example.rabbitmq.MyDeliverCallback;
import org.xinhua.example.rabbitmq.Util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Util.getChannel();

        String QUEUE = "queue.test.direct";

        boolean durable = true;
        boolean autoDelete = false;
        boolean exclusive = false;//独占
        Map<String, Object> arguments = null;

        channel.queueDeclare(QUEUE, durable, exclusive, autoDelete, arguments);

        boolean autoAck = false;
        CancelCallback cancelCallback = consumerTag -> logger.warn("cancel callback , consumerTag : {}", consumerTag);

        channel.basicConsume(QUEUE, autoAck, new MyDeliverCallback(channel), cancelCallback);
    }

}
