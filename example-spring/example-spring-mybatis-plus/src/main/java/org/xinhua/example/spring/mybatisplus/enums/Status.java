package org.xinhua.example.spring.mybatisplus.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Status {

    NORMAL(1, "正常"),
    FREEZE(2, "异常"),;

    @EnumValue
    private int value;

    @JsonValue
    private String desc;

    Status(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
