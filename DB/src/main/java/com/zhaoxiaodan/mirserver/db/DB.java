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

	public static void init() throws Exception {
		Configuration configuration = new Configuration();
		configuration.configure();

		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		ourSessionFactory = configuration.buildSessionFactory();
	}

	public static Session getSession() throws HibernateException {
		Session session = ourSessionFactory.getCurrentSession();
		return session;
	}

	public static void save(Object object) {
		Session session = getSession();
		session.save(object);
	}

	public static void update(Object object) {
		Session session = getSession();
		session.update(object);
	}

	public static void delete(Object object) {
		Session session = getSession();
		session.delete(object);
	}

	public static List query(Class clazz, SimpleExpression... simpleExpressions) {
		Session  session  = getSession();
		Criteria criteria = session.createCriteria(clazz);
		for (SimpleExpression simpleExpression : simpleExpressions) {
			criteria.add(simpleExpression);
		}
		List result = criteria.list();
		return result;
	}

}
