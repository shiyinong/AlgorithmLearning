package com.syn.learning.frame.hibernate;

import com.syn.learning.frame.hibernate.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


/**
 * @author shiyinong
 * @version 1.0
 * @ClassName HibernateDemo
 * @Description TODO
 * @Date 2019/6/13 14:18
 **/
public class HibernateDemo {

    public static void main(String[] args){
        hibernateTest2();
    }

    static void hibernateTest1(){
        //        // 1.加载配置文件
//        Configuration cfg = new Configuration().configure();
//        // 2.获取session工厂
//        SessionFactory sessionFactory = cfg.buildSessionFactory();
//        // 3.获取session
//        Session session = sessionFactory.openSession();
        Session session= HibernateUtil.getSession();
        // 4.开启事务
        Transaction transaction = session.beginTransaction();
        // 5.执行操作


        //User user = session.get(User.class,1);

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);

        Root<User> root = query.from(User.class);
        query.select(root);
        query.where(criteriaBuilder.like(root.get("name"),"%啊%"));
        List<User> users = session.createQuery(query).list();

        for(User user  : users){
            System.out.println(user);
        }



//        //user.setUid(4);
//        user.setUage(49);
//        user.setName("张6");
//        user.setPassword("mnbv");
//        System.out.println(user);
//        Serializable id = session.save(user);
//        System.out.println(id);

        // 6.提交事务
        transaction.commit();
        // 7.释放资源
        session.close();
    }

    static void hibernateTest2(){
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Father father = new Father(70,"王父");

        Child child1 = new Child(45,"王子一",father);
        Child child2 = new Child(44,"王子二",father);
        Child child3 = new Child(43,"王子三",father);
        Child child4 = new Child(40,"王子四",father);

        father.getChildren().add(child1);
        father.getChildren().add(child2);
        father.getChildren().add(child3);
        father.getChildren().add(child4);

        session.save(child1);

//        session.save(child1);
//        session.save(child2);
//        session.save(child3);
//        session.save(child4);

        transaction.commit();
        session.close();
    }
}
