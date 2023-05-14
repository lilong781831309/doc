package org.xinhua.example.design_pattern.structural.proxy._jdk.demo2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: lilong
 * @createDate: 2023/4/9 0:09
 * @Description: 用户类
 * @Version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Long id;
    private String name;
    private String address;
    private String email;
}
