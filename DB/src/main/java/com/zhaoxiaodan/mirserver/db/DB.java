package com.zhaoxiaodan.mirserver.db;

import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class DB {

	private static SessionFactory  ourSessionFactory;
	private static ServiceRegistry serviceRegistry;
	private Session session = null;

	public static void init() throws Exception {
		Configuration configuration = new Configuration();
		configuration.configure();

		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		ourSessionFactory = configuration.buildSessionFactory();
	}

	public DB begin(){
		session = ourSessionFactory.getCurrentSession();
		session.getTransaction().begin();
		return this;
	}

	public void commit(){
		session.getTransaction().commit();
	}

	public void rollback(){
		if(session.isOpen())
			session.getTransaction().rollback();
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

	public List query(Class clazz, SimpleExpression... simpleExpressions) {
		Criteria criteria = session.createCriteria(clazz);
		for (SimpleExpression simpleExpression : simpleExpressions) {
			criteria.add(simpleExpression);
		}
		List result = criteria.list();
		return result;
	}

	public SQLQuery createSQLQuery(String queryString){
		return session.createSQLQuery(queryString);
	}

}
