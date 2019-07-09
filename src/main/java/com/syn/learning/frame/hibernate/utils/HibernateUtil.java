package com.syn.learning.frame.hibernate.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName HibernateUtil
 * @Description TODO
 * @Date 2019/6/13 15:37
 **/
public class HibernateUtil {
    static private SessionFactory sessionFactory;

    static {
        Configuration cfg = new Configuration().configure();
        sessionFactory=cfg.buildSessionFactory();
    }

    public static Session getSession(){
        return sessionFactory.openSession();
    }
}
