package org.xinhua.example.flume.source;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class CustomeSource extends AbstractSource implements Configurable, PollableSource {

    private Long delay;
    private String prefix;

    @Override
    public Status process() throws EventDeliveryException {
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
            Event event = getSomeData();
            getChannelProcessor().processEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return Status.BACKOFF;
        }
        return Status.READY;
    }

    private Event getSomeData() {
        String data  = UUID.randomUUID().toString();

        SimpleEvent event = new SimpleEvent();
        event.getHeaders().put("prefix", prefix);
        event.setBody((prefix + data).getBytes(StandardCharsets.UTF_8));

        return event ;
    }

    @Override
    public long getBackOffSleepIncrement() {
        return 0;
    }

    @Override
    public long getMaxBackOffSleepInterval() {
        return 0;
    }

    @Override
    public void configure(Context context) {
        delay = context.getLong("delay", 0L);
        prefix = context.getString("prefix", "log-");
    }

}
