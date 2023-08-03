package org.xinhua.example.mapper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtil {

    private static String config = "mybatis-config.xml";
    private static SqlSessionFactory defaultFactory = null;


    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory factory = null;
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        try {
            InputStream is = Resources.getResourceAsStream(config);
            factory = builder.build(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return factory;
    }

    public static SqlSession getSqlSession() {
        if(defaultFactory == null){
            defaultFactory = getSqlSessionFactory();
        }
        return defaultFactory.openSession();
    }
}
