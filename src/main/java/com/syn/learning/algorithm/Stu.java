package com.syn.learning.algorithm;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName Stu
 * @Description TODO
 * @Date 2019/4/11 15:29
 **/
public class Stu<T> extends Person{

    static {
        System.out.println("sssssssssssssss");
    }
    public String school;
    T t;
    public Stu(String school){
        this.school=school;
    }
    public Stu(){}


//    public void drink() {
//        System.out.println("cola");
//    }

    void study(){
        System.out.println("学习");

    }


    @Override
    public void eat(int a) {
        System.out.println("stu ---");
    }

}
