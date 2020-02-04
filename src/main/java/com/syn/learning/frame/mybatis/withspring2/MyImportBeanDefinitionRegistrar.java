package com.syn.learning.frame.mybatis.withspring2;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/12/17 15:58
 **/
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
                                        BeanDefinitionRegistry beanDefinitionRegistry) {
        Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes("My");
        BeanDefinitionBuilder builder =BeanDefinitionBuilder.genericBeanDefinition(TestMy.class);
        beanDefinitionRegistry.registerBeanDefinition("TestMy",builder.getBeanDefinition());
    }
}
