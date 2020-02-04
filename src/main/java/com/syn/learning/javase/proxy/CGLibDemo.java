package com.syn.learning.javase.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/12/16 17:03
 **/
public class CGLibDemo {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Men.class);
        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
            System.out.println("刷牙");
            methodProxy.invokeSuper(o, objects);
            System.out.println("漱口");
            return null;
        });
        Men men = (Men) enhancer.create();
        men.eat();
    }
}
class Men {
    public void eat() {
        System.out.println("eat");
    }
}
