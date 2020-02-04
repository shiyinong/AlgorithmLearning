package com.syn.learning.frame.mybatis.withspring.pojo;

import java.util.Date;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/12/17 14:29
 **/
public class User {
    private Integer uId;
    private String uName;
    private Date rTime;

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public Date getuDate() {
        return rTime;
    }

    public void setuDate(Date uTime) {
        this.rTime = uTime;
    }
}
