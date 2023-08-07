package org.xinhua.example.spring.mybatisplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "分页查询参数")
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "第几页", required = true)
    private Integer pageNum = 0;

    @ApiModelProperty(value = "查询数量", required = true)
    private Integer pageSize = 10;

    @ApiModelProperty("排序字段")
    @JsonProperty("order_by")
    private String orderBy;

    @ApiModelProperty("是否升序")
    @JsonProperty("is_asc")
    private boolean isAsc;

}
