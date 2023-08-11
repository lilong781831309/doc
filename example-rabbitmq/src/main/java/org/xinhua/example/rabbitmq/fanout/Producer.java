package org.xinhua.example.rabbitmq.fanout;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.xinhua.example.rabbitmq.MyConfirmListener;
import org.xinhua.example.rabbitmq.MyReturnListener;
import org.xinhua.example.rabbitmq.Util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Util.getChannel();

        String EXCHANGE = "exchange.test.fanout";
        String ROUTING_KEY = "routingKey.test.fanout";

        BuiltinExchangeType type = BuiltinExchangeType.FANOUT;
        channel.exchangeDeclare(EXCHANGE, type, true, false, false, null);

        channel.confirmSelect();
        channel.addConfirmListener(new MyConfirmListener());
        channel.addReturnListener(new MyReturnListener());

        AMQP.BasicProperties props = MessageProperties.PERSISTENT_TEXT_PLAIN;
        String message = "fanout message...";

        for (int i = 0; i < 1000; i++) {
            byte[] bytes = (message + i).getBytes();
            channel.basicPublish(EXCHANGE, ROUTING_KEY, true, false, props, bytes);
            Util.sleep(1000L);
        }
    }

}
