package org.xinhua.example.rabbitmq.direct;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.xinhua.example.rabbitmq.MyConfirmListener;
import org.xinhua.example.rabbitmq.MyReturnListener;
import org.xinhua.example.rabbitmq.Util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Util.getChannel();

        String EXCHANGE = "exchange.test.direct";
        String QUEUE = "queue.test.direct";
        String ROUTING_KEY = "routingKey.test.direct";

        BuiltinExchangeType type = BuiltinExchangeType.DIRECT;
        boolean durable = true;
        boolean autoDelete = false;
        boolean internal = false;//内部使用
        boolean exclusive = false;//1.独占连接,只能有一个消费者。2.connection关闭时,是否删除队列
        Map<String, Object> arguments = null;

        channel.exchangeDeclare(EXCHANGE, type, durable, autoDelete, internal, arguments);
        channel.queueDeclare(QUEUE, durable, exclusive, autoDelete, arguments);
        channel.queueBind(QUEUE, EXCHANGE, ROUTING_KEY);

        channel.confirmSelect();
        channel.addConfirmListener(new MyConfirmListener());

        channel.addReturnListener(new MyReturnListener());

        boolean mandatory = true;//无法投递到队列
        AMQP.BasicProperties props = MessageProperties.PERSISTENT_TEXT_PLAIN;
        String message = "org.xinhua.example.rabbitmq message...";

        for (int i = 0; i < 1000; i++) {
            byte[] bytes = (message + i).getBytes();
            channel.basicPublish(EXCHANGE, ROUTING_KEY, mandatory, false, props, bytes);
            Util.sleep(1000L);
        }

    }

}
