package com.syn.learning.javase.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {
    int value(); //如果该注解中主需要一个值，那么最好设置为value，因为用户在标注时，不用在写value = ***，直接写***即可
}
