package com.indexing.storage.api;

import com.indexing.storage.entity.HadithTerm;
import com.indexing.storage.util.HibernateUtil;
import org.hibernate.Session;

public class HadithStorage {

    public static void save(HadithTerm hadithTerm) {
        Session session = HibernateUtil.getSession();
        session.saveOrUpdate(hadithTerm);
        session.getTransaction().commit();
        session.close();
    }

    public static void delete(HadithTerm hadithTerm) {
        Session session = HibernateUtil.getSession();
        session.delete(hadithTerm);
        session.getTransaction().commit();
        session.close();
    }
}
