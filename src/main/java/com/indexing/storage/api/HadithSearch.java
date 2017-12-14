package com.indexing.storage.api;

import com.indexing.storage.entity.HadithTerm;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;

import java.util.List;

public class HadithSearch {

    public List<HadithTerm> getResult(QueryBuilder qb, FullTextSession fullTextSession, String query) {
        org.apache.lucene.search.Query luceneQuery = ((org.hibernate.search.query.dsl.QueryBuilder) qb)
                .keyword().fuzzy().onFields("term", "cr")
                .matching(query).createQuery();
        org.hibernate.Query fullTextQuery = fullTextSession
                .createFullTextQuery(luceneQuery);
        return fullTextQuery.list();
    }

}
