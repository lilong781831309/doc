package org.xinhua.example.rabbitmq;

import com.rabbitmq.client.ConfirmListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MyConfirmListener implements ConfirmListener {

    private static final Logger logger = LoggerFactory.getLogger(MyConfirmListener.class);

    @Override
    public void handleAck(long deliveryTag, boolean multiple) throws IOException {
        logger.info("confirm listener ack , deliveryTag : {}", deliveryTag);
        logger.info("confirm listener ack , multiple : {}", multiple);
    }

    @Override
    public void handleNack(long deliveryTag, boolean multiple) throws IOException {
        logger.warn("confirm listener nack , deliveryTag : {}", deliveryTag);
        logger.warn("confirm listener nack , multiple : {}", multiple);
    }
}
