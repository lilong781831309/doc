package org.xinhua.example.spring.mvc.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * User数据传输对象
 */
@Data
@ToString
@EqualsAndHashCode
public class UserDto implements Serializable {

    private static final long serialVersionUID = 100654156901034127L;

    @Min(value = 1, message = "id 必须大于0", groups = {Update.class})
    private Long id;

    @NotBlank(message = "username 不能为空", groups = {Add.class})
    @Size(min = 5, max = 50, message = "username 长度需在 5 ~ 50 之间", groups = {Add.class})
    private String username;

    @NotBlank(message = "name 不能为空", groups = {Add.class, Update.class})
    @Size(max = 50, message = "name 长度需小于 50", groups = {Add.class, Update.class})
    private String name;

    @Size(max = 255, message = "address 长度需小于 255", groups = {Add.class, Update.class})
    private String address;

    @Size(max = 20, message = "phone 长度需小于 20", groups = {Add.class, Update.class})
    private String phone;

}
