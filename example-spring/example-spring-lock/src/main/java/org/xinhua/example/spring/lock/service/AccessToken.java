package org.xinhua.example.spring.lock.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class AccessToken {

	private String access_token;

	//有效期(有效期2小时) 从1970 算起的毫秒数
	private long expires_in;

}
