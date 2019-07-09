package com.syn.learning.frame.mybatis;

import java.util.HashSet;
import java.util.Set;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName Father
 * @Description TODO
 * @Date 2019/6/14 15:11
 **/
public class Father {
    private Integer fid;
    private Integer age;
    private String name;
    private Set<Child> children=new HashSet<>();

    public Father() {
    }

    public Father(Integer age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Father{" +
                "fid=" + fid +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", children=" + children +
                '}';
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Child> getChildren() {
        return children;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }
}
