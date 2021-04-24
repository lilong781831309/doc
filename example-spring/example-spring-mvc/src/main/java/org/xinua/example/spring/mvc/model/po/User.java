package org.xinua.example.spring.mvc.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "t_user", indexes = {
        @Index(name = "unique_username", columnList = "username", unique = true),
        @Index(name = "unique_phone", columnList = "phone", unique = true),
        @Index(name = "index_orderFlag", columnList = "orderFlag"),
        @Index(name = "index_addTime", columnList = "addTime")})
@org.hibernate.annotations.Table(appliesTo = "t_user", comment = "用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 3914305006513172648L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "varchar(50) not null comment '用户名'", nullable = false, updatable = false)
    private String username;

    @Column(columnDefinition = "varchar(50) not null comment '用户姓名'", nullable = false)
    private String name;

    @Column(columnDefinition = "varchar(255) not null comment '用户地址'", nullable = false)
    private String address;

    @Column(columnDefinition = "varchar(20) not null comment '电话号码'", nullable = false)
    private String phone;

    @Column(columnDefinition = "bigint(20) not null default 0 comment '排序标识'", nullable = false, updatable = false)
    private Long orderFlag;

    @Column(columnDefinition = "datetime not null default CURRENT_TIMESTAMP comment '添加时间'", nullable = false, insertable = false, updatable = false)
    private Date addTime;

    @Column(columnDefinition = "datetime null on update CURRENT_TIMESTAMP comment '修改时间'", updatable = false)
    private Date modifyTime;

}
