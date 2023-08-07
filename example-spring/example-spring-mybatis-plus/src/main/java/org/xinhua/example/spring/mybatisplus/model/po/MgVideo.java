package org.xinhua.example.spring.mybatisplus.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@TableName(value = "mg_video", autoResultMap = true)
@ApiModel(value = "MgVideo对象", description = "")
public class MgVideo extends BaseEntity {

    @ApiModelProperty("小程序id")
    @TableField("pid")
    private Long pid;

    @ApiModelProperty("微信剧目id")
    @TableField("drama_id")
    private Long dramaId;

    @ApiModelProperty("微信剧目名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("备注信息")
    @TableField("comment")
    private String comment;
}
