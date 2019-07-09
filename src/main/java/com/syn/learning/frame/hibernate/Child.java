package com.syn.learning.frame.hibernate;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName Child
 * @Description TODO
 * @Date 2019/6/14 15:11
 **/
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
        return "cid: " + (null == cid ? "" : cid)
                + " age: " + (null == age ? "" : age)
                + " name: " + (null == name ? "" : name)
                + " father: " + (null == father ? "" : father.getName());
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
