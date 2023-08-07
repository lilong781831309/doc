package org.xinhua.example.spring.mybatisplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "版本号", required = true)
    @JsonProperty(value = "version")
    private Integer version;

    @ApiModelProperty("附加信息")
    @JsonProperty(value = "extend_info")
    private Map<String, Object> extendInfo = new LinkedHashMap<>();

}
