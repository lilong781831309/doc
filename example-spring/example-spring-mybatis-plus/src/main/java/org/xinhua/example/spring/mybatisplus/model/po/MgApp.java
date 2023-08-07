package org.xinhua.example.spring.mybatisplus.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author lilong
 * @since 2023-08-06 01:44
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "mg_app", autoResultMap = true)
@ApiModel(value = "MgApp对象", description = "")
public class MgApp extends BaseEntity {

    @ApiModelProperty("app_id")
    @TableField("app_id")
    private String appId;

    @ApiModelProperty("app_secret")
    @TableField("app_secret")
    private String appSecret;

    @ApiModelProperty("access_token")
    @TableField("access_token")
    private String accessToken;

    @ApiModelProperty("过期时间")
    @TableField("expires_time")
    private LocalDateTime expiresTime;
}
