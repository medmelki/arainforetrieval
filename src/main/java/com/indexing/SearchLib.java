package com.indexing;

import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;

import java.util.List;

public class SearchLib {

	public List<User> getResult(QueryBuilder qb,FullTextSession fullTextSession, String query) {
		org.apache.lucene.search.Query luceneQuery = ((org.hibernate.search.query.dsl.QueryBuilder) qb)
				.keyword().onField("username").andField("email")
				.matching(query).createQuery();
		org.hibernate.Query fullTextQuery = fullTextSession
				.createFullTextQuery(luceneQuery);
		return fullTextQuery.list();
	}

}
