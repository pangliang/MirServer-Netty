package com.zhaoxiaodan.mirserver.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by liangwei on 16/2/18.
 */
public class DB {

    private static EntityManagerFactory entityManagerFactory = null;

    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("mir2");
    }

    public static EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }



}
