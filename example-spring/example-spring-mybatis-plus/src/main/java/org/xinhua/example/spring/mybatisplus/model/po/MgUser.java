package org.xinhua.example.spring.mybatisplus.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.xinhua.example.spring.mybatisplus.enums.Gender;

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
@TableName(value = "mg_user", autoResultMap = true)
@ApiModel(value = "MgUser对象", description = "")
public class MgUser extends BaseEntity {

    @ApiModelProperty("姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty("年龄")
    @TableField("age")
    private Integer age;

    @ApiModelProperty("电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("性别")
    @TableField("gender")
    private Gender gender;
}
