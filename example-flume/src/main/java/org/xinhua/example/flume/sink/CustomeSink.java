package org.xinhua.example.flume.sink;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class CustomeSink extends AbstractSink implements Configurable {

    private static final Logger logger = LoggerFactory.getLogger(CustomeSink.class);

    @Override
    public Status process() throws EventDeliveryException {
        Channel ch = getChannel();
        Transaction txn = ch.getTransaction();
        txn.begin();
        try {
            Event event = ch.take();
            storeSomeData(event);
            txn.commit();
            return Status.READY;
        } catch (Throwable t) {
            txn.rollback();
            return Status.BACKOFF;
        } finally{
            txn.close();
        }
    }

    private void storeSomeData(Event event) {
        String printData  = event.getHeaders() + " ::: "+ new String(event.getBody(), StandardCharsets.UTF_8);
        logger.info(printData);
    }

    @Override
    public void configure(Context context) {

    }

}
