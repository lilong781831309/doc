package org.xinhua.example.spring.mybatisplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel
public class MgAppDTO extends BaseDTO {

    @ApiModelProperty(value = "app_id", required = true)
    @JsonProperty("app_id")
    private String appId;

    @ApiModelProperty(value = "app_secret", required = true)
    @JsonProperty("app_secret")
    private String appSecret;

    @ApiModelProperty("access_token")
    @JsonProperty("access_token")
    private String accessToken;

    @ApiModelProperty("过期时间")
    @JsonProperty("expires_time")
    private LocalDateTime expiresTime;

}
