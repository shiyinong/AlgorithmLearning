package com.syn.learning.javase.proxy;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName ProxyDemo
 * @Description TODO
 * @Date 2019/6/26 15:55
 **/
public class ProxyDemo {

    @Test
    public void test() {
        Student student = new StudentImpl();
        Student studentProxy = (Student) Proxy.newProxyInstance(student.getClass().getClassLoader(),
                student.getClass().getInterfaces(),
                (Object proxy, Method method, Object[] args) -> {
                    System.out.println("准备文具");
                    method.invoke(student, args);
                    System.out.println("检查一遍");
                    return null;
                });
        studentProxy.work();
    }
}
