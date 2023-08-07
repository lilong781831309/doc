package org.xinhua.example.spring.mybatisplus.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ResultEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("错误代码,0 正常")
    private int errCode = 0;

    @ApiModelProperty("错误信息,为空 正常")
    private String errMsg = "";

    @ApiModelProperty("其他信息")
    private String msg = "";

    @ApiModelProperty("响应结果正文")
    private T content;

}
