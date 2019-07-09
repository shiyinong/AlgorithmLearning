package com.syn.learning.frame.mybatis;

import com.syn.learning.frame.mybatis.mapper.UserMapper;
import com.syn.learning.frame.mybatis.utils.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.util.List;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName MyBatisDemo
 * @Description mybatis 学习
 * @Date 2019/6/26 10:36
 **/
public class MyBatisDemo {

    @Test
    public void selectTest() {
        SqlSessionFactory sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        List<User> users = sqlSession.selectList("com.syn.learning.frame.mybatis.mapper.UserMapper.selectAll");
        for (User u : users) {
            System.out.println(u);
        }

        sqlSession.close();
    }

    @Test
    public void selectTest2(){
        SqlSessionFactory sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        User user = userMapper.select(2);
        System.out.println(user);

        List<User> users = userMapper.selectAll("");
        for(User u : users){
            System.out.println(u);
        }
    }

    @Test
    public void insertTest(){
        SqlSessionFactory sqlSessionFactory=MyBatisUtil.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        User user = new User();
        user.setUid(11);
        user.setPassword("098765");

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        int id = userMapper.insert(user);
        sqlSession.commit();
        sqlSession.close();
        System.out.println(id);
    }

}
