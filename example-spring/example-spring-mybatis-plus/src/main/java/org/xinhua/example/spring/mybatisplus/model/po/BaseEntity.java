package org.xinhua.example.spring.mybatisplus.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("附加信息")
    @TableField(value = "extend_info", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> extendInfo;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_at", fill = FieldFill.INSERT)
    private LocalDateTime createAt;

    @ApiModelProperty("创建人")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("修改时间")
    @TableField(value = "update_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateAt;

    @ApiModelProperty("修改人")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "是否删除标志,0 未删除,其他 已删除", hidden = true)
    @TableField("deleted")
    @TableLogic
    private Long deleted;

    @ApiModelProperty(value = "版本号")
    @TableField(value = "version", fill = FieldFill.UPDATE)
    @Version
    private Integer version;

}
