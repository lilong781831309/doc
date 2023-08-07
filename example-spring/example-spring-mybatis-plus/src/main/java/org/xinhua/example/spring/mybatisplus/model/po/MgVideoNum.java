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
@TableName(value = "mg_video_num", autoResultMap = true)
@ApiModel(value = "MgVideoNum对象", description = "")
public class MgVideoNum extends BaseEntity {

    @ApiModelProperty("小程序id")
    @TableField("pid")
    private Long pid;

    @ApiModelProperty("所属视频id")
    @TableField("video_id")
    private Long videoId;

    @ApiModelProperty("第几集")
    @TableField("video_num")
    private Integer videoNum;

    @ApiModelProperty("微信视频素材id")
    @TableField("media_id")
    private Long mediaId;

    @ApiModelProperty("微信视频素材url")
    @TableField("mp4_url")
    private String mp4Url;

    @ApiModelProperty("视频链接过期时间")
    @TableField("expires_time")
    private LocalDateTime expiresTime;
}
