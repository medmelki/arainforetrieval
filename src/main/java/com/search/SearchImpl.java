package com.search;

import com.awn.ArabicWordSenseService;
import com.indexing.storage.api.HadithSearch;
import com.indexing.storage.entity.HadithTerm;
import com.indexing.storage.util.HibernateUtil;
import com.textprocessing.Stemmer;
import com.textprocessing.util.HadithIndexUtil;
import com.textprocessing.util.TextUtility;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchImpl {

    public static void search(String query) {

        query = TextUtility.removePunctuations(query);
        Stemmer stemmer = new Stemmer();
        List<String> words = TextUtility.tokenize(query);
        words = TextUtility.removeStopWords(words);
        words = words.stream()
                .map(stemmer::findRoot)
                .collect(Collectors.toList());
        Session session = HibernateUtil.getSession();
        List<String> qwords = termExpansion(words);

        List<String> hadiths = retrieveRelatedIndexedHadiths(session, qwords);

        hadiths.forEach(System.out::println);
        session.close();
        System.out.println("Done...");
    }

    private static List<String> termExpansion(List<String> words) {
        List<String> qwords = new ArrayList<>(words);
        for (String word : words) {
            qwords.addAll(ArabicWordSenseService.getSynonyms(word, word));
        }
        return qwords;
    }

    private static List<String> retrieveRelatedIndexedHadiths(Session session, List<String> qwords) {

        // retrieve related indexed hadith by fuzzy search
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        List<HadithTerm> hadithTerms = new ArrayList<>();
        for (String word : qwords) {
            hadithTerms.addAll(HadithSearch.getHadithTerms(word, fullTextSession));
        }
        // filter unique docDIDs
        Set<Long> docIndexesInSahih = new HashSet<>();
        for (HadithTerm hadithTerm : hadithTerms) {
            docIndexesInSahih.add(HadithIndexUtil.getIndexInSahih(hadithTerm.getDIDdoc()));
        }

        StringBuilder hadithContent = new StringBuilder();
        // read hadith txt
        try (BufferedReader br = Files.newBufferedReader(Paths.get("hadith/hadith.txt"))) {
            br.lines().forEach(hadithContent::append);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> hadiths = new ArrayList<>();
        for (Long index : docIndexesInSahih) {
            hadiths.add(hadithContent.substring(hadithContent.indexOf(index.toString()),
                    hadithContent.indexOf(Long.toString(index + 1))));
        }
        return hadiths;
    }

    public static void main(String[] args) {
        search("فوائد العسل");
    }
}
