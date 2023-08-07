package org.xinhua.example.spring.mybatisplus.model.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * string	获取到的凭证
     */
    private String access_token;

    /**
     * number	凭证有效时间，单位：秒。目前是7200秒之内的值。
     */
    private Integer expires_in;
}
