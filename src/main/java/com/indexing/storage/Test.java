package com.indexing.storage;

import com.indexing.storage.api.HadithSearch;
import com.indexing.storage.entity.HadithTerm;
import com.indexing.storage.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import java.util.List;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {

        Session session = HibernateUtil.getSession();

        Query query = session.createQuery("from HadithTerm");
        List<HadithTerm> hadithTerms = query.list();
        System.out
                .println("----------------------List of stored hadithTerm---------------------");
        for (HadithTerm ht : hadithTerms) {
            System.out.println(ht);
        }
        System.out
                .println("--------------------------------------------------------------");
        Scanner sc = new Scanner(System.in);
        FullTextSession fullTextSession = Search.getFullTextSession(session);

        try {
            fullTextSession.createIndexer().startAndWait();
            QueryBuilder qb = fullTextSession.getSearchFactory()
                    .buildQueryBuilder().forEntity(HadithTerm.class).get();
            String text = null;
            while (true) {
                System.out.println("Enter text to Search or q for quit");
                text = sc.nextLine();
                if ("q".equalsIgnoreCase(text)) {
                    System.out.println("End");
                    System.exit(0);
                }

                List<HadithTerm> result = new HadithSearch().getResult(qb,
                        fullTextSession, text);
                System.out.println("search results found " + result.size());
                for (HadithTerm ht : result) {
                    System.out.println(ht);
                }

            }

        } catch (InterruptedException e) {

            e.printStackTrace();
        }

    }

}
