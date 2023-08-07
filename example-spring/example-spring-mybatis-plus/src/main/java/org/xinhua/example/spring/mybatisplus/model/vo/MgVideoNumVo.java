package org.xinhua.example.spring.mybatisplus.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel
public class MgVideoNumVo extends BaseVo {

    @ApiModelProperty("小程序id")
    @JsonProperty("pid")
    private Long pid;

    @ApiModelProperty("所属视频id")
    @JsonProperty("video_id")
    private Long videoId;

    @ApiModelProperty("第几集")
    @JsonProperty("video_num")
    private Integer videoNum;

    @ApiModelProperty("微信视频素材id")
    @JsonProperty("media_id")
    private Long mediaId;

    @ApiModelProperty("微信视频素材url")
    @JsonProperty("mp4_url")
    private String mp4Url;

    @ApiModelProperty("视频链接过期时间")
    @JsonProperty("expires_time")
    private LocalDateTime expiresTime;

}
