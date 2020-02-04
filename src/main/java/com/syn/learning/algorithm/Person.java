package com.syn.learning.algorithm;


/**
 * @author shiyinong
 * @version 1.0
 * @ClassName Person
 * @Description TODO
 * @Date 2019/4/10 8:53
 **/
public class Person{
    static {
        System.out.println("ppppppppppppppp");
    }
    public static int p=100;
    public String name;
    public int age;
    transient public int id;
    public Person(String name,int id,int age){
        this.age=age;
        this.name=name;
        this.id=id;
    }
    public Person(){}

    final public void eat(){
        System.out.println("fan");
    }

    public void eat(int a){

    }

    static protected void test(){}

}


