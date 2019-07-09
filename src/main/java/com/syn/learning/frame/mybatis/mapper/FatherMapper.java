package com.syn.learning.frame.mybatis.mapper;

import com.syn.learning.frame.mybatis.Father;

import java.util.List;

public interface FatherMapper {
    Father select();
    List<Father> selectAll();
    int insert(Father father);
}
