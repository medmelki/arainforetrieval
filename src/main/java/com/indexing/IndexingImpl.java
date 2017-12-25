package com.indexing;

import com.indexing.storage.api.HadithStorage;
import com.indexing.storage.entity.HadithTerm;
import com.indexing.storage.util.HibernateUtil;
import com.ontology.search.OntDictionary;
import com.ontology.search.OntMatcher;
import com.ontology.search.OntTerm;
import com.ontology.util.OntologyLoader;
import com.textprocessing.Stemmer;
import com.textprocessing.util.TextUtility;
import org.hibernate.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IndexingImpl {

    private static List<Path> filePaths = new ArrayList<>();

    private static Set<HadithTerm> hadithTerms = new HashSet<>();

    static {
        try (Stream<Path> paths = Files.walk(Paths.get("tibb"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(filePaths::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void index() {
        for (Path filePath : filePaths) {
            List<String> phrases = new ArrayList<>();
            long docDID = getDocDID(filePath);
            try (Stream<String> stream = Files.lines(Paths.get(filePath.toString()))) {
                // foreach ph in doc
                // eliminateStopWords(ph)
                stream
                        .map(TextUtility::removePunctuations)
                        .forEach(s -> phrases.add(s.trim()));

                Stemmer stemmer = new Stemmer();
                for (String phrase : phrases) {
                    List<String> words = TextUtility.tokenize(phrase);
                    words = TextUtility.removeStopWords(words);
                    // foreach w in ph
                    // stemm(w)
                    words = words.stream()
                            .map(stemmer::findRoot)
                            .collect(Collectors.toList());
                    try {
                        reasoningAndStore(words, docDID);
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        storeTermsInDB();
        System.out.println("Done...");
    }

    private static long getDocDID(Path filePath) {
        String docDID = "";
        String str = filePath.getFileName().toString();
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(str);
        while (m.find()) {
            docDID = m.group(1);
        }
        return Long.parseLong(docDID);
    }

    private static void reasoningAndStore(List<String> words, long docDID) throws SQLException, ClassNotFoundException {
        OntDictionary dictionary = OntologyLoader.loadOntology();
        OntMatcher matcher = new OntMatcher(dictionary);
        for (String word : words) {
            storeIndexes(word, matcher.match(word), docDID);
        }
    }


    private static void storeIndexes(String word, List<OntTerm> ontTerms, long docDID) {
        for (OntTerm term : ontTerms) {
            hadithTerms.add(new HadithTerm(docDID, word, term.getName()));
            for (String synonym : term.getSynonyms()) {
                hadithTerms.add(new HadithTerm(docDID, word, synonym));
            }
        }
    }

    private static void storeTermsInDB() {
        Session session = HibernateUtil.getSession();
        for (HadithTerm term : hadithTerms) {
            HadithStorage.save(term, session);
        }
        session.getTransaction().commit();
        session.close();
    }


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        index();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime / 1000 + " seconds");
    }
}
