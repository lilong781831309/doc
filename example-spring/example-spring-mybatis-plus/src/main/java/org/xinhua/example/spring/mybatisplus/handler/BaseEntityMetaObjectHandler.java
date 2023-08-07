package org.xinhua.example.spring.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BaseEntityMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createAt", LocalDateTime.now(), metaObject);
        this.setFieldValByName("createBy", "system", metaObject);
        this.setFieldValByName("updateAt", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateBy", "system", metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateAt", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateBy", "system", metaObject);
    }

}
