package org.xinua.example.spring.rest.template.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.xinua.example.spring.rest.template.handler.LoggingRequestInterceptor;
import org.xinua.example.spring.rest.template.handler.WechatResponseErrorHandler;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean("restTemplate")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(myMessageConverters());
        restTemplate.setRequestFactory(bufferingClientHttpRequestFactory());
        return restTemplate;
    }

    @Bean("loggingRestTemplate")
    public RestTemplate loggingRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(myMessageConverters());
        restTemplate.setRequestFactory(bufferingClientHttpRequestFactory());
        //设置ClientHttpRequestInterceptor
        restTemplate.getInterceptors().add(loggingRequestInterceptor());
        return restTemplate;
    }

    @Bean("wechatRestTemplate")
    public RestTemplate wechatRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(myMessageConverters());
        restTemplate.setRequestFactory(bufferingClientHttpRequestFactory());
        //设置ErrorHandler
        restTemplate.setErrorHandler(new WechatResponseErrorHandler());
        //设置ClientHttpRequestInterceptor
        restTemplate.getInterceptors().add(loggingRequestInterceptor());
        return restTemplate;
    }

    @Bean
    public List<HttpMessageConverter<?>> myMessageConverters() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        // 使用 utf-8 编码集的 conver 替换默认的 conver（默认的 string conver 的编码集为"ISO-8859-1"）
        messageConverters.removeIf(converter -> converter instanceof StringHttpMessageConverter);
        messageConverters.add(stringHttpMessageConverter());
        messageConverters.add(jackson2HttpMessageConverter());
        return messageConverters;
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        return jackson2HttpMessageConverter;
    }

    @Bean
    public ClientHttpRequestFactory bufferingClientHttpRequestFactory() {
        return new BufferingClientHttpRequestFactory(clientHttpRequestFactory());
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        //不处理重定向
        //HttpClient httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
        //允许多次重定向
        HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(15000);
        factory.setConnectTimeout(15000);

        return factory;
    }

    @Bean
    public LoggingRequestInterceptor loggingRequestInterceptor() {
        return new LoggingRequestInterceptor();
    }

}