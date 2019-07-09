package com.syn.learning.frame.mybatis.mapper;

import com.syn.learning.frame.mybatis.User;

import java.util.List;

public interface UserMapper {
    User select(int uid);
    List<User> selectAll(String name);
    int insert(User user);
}
