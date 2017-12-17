package com.search;

import com.indexing.storage.api.HadithSearch;
import com.indexing.storage.entity.HadithTerm;
import com.indexing.storage.util.HibernateUtil;
import com.textprocessing.Stemmer;
import com.textprocessing.util.TextUtility;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

public class SearchImpl {


    public static void search(String query) {


        query = TextUtility.removePunctuations(query);
        // TODO : spellcheck the query
        Stemmer stemmer = new Stemmer();
        List<String> words = TextUtility.tokenize(query);
        words = TextUtility.removeStopWords(words);
        // TODO : add POS to the query
        words = words.stream()
                // TODO : termExpansion for words
                .map(stemmer::findRoot)
                .collect(Collectors.toList());
        Session session = HibernateUtil.getSession();
        for (String word : words) {
            FullTextSession fullTextSession = Search.getFullTextSession(session);
            createQuery(word, fullTextSession);
        }
        session.close();
        System.out.println("Done...");
    }


    // TODO : move to HadithSearch class
    public static void createQuery(String text, FullTextSession fullTextSession) {
        QueryBuilder qb = fullTextSession.getSearchFactory()
                .buildQueryBuilder().forEntity(HadithTerm.class).get();

        List<HadithTerm> result = new HadithSearch().getResult(qb,
                fullTextSession, text);
        for (HadithTerm ht : result) {
            System.out.println(ht);
        }
    }


    public static void main(String[] args) {
        search("فوائد العسل");
    }
}
