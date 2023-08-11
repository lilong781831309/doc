package org.xinhua.example.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ReturnListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MyReturnListener implements ReturnListener {

    private static final Logger logger = LoggerFactory.getLogger(MyReturnListener.class);

    @Override
    public void handleReturn(int replyCode, String replyText, String exchange, String routingKey,
                             AMQP.BasicProperties properties, byte[] body) throws IOException {
        logger.warn("return listener , replyCode : {}", replyCode);
        logger.warn("return listener , replyText : {}", replyText);
        logger.warn("return listener , exchange : {}", exchange);
        logger.warn("return listener , routingKey : {}", routingKey);
        logger.warn("return listener , properties : {}", properties);
        logger.warn("return listener , body : {}", new String(body));
    }
}
