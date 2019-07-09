package com.syn.learning.frame.mybatis.mapper;

import com.syn.learning.frame.mybatis.Child;
import com.syn.learning.frame.mybatis.utils.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChildMapperTest {

    @Test
    public void select() {
        SqlSessionFactory sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        ChildMapper childMapper = sqlSession.getMapper(ChildMapper.class);
        Child child = childMapper.select(2);
        System.out.println(child);
    }
}