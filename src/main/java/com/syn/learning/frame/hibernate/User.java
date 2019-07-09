package com.syn.learning.frame.hibernate;

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
    private Integer age;

    public User() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[uid:");
        if (null != this.uid) {
            sb.append(this.uid);
        }
        sb.append(" name:");
        if (null != this.name) {
            sb.append(this.name);
        }
        sb.append(" password:");
        if (null != this.password) {
            sb.append(this.password);
        }
        sb.append(" age:");
        if (null != this.age) {
            sb.append(this.age);
        }
        sb.append("]");
        return sb.toString();
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
