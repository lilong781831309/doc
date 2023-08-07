package org.xinhua.example.spring.mybatisplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MgVideoDTO extends BaseDTO {

    @ApiModelProperty(value = "小程序id", required = true)
    @JsonProperty("pid")
    private Long pid;

    @ApiModelProperty(value = "微信剧目id", required = true)
    @JsonProperty("drama_id")
    private Long dramaId;

    @ApiModelProperty("微信剧目名称")
    @JsonProperty("name")
    private String name;

    @ApiModelProperty("备注信息")
    @JsonProperty("comment")
    private String comment;

}
