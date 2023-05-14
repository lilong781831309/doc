package org.xinhua.example.design_pattern.structural.proxy._jdk.demo2;

import java.lang.annotation.*;

/**
 * @Author: lilong
 * @createDate: 2023/4/9 2:06
 * @Description: 事务注解
 * @Version: 1.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@interface Transaction {
}
