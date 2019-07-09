package com.syn.learning.frame.mybatis;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName User
 * @Description TODO
 * @Date 2019/6/13 14:27
 **/
public class User {
    private Integer uid;
    private String name;
    private String password;
    private Integer uage;

    private Integer sex;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", uage=" + uage +
                '}';
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUage() {
        return uage;
    }

    public void setUage(Integer uage) {
        this.uage = uage;
    }

}
