package org.xinhua.example.spring.mybatisplus.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@AllArgsConstructor(staticName = "of")
public class PageVo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前页")
    private long current;

    @ApiModelProperty("总页数")
    private long pages;

    @ApiModelProperty("查询数量")
    private long size;

    @ApiModelProperty("总数量")
    private long total;

    @ApiModelProperty("分页数据")
    private List<T> records;

}
