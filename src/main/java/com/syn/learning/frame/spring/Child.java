package com.syn.learning.frame.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName Child
 * @Description TODO
 * @Date 2019/6/14 15:11
 **/
@Repository("child")
public class Child {
    private Integer cid;
    private Integer age;
    private String name;
    private Father father;

    public Child() {
    }

    public Child(Integer age, String name, Father father) {
        this.age = age;
        this.name = name;
        this.father = father;
    }

    @Override
    public String toString() {
        return "Child{" +
                "cid=" + cid +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", father=" + father +
                '}';
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
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

    @Value("aaaa")
    public void setName(String name) {
        this.name = name;
    }

    public Father getFather() {
        return father;
    }

    public void setFather(Father father) {
        this.father = father;
    }
}
