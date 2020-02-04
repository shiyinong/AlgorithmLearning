package com.syn.learning.javase.spi;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.servlet.annotation.HandlesTypes;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/12/26 15:24
 **/
public class Main {
    public static void main(String[] args) {
        ServiceLoader<In> load = ServiceLoader.load(In.class);
        for (In in : load) {
            in.test();
        }
    }


}
