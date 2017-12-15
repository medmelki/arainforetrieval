package com.indexing;

import com.indexing.storage.api.HadithStorage;
import com.indexing.storage.entity.HadithTerm;
import com.textprocessing.Stemmer;
import com.textprocessing.util.TextUtility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IndexingImpl {

    private static List<Path> filePaths = new ArrayList<>();

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
        List<String> phrases = new ArrayList<>();
        for (Path filePath : filePaths) {
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

                    // TODO : reasoning Ontology to get CRw

                    storeIndexes(words);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("Done...");
    }

    // TODO : do it in one Hibernate Session
    private static void storeIndexes(List<String> words) {
        words.forEach(e -> HadithStorage.save(new HadithTerm((long) 92, e, e)));
    }


    public static void main(String[] args) {
        index();
    }
}
