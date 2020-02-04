package com.syn.learning.frame.mybatis.withspring;

import com.alibaba.druid.pool.DruidDataSource;
import com.syn.learning.frame.mybatis.withspring.mapper.UserMapper;
import com.syn.learning.frame.mybatis.withspring2.MyImportBeanDefinitionRegistrar;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/12/17 14:27
 **/
@Configuration
@MapperScan("com.syn.learning.frame.mybatis.withspring.mapper")
@Import(MyImportBeanDefinitionRegistrar.class)
public class Config {

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        return factoryBean.getObject();
    }

    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test?serverTimezone=UTC");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

}
