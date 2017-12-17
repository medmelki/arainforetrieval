package com.ontology.search;

import java.util.List;

public abstract class OntTerm {

    protected String uri;
    protected String name;
    protected String stem;
    protected List<String> synonyms;


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }


    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public boolean match(String stemmedQueryText) {
        if (this.getName() != null && this.getStem().equalsIgnoreCase(stemmedQueryText))
            return true;
        else
            return matchSynonyms(stemmedQueryText);
    }

    private boolean matchSynonyms(String queryText) {
        if (this.getSynonyms() != null && !this.getSynonyms().isEmpty()) {
            for (String label : this.getSynonyms()) {
                if (queryText.equalsIgnoreCase(label))
                    return true;
            }
        }
        return false;
    }
}
