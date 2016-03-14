package com.zhaoxiaodan.mirserver.db;

import org.hibernate.Criteria;
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

	public static Session getSession(){
		return ourSessionFactory.getCurrentSession();
	}

	public static void save(Object object) {
		getSession().save(object);
	}

	public static void update(Object object) {
		getSession().update(object);
	}

	public static void delete(Object object) {
		getSession().delete(object);
	}

	public static List query(Class clazz, SimpleExpression... simpleExpressions) {
		Criteria criteria = getSession().createCriteria(clazz);
		for (SimpleExpression simpleExpression : simpleExpressions) {
			criteria.add(simpleExpression);
		}
		List result = criteria.list();
		return result;
	}

}
