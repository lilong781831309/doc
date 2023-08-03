package org.xinhua.example.mapper;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xinhua.example.model.MgApp;

public class TestMgAppMapper {
    Logger logger = LoggerFactory.getLogger(TestMgAppMapper.class);

    @Test
    public void test(){
        logger.info("==============================");
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        MgAppMapper mapper = sqlSession.getMapper(MgAppMapper.class);

        MgApp mgApp1 = mapper.selectByPrimaryKey(1L);
        System.out.println(mgApp1);

        MgApp mgApp2 = mapper.selectByPrimaryKey(2L);
        System.out.println(mgApp2);
    }

    @Test
    public void test2(){
        logger.info("==============================");
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        MgAppMapper mapper = sqlSession.getMapper(MgAppMapper.class);

        MgApp mgApp1 = mapper.selectByPrimaryKey(1L);
        System.out.println(mgApp1);

        MgApp mgApp2 = mapper.selectByPrimaryKey(1L);
        System.out.println(mgApp2);
    }

    @Test
    public void test3(){
        logger.info("==============================");
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();

        MgAppMapper mapper = sqlSession.getMapper(MgAppMapper.class);
        MgApp mgApp1 = mapper.selectByPrimaryKey(1L);
        System.out.println(mgApp1);

        MgAppMapper mapper2 = sqlSession.getMapper(MgAppMapper.class);
        MgApp mgApp2 = mapper2.selectByPrimaryKey(1L);
        System.out.println(mgApp2);
    }

    @Test
    public void test4(){
        logger.info("==============================");
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        MgAppMapper mapper = sqlSession.getMapper(MgAppMapper.class);
        MgApp mgApp1 = mapper.selectByPrimaryKey(1L);
        System.out.println(mgApp1);
        sqlSession.commit();

        SqlSession sqlSession2 = SqlSessionUtil.getSqlSession();
        MgAppMapper mapper2 = sqlSession2.getMapper(MgAppMapper.class);
        MgApp mgApp2 = mapper2.selectByPrimaryKey(1L);
        System.out.println(mgApp2);
    }

    @Test
    public void test5(){
        logger.info("==============================");
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        MgAppMapper mapper = sqlSession.getMapper(MgAppMapper.class);
        MgApp mgApp1 = mapper.selectByPrimaryKey(1L);
        System.out.println(mgApp1);
        sqlSession.commit();
        sqlSession.clearCache();

        SqlSession sqlSession2 = SqlSessionUtil.getSqlSession();
        MgAppMapper mapper2 = sqlSession2.getMapper(MgAppMapper.class);
        MgApp mgApp2 = mapper2.selectByPrimaryKey(1L);
        System.out.println(mgApp2);
    }

    @Test
    public void test6(){
        logger.info("==============================");
        SqlSession sqlSession = SqlSessionUtil.getSqlSessionFactory().openSession();
        MgAppMapper mapper = sqlSession.getMapper(MgAppMapper.class);
        MgApp mgApp1 = mapper.selectByPrimaryKey(1L);
        System.out.println(mgApp1);

        SqlSession sqlSession2 = SqlSessionUtil.getSqlSessionFactory().openSession();
        MgAppMapper mapper2 = sqlSession2.getMapper(MgAppMapper.class);
        MgApp mgApp2 = mapper2.selectByPrimaryKey(1L);
        System.out.println(mgApp2);
    }
}
