package com.search;

import com.awn.ArabicWordSenseService;
import com.indexing.storage.api.HadithSearch;
import com.indexing.storage.entity.HadithTerm;
import com.indexing.storage.util.HibernateUtil;
import com.ontology.search.OntDictionary;
import com.ontology.search.OntMatcher;
import com.ontology.search.OntTerm;
import com.ontology.util.OntologyLoader;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchImpl {

    public static void search(String query) {

        query = TextUtility.removePunctuations(query);
        Stemmer stemmer = new Stemmer();
        List<String> words = TextUtility.tokenize(query);
        Session session = HibernateUtil.getSession();
        // search as a bag of words
        List<String> hadiths;
        do {
            hadiths = retrieveRelatedIndexedHadithsByQuery(session, buildQuery(words));
            words.remove(0);
        } while (words.size() > 0 && hadiths.isEmpty());
        // search with stemmed qwords
        if (hadiths.isEmpty()) {
            words = TextUtility.removeStopWords(words);
            words = words.stream()
                    .map(stemmer::findRoot)
                    .collect(Collectors.toList());
            List<String> qwords = termExpansion(words);

            hadiths = retrieveRelatedIndexedHadiths(session, qwords);
        }
        hadiths.forEach(System.out::println);

        session.close();
    }

    private static String buildQuery(List<String> words) {
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word).append(" ");
        }
        return sb.toString().trim();
    }

    private static List<String> termExpansion(List<String> words) {
        List<String> qwords = new ArrayList<>(words);
        for (String word : words) {
            qwords.addAll(ArabicWordSenseService.getSynonyms(word, word));
        }
        return qwords;
    }

    private static List<String> retrieveRelatedIndexedHadiths(Session session, List<String> qwords) {

        FullTextSession fullTextSession = Search.getFullTextSession(session);

        List<HadithTerm> hadithTerms = new ArrayList<>();
        Set<Long> docIndexesInSahih = new LinkedHashSet<>();
        Set<Double> similarities = new LinkedHashSet<>();
        for (String word : qwords) {
            hadithTerms.addAll(HadithSearch.getHadithTerms(word, fullTextSession));
            // filter unique docDIDs
            for (HadithTerm hadithTerm : hadithTerms) {
                docIndexesInSahih.add(HadithIndexUtil.getIndexInSahih(hadithTerm.getDIDdoc()));
                // calculate similarity
//                ResnikCalculator
//                        .calcRelatedness(OntologyLoader.ontModel,
//                                reasoning(hadithTerm.getCr()),
//                                reasoning(word));
            }
            hadithTerms.clear();
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

    private static List<String> retrieveRelatedIndexedHadithsByQuery(Session session, String query) {

        FullTextSession fullTextSession = Search.getFullTextSession(session);

        List<HadithTerm> hadithTerms = new ArrayList<>();
        Set<Long> docIndexesInSahih = new LinkedHashSet<>();
        hadithTerms.addAll(HadithSearch.getExactHadithTerms(query, fullTextSession));

        // filter unique docDIDs
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

    private static OntTerm reasoning(String w) {
        OntDictionary dictionary = null;
        try {
            dictionary = OntologyLoader.loadOntology();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        assert dictionary != null;
        OntMatcher matcher = new OntMatcher(dictionary);
        List<OntTerm> result = matcher.match(w);
        return result == null || result.isEmpty() ? null : result.get(0);
    }

    public static void main(String[] args) {
//        String[] inputs = new String[20];
//        inputs[0] = "الحث على المداوة.";
//        inputs[1] = "علاج الصداع.";
//        inputs[2] = "نهي سب الحمى.";
//        inputs[3] = "علاج الالتهاب الرئوي.";
//        inputs[4] = "أهمية السواك.";
//        inputs[5] = "نهي تعذيب الأولاد.";
//        inputs[6] = "مداواة روحانية.";
//        inputs[7] = "العلاج بالماء.";
//        inputs[8] = "تقرحات الجلد.";
//        inputs[0] = "الحجامة.";
//        inputs[1] = "الحث على المداوة.";
//        inputs[2] = "علاج الحمى.";
//        inputs[3] = "أمراض تعالج بالحناء.";
//        inputs[4] = "التداوي بأمر الله.";
//        inputs[5] = "فوائد العسل.";
//        inputs[6] = "الأجرة على الحجامة.";
//        inputs[7] = "علاج الصداع.";
//        inputs[8] = "نهي سب الحمى.";
//        inputs[9] = "علاج الالتهاب الرئوي.";
//        inputs[10] = "أهمية السواك.";
//        inputs[11] = "نهي تعذيب الأولاد.";
//        inputs[12] = "الرقية من الوجع.";
//        inputs[13] = "مداواة روحانية.";
//        inputs[14] = "الوقاية من السحر.";
//        inputs[15] = "الأدوية الوقائية.";
//        inputs[16] = "أمراض الجسم.";
//        inputs[17] = "العلاج بالماء.";
//        inputs[18] = "أنواع الأدوية الطبيعية.";
//        inputs[19] = "تقرحات الجلد.";
//        for (String input : inputs) {
//            System.out.printf("Results for %s : %n", input);
//            search(input);
//            System.out.printf("%n%n");
//        }
        search("فوائد العسل");
        System.out.println("\nDone...");
    }
}
