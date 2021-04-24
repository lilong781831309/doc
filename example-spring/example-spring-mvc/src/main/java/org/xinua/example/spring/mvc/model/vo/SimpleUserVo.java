package org.xinua.example.spring.mvc.model.vo;

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
public class SimpleUserVo implements Serializable {

    private static final long serialVersionUID = 100654156901034128L;

    private Long id;
    private String username;
    private String address;

}
