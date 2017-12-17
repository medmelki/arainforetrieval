package com.indexing.storage.api;

import com.indexing.storage.entity.HadithTerm;
import org.hibernate.Session;

public class HadithStorage {

    public static void save(HadithTerm hadithTerm, Session session) {
        session.saveOrUpdate(hadithTerm);
    }

    public static void delete(HadithTerm hadithTerm, Session session) {
        session.delete(hadithTerm);
        session.getTransaction().commit();
    }
}
