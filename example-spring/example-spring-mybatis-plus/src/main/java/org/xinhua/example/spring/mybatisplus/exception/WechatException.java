package org.xinhua.example.spring.mybatisplus.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WechatException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer errcode;//微信返回字段
    private String errmsg;//微信返回字段

}
