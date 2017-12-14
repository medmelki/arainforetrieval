package com.indexing.storage.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {


    public static Session getSession() {
        SessionFactory sf = new Configuration().configure("hibernate.cfg.xml")
                .buildSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        return session;
    }
}
