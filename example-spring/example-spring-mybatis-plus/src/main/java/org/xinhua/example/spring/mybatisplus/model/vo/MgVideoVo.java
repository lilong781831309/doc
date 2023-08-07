package org.xinhua.example.spring.mybatisplus.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MgVideoVo extends BaseVo {

    @ApiModelProperty("小程序id")
    @JsonProperty("pid")
    private Long pid;

    @ApiModelProperty("微信剧目id")
    @JsonProperty("drama_id")
    private Long dramaId;

    @ApiModelProperty("微信剧目名称")
    @JsonProperty("name")
    private String name;

    @ApiModelProperty("备注信息")
    @JsonProperty("comment")
    private String comment;

}
