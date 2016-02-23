package com.zhaoxiaodan.mirserver.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class DB {

    private static SessionFactory  ourSessionFactory;
    private static ServiceRegistry serviceRegistry;

    public static void init() throws Exception{
        Configuration configuration = new Configuration();
        configuration.configure();

        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        ourSessionFactory = configuration.buildSessionFactory();
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }



}
