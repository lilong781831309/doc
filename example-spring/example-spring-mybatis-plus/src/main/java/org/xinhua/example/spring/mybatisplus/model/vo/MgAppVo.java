package org.xinhua.example.spring.mybatisplus.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel
public class MgAppVo extends BaseVo {

    @ApiModelProperty("app_id")
    @JsonProperty("app_id")
    private String appId;

    @ApiModelProperty("app_secret")
    @JsonProperty("app_secret")
    private String appSecret;

    @ApiModelProperty("access_token")
    @JsonProperty("access_token")
    private String accessToken;

    @ApiModelProperty("过期时间")
    @JsonProperty("expires_time")
    private LocalDateTime expiresTime;

}
