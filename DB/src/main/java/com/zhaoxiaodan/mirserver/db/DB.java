package com.zhaoxiaodan.mirserver.db;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class DB {

    private static SessionFactory  ourSessionFactory;
    private static ServiceRegistry serviceRegistry;

    public static void init() throws Exception{
        Configuration configuration = new Configuration();
        configuration.configure();

        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        ourSessionFactory = configuration.buildSessionFactory();
    }

    private static Session getSession() throws HibernateException {
        return ourSessionFactory.getCurrentSession();
    }

    public static void saveOrUpdate(Object object){
        Session session = getSession();
        try{
            session.getTransaction().begin();
            session.saveOrUpdate(object);
            session.flush();
            session.getTransaction().commit();
        }catch (Exception e){
            session.getTransaction().rollback();
            throw e;
        }

    }

    public static void delete(Object object){
        Session session = getSession();
        try{
            session.getTransaction().begin();
            session.delete(object);
            session.flush();
            session.getTransaction().commit();
        }catch (Exception e){
            session.getTransaction().rollback();
            throw e;
        }
    }

    public static List query(Class clazz, SimpleExpression... simpleExpressions){
        Session session = getSession();
        try{
            session.getTransaction().begin();
            Criteria criteria = session.createCriteria(clazz);
            for(SimpleExpression simpleExpression : simpleExpressions){
                criteria.add(simpleExpression);
            }
            List result = criteria.list();
            return result;
        }catch (Exception e){
            session.getTransaction().rollback();
            throw e;
        }

    }

}
