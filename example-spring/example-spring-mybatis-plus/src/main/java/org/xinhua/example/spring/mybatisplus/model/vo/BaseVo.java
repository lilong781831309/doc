package org.xinhua.example.spring.mybatisplus.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty("创建时间")
    @JsonProperty("create_at")
    private LocalDateTime createAt;

    @ApiModelProperty("创建人")
    @JsonProperty("create_by")
    private String createBy;

    @ApiModelProperty("修改时间")
    @JsonProperty("update_at")
    private LocalDateTime updateAt;

    @ApiModelProperty("修改人")
    @JsonProperty("update_by")
    private String updateBy;

    @ApiModelProperty("附加信息")
    @JsonProperty("extend_info")
    private Map<String, Object> extendInfo;

}
