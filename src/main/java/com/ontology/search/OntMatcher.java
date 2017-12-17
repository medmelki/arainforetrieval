package com.ontology.search;

import com.textprocessing.Stemmer;

import java.util.ArrayList;
import java.util.List;

public class OntMatcher {

    private List<OntTerm> allTerms;
    private static Stemmer stemmer;

    static {
        stemmer = new Stemmer();
    }

    public OntMatcher(OntDictionary dictionary) {
        allTerms = dictionary.getAllOntTerms();
    }

    public List<OntTerm> match(String queryText) {
        // TODO : delete when applying before stemming
        String StemmedQueryText = OntMatcher.stemmer.findRoot(queryText);
        List<OntTerm> results = new ArrayList<OntTerm>();
        for (OntTerm term : allTerms) {
            if (term.match(StemmedQueryText))
                results.add(term);
        }
        return results;
    }


    public OntTerm getOntTerm(String uri) {
        for (OntTerm t : allTerms) {
            if (t.getUri().equalsIgnoreCase(uri))

                return t;
        }
        return null;
    }
}


