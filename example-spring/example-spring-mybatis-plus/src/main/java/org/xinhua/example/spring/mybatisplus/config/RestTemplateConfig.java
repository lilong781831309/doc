package org.xinhua.example.spring.mybatisplus.config;

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
import org.xinhua.example.spring.mybatisplus.handler.WechatResponseErrorHandler;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		HttpClientBuilder clientBuilder = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy());
		ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(clientBuilder.build());

		MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		jackson2HttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));

		List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
		messageConverters.removeIf(converter -> converter instanceof StringHttpMessageConverter);
		messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
		messageConverters.add(jackson2HttpMessageConverter);

		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(factory));

		restTemplate.setMessageConverters(messageConverters);

		//设置ErrorHandler
		restTemplate.setErrorHandler(new WechatResponseErrorHandler());

		return restTemplate;
	}

}
