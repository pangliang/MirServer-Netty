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
	private static Session session = null;

	public static void init() throws Exception {
		Configuration configuration = new Configuration();
		configuration.configure();

		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		ourSessionFactory = configuration.buildSessionFactory();
	}

	public void begin(){
		session = ourSessionFactory.getCurrentSession();
		session.getTransaction().begin();
	}

	public void commit(){
		session.getTransaction().commit();
	}

	public void rollback(){
		if(session.isOpen())
			session.getTransaction().rollback();
	}

	public static Session getSession() throws HibernateException {
		Session session = ourSessionFactory.getCurrentSession();
		return session;
	}

	public void save(Object object) {
		session.save(object);
	}

	public void update(Object object) {
		session.update(object);
	}

	public void delete(Object object) {
		session.delete(object);
	}

	public static List query(Class clazz, SimpleExpression... simpleExpressions) {
		Criteria criteria = session.createCriteria(clazz);
		for (SimpleExpression simpleExpression : simpleExpressions) {
			criteria.add(simpleExpression);
		}
		List result = criteria.list();
		return result;
	}

}
