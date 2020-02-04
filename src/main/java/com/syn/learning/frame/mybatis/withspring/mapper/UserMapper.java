package com.syn.learning.frame.mybatis.withspring.mapper;

import com.syn.learning.frame.mybatis.withspring.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/12/17 14:28
 **/
public interface UserMapper {
    @Select("select * from user")
    List<User> listUser();

    @Select("select * from user where uid=#{uid}")
    User getUser(@Param("uid") int uid);
}
