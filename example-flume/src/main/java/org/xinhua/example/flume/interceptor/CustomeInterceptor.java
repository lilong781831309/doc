package org.xinhua.example.flume.interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class CustomeInterceptor implements Interceptor {

    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        String body = new String(event.getBody(), StandardCharsets.UTF_8);
        if (body.startsWith("lilong")) {
            event.getHeaders().put("state", "zh");
        } else if (body.startsWith("long li")) {
            event.getHeaders().put("state", "en");
        }else{
            event.getHeaders().put("state", "default");
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        return null;
    }

    @Override
    public void close() {

    }

    public static class MyBuilder implements Builder {

        @Override
        public Interceptor build() {
            return new CustomeInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
