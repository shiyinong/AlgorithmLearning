package com.syn.learning.frame.spring;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName SpringDemo
 * @Description TODO
 * @Date 2019/6/28 11:04
 **/
public class SpringDemo {

    @Test
    public void selectTest(){
        ApplicationContext applicationContext= new ClassPathXmlApplicationContext("applicationContext.xml");
        ChildDao childDao = (ChildDao)applicationContext.getBean("childDao");
        childDao.select();
    }
}
