package org.xinhua.example.spring.mybatisplus.config;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.xinhua.example.spring.mybatisplus.handler.WechatResponseErrorHandler;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public RestTemplate wechatRestTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        restTemplate.setErrorHandler(new WechatResponseErrorHandler());
        return restTemplate;
    }

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer(MappingJackson2HttpMessageConverter jackson2HttpMessageConverter) {
        return restTemplate -> {
            List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
            messageConverters.removeIf(converter -> converter instanceof StringHttpMessageConverter);
            messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
            messageConverters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
            messageConverters.add(jackson2HttpMessageConverter);

            HttpClientBuilder clientBuilder = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy());
            ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(clientBuilder.build());
            restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(factory));
        };
    }

}
