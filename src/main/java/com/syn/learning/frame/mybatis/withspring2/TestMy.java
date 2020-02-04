package com.syn.learning.frame.mybatis.withspring2;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/12/17 16:14
 **/
@My
public class TestMy {

    @Value("123")
    private String s;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
