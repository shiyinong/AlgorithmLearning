package com.syn.learning.frame.spring;

import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName ChildDaoImpl
 * @Description TODO
 * @Date 2019/6/28 11:06
 **/
public class ChildDaoImpl implements ChildDao {

    @Resource(name="child")
    private Child child;

    @Value("lalalal")
    private String name;

    @Override
    public void select() {
        System.out.println("child select......"+child);
    }
}
