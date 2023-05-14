package org.xinhua.example.design_pattern.creational.prototype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.*;

/**
 * @Author: lilong
 * @createDate: 2023/4/8 19:40
 * @Description: 省  实现 Cloneable，Serializable
 * @Version: 1.0
 */

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Province implements Cloneable, Serializable {

    private String name;
    private ArrayList<City> citys;

    @Override
    protected Object clone() {
        Province province = null;
        try {
            province = (Province) super.clone();
            province.setCitys((ArrayList<City>) citys.clone());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return province;
    }

    public Object deepClone() {
        Province province = null;
        try {
            province = (Province) super.clone();
            province.setCitys((ArrayList<City>) citys.deepClone());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return province;
    }

}
