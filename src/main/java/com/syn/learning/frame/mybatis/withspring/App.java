package com.syn.learning.frame.mybatis.withspring;

import com.syn.learning.frame.mybatis.withspring.mapper.UserMapper;
import com.syn.learning.frame.mybatis.withspring.pojo.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/12/17 14:28
 **/
public class App {

    public static void main(String[] args){
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(Config.class);
        UserMapper userMapper = context.getBean(UserMapper.class);
//        List<User> users = userMapper.listUser();
        User user = userMapper.getUser(1);
        System.out.println(user.getuName()+user.getuId()+user.getuDate());

    }

}
