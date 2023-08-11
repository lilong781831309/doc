package org.xinhua.example.spring.rest.template.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.xinhua.example.spring.rest.template.handler.LoggingRequestInterceptor;
import org.xinhua.example.spring.rest.template.handler.WechatResponseErrorHandler;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer(MappingJackson2HttpMessageConverter jackson2HttpMessageConverter) {
        return restTemplate -> {
            List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
            messageConverters.removeIf(converter -> converter instanceof StringHttpMessageConverter);
            messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
            messageConverters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
            messageConverters.add(jackson2HttpMessageConverter);
            restTemplate.setRequestFactory(bufferingClientHttpRequestFactory());
        };
    }

    @Bean
    public BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory() {
        //不处理重定向
        //HttpClient httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
        //允许多次重定向
        HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(15000);
        factory.setConnectTimeout(15000);
        return new BufferingClientHttpRequestFactory(factory);
    }

    @Primary
    @Bean("restTemplate")
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean("loggingRestTemplate")
    public RestTemplate loggingRestTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        restTemplate.getInterceptors().add(new LoggingRequestInterceptor());
        return restTemplate;
    }

    @Bean("wechatRestTemplate")
    public RestTemplate wechatRestTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        restTemplate.setErrorHandler(new WechatResponseErrorHandler());
        restTemplate.getInterceptors().add(new LoggingRequestInterceptor());
        return restTemplate;
    }

}