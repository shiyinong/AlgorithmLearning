package com.syn.learning.frame.mybatis.mapper;

import com.syn.learning.frame.mybatis.Father;
import com.syn.learning.frame.mybatis.utils.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FatherMapperTest {

    @Test
    public void select() {
        SqlSessionFactory sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        FatherMapper fatherMapper = sqlSession.getMapper(FatherMapper.class);
        List<Father> fathers = fatherMapper.selectAll();
        for(Father father : fathers){
            System.out.println(father);
        }

    }

    @Test
    public void selectAll() {
    }

    @Test
    public void insert() {
    }
}