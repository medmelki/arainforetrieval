package com.indexing;

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

		Query query = session.createQuery("from User");
		List<User> usr = query.list();
		System.out
				.println("----------------------List of stored user---------------------");
		for (User u : usr) {
			System.out.println("|" + u.getId() + "| " + u.getUsername() + "| "
					+ u.getEmail());
		}
		System.out
				.println("--------------------------------------------------------------");
		Scanner sc = new Scanner(System.in);
		FullTextSession fullTextSession = Search.getFullTextSession(session);

		try {
			fullTextSession.createIndexer().startAndWait();
			QueryBuilder qb = fullTextSession.getSearchFactory()
					.buildQueryBuilder().forEntity(User.class).get();
			String text = null;
			while (true) {
				System.out.println("Enter text to Search or q for quit");
				text = sc.nextLine();
				if ("q".equalsIgnoreCase(text)) {
					System.out.println("End");
					System.exit(0);
				}

				List<User> result = new SearchLib().getResult(qb,
						fullTextSession, text);
				System.out.println("search results found " + result.size());
				for (User u : result) {
					System.out.println(u.getId() + "|" + u.getPassword() + "|"
							+ u.getUsername() + "|" + u.getEmail());
				}

			}

		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

}
