package org.xinhua.example.design_pattern.creational.prototype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: lilong
 * @createDate: 2023/4/8 19:40
 * @Description: 城市  实现 Cloneable，Serializable
 * @Version: 1.0
 */

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class City implements Cloneable, Serializable {

    private static final long serialVersionUID = 5368283749346272841L;
    private String name;

    @Override
    protected Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

}
