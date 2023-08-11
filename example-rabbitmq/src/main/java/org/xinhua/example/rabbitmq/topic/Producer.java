package org.xinhua.example.rabbitmq.topic;

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

        String EXCHANGE = "exchange.test.topic";
        String QUEUE_LOG_ORDER = "queue.test.topic.log.order";
        String QUEUE_LOG_ERROR = "queue.test.topic.log.error";
        String QUEUE_LOG_OTHER = "queue.test.topic.log.other";

        BuiltinExchangeType type = BuiltinExchangeType.TOPIC;
        channel.exchangeDeclare(EXCHANGE, type, true, false, false, null);

        channel.queueDeclare(QUEUE_LOG_ORDER, true, false, false, null);
        channel.queueDeclare(QUEUE_LOG_ERROR, true, false, false, null);
        channel.queueDeclare(QUEUE_LOG_OTHER, true, false, false, null);

        channel.queueBind(QUEUE_LOG_ORDER, EXCHANGE, "order.#");
        channel.queueBind(QUEUE_LOG_ERROR, EXCHANGE, "#.error");
        channel.queueBind(QUEUE_LOG_OTHER, EXCHANGE, "#.info");
        channel.queueBind(QUEUE_LOG_OTHER, EXCHANGE, "#.warn");
        channel.queueBind(QUEUE_LOG_OTHER, EXCHANGE, "#.debug");

        channel.confirmSelect();
        channel.addConfirmListener(new MyConfirmListener());
        channel.addReturnListener(new MyReturnListener());

        AMQP.BasicProperties props = MessageProperties.PERSISTENT_TEXT_PLAIN;

        new LogTask("order add info detail ", channel, EXCHANGE, "order.add.info", props).start();
        new LogTask("order add warn detail ", channel, EXCHANGE, "order.add.warn", props).start();
        new LogTask("order update debug detail ", channel, EXCHANGE, "order.update.debug", props).start();
        new LogTask("order update warn detail ", channel, EXCHANGE, "order.update.warn", props).start();
        new LogTask("order update error detail ", channel, EXCHANGE, "order.update.error", props).start();
        new LogTask("user update info detail ", channel, EXCHANGE, "user.update.info", props).start();
        new LogTask("user update error detail ", channel, EXCHANGE, "user.update.error", props).start();
        new LogTask("user update warn detail ", channel, EXCHANGE, "user.update.warn", props).start();
    }

    static class LogTask extends Thread {
        String logDetail;
        Channel channel;
        String exchange;
        String routingKey;
        AMQP.BasicProperties props;

        public LogTask(String logDetail, Channel channel, String exchange, String routingKey, AMQP.BasicProperties props) {
            this.logDetail = logDetail;
            this.channel = channel;
            this.exchange = exchange;
            this.routingKey = routingKey;
            this.props = props;
        }

        @Override
        public void run() {
            int i = 1;
            while (true) {
                byte[] bytes = (logDetail + i).getBytes();
                try {
                    channel.basicPublish(exchange, routingKey, true, false, props, bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Util.sleep(1000L);
                i++;
            }
        }
    }

}
