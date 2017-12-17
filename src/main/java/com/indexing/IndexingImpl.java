package com.indexing;

import com.indexing.storage.api.HadithStorage;
import com.indexing.storage.entity.HadithTerm;
import com.indexing.storage.util.HibernateUtil;
import com.ontology.Config;
import com.ontology.modelloader.FileOntModelLoader;
import com.ontology.rdfstore.MySQLRDFStore;
import com.ontology.search.OntDictionary;
import com.ontology.search.OntMatcher;
import com.ontology.search.OntTerm;
import com.textprocessing.Stemmer;
import com.textprocessing.util.TextUtility;
import org.apache.jena.ontology.OntModel;
import org.hibernate.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        Session session = HibernateUtil.getSession();
        List<String> phrases = new ArrayList<>();
        for (Path filePath : filePaths) {
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
                        reasoningAndStore(words, docDID, session);
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        session.getTransaction().commit();
        session.close();
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

    private static void reasoningAndStore(List<String> words, long docDID, Session session) throws SQLException, ClassNotFoundException {
        OntDictionary dictionary = loadOntology();
        OntMatcher matcher = new OntMatcher(dictionary);
        for (String word : words) {
            storeIndexes(word, matcher.match(word), docDID, session);
        }
    }

    private static OntDictionary loadOntology() throws ClassNotFoundException, SQLException {
        FileOntModelLoader foml = new FileOntModelLoader();
        OntModel ontModel = foml.getOntModel(Config.ontologyFile);

        //connect to the RDF store (Database)
        MySQLRDFStore rdfStore = new MySQLRDFStore(Config.DB_URL, Config.DB_USER, Config.DB_PASSWD);

        rdfStore.getModel();

        return new OntDictionary(ontModel, rdfStore);
    }

    private static void storeIndexes(String word, List<OntTerm> ontTerms, long docDID, Session session) {
        for (OntTerm term : ontTerms) {
            HadithStorage.save(new HadithTerm(docDID, term.getName(), word), session);
        }
    }


    public static void main(String[] args) {
        index();
    }
}
