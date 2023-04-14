package org.xinhua.example.spring.mvc.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * User视图对象
 */
@Data
@ToString
@EqualsAndHashCode
public class UserVo implements Serializable {

    private static final long serialVersionUID = 100654156901034128L;

    private Long id;
    private String username;
    private String name;
    private String address;
    private String phone;

}
