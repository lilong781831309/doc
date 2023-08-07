package org.xinhua.example.spring.mybatisplus.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class WechatApiConfig {

	@Value("${wechat.url.token}")
	private String token;

	@Value("${wechat.url.getmedialink}")
	private String getmedialink;

    @Value("${wechat.url.listmedia}")
    private String listmedia;

}
