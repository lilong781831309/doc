package org.xinhua.example.spring.mybatisplus.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Gender {

    MAN(1, "男"),
    FEMALE(2, "女"),;

    @EnumValue
    private int value;

    @JsonValue
    private String desc;

    Gender(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
