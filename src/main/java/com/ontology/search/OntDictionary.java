package com.ontology.search;

import com.ontology.Config;
import com.ontology.rdfstore.IRDFStore;
import com.textprocessing.Stemmer;
import com.textprocessing.util.TextUtility;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.ArrayList;
import java.util.List;

public class OntDictionary {

    private static Stemmer stemmer = new Stemmer();

    OntModel ontModel;
    IRDFStore rdfStore;
    OntClass categoryClass;


    public OntDictionary(OntModel ontModel, IRDFStore rdfStore) {
        this.ontModel = ontModel;
        this.rdfStore = rdfStore;
        categoryClass = ontModel.getOntClass(Config.baseUri + "category");
    }

    public List<OntTerm> getAllOntTerms() {
        List<OntTerm> allTerms = new ArrayList<OntTerm>();
        allTerms.addAll(getAllCategories());
        allTerms.addAll(getAllIndividuals());
//        allTerms.addAll(getAllProperties());
        return allTerms;
    }

    protected List<Category> getAllCategories() {
        ExtendedIterator<OntClass> iter = categoryClass.listSubClasses();
        List<Category> categories = new ArrayList<Category>();
        while (iter.hasNext()) {
            OntClass subClass = iter.next();
            if (!subClass.isAnon() && subClass.getURI().startsWith(Config.baseUri)) {
                Category cat = new Category();
                cat.setUri(subClass.getURI());
                cat.setName(subClass.getLocalName());
                cat.setName(subClass.getLabel(null));
                cat.setStem(OntDictionary.stemmer.findRoot(subClass.getLocalName()));
                List<String> labels = this.getLabels(subClass);
                if (labels != null) {
                    List<String> stemmedLabels = new ArrayList<>();
                    for (String label : labels) {
                        label = TextUtility.removePunctuations(label);
                        List<String> tokens = TextUtility.tokenize(label);
                        tokens = TextUtility.removeStopWords(tokens);
                        for (String token : tokens) {
                            stemmedLabels.add(OntDictionary.stemmer.findRoot(token));
                        }
                    }
                    cat.setSynonyms(stemmedLabels);
                }
                categories.add(cat);
            }
        }
        return categories;
    }

    protected List<HadithIndividual> getAllIndividuals() {
        ExtendedIterator<Individual> iter = ontModel.listIndividuals();
        List<HadithIndividual> inds = new ArrayList<HadithIndividual>();
        while (iter.hasNext()) {
            Individual i = iter.next();
            if (!i.isAnon() && i.getURI().startsWith(Config.baseUri)) {
                HadithIndividual hInd = new HadithIndividual();
                hInd.setUri(i.getURI());
                hInd.setName(i.getLocalName());
                hInd.setName(i.getLabel(null));
                hInd.setStem(OntDictionary.stemmer.findRoot(i.getLocalName()));
                List<String> labels = this.getLabels(i);
                if (labels != null) {
                    List<String> stemmedLabels = new ArrayList<>();
                    for (String label : labels) {
                        label = TextUtility.removePunctuations(label);
                        List<String> tokens = TextUtility.tokenize(label);
                        tokens = TextUtility.removeStopWords(tokens);
                        for (String token : tokens) {
                            stemmedLabels.add(OntDictionary.stemmer.findRoot(token));
                            stemmedLabels.add(token);
                        }
                    }
                    hInd.setSynonyms(stemmedLabels);
                }
                inds.add(hInd);
            }
        }
        return inds;
    }

    protected List<Relation> getAllProperties() {
        ExtendedIterator<OntProperty> iter = ontModel.listAllOntProperties();
        List<Relation> relations = new ArrayList<Relation>();
        while (iter.hasNext()) {
            OntProperty property = iter.next();
            if (!property.isAnon() && property.getURI().startsWith(Config.baseUri)) {
                Relation r = new Relation();
                r.setUri(property.getURI());
                r.setName(property.getLocalName());
                r.setName(property.getLabel(null));

                r.setStem(OntDictionary.stemmer.findRoot(property.getLocalName()));
                if (property.isObjectProperty())
                    r.setType(Relation.Type.ObjectProperty);
                else
                    r.setType(Relation.Type.DataProperty);
                List<String> labels = this.getLabels(property);
                if (labels != null) {
                    List<String> stemmedLabels = new ArrayList<String>();
                    for (String label : labels)
                        stemmedLabels.add(OntDictionary.stemmer.findRoot(label));
                    r.setSynonyms(stemmedLabels);
                }
                relations.add(r);
            }
        }
        return relations;
    }


    protected List<String> getLabels(Resource r) {
        Property labelProp = ontModel.getAnnotationProperty(Config.rdfsPrefix + "label");
        if (labelProp != null) {
            List<String> synonyms = new ArrayList<String>();
            StmtIterator iter = ontModel.listStatements(r, labelProp, (String) null);
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                String object = stmt.getString();
                if (object != null)
                    synonyms.add(object);
            }
            return synonyms;
        }
        return null;
    }

    public Category getCategory(String uri) {
        Individual individual = ontModel.getIndividual(uri);
        if (individual != null) {
            OntClass parent = individual.getOntClass(true);
            Category category = new Category();
            category.setName(parent.getLocalName());
            category.setName(parent.getLabel(null));
            category.setStem(OntDictionary.stemmer.findRoot(parent.getLocalName()));
            category.setUri(parent.getURI());
            return category;
        }
        return null;
    }

    public List<Category> getCategoryOf(String uri) {
        OntClass c = ontModel.getOntClass(uri);
        List<Category> inds = new ArrayList<Category>();
        ExtendedIterator<OntClass> iter = c.listSubClasses(true);
        while (iter.hasNext()) {
            OntClass ind = iter.next();
            Category hd = new Category();
            hd.setName(ind.getLocalName());
            hd.setName(ind.getLabel(null));
            hd.setUri(ind.getURI());
            List<String> labels = this.getLabels(ind);
            if (labels != null) {
                List<String> stemmedLabels = new ArrayList<String>();
                for (String label : labels)
                    stemmedLabels.add(OntDictionary.stemmer.findRoot(label));
                hd.setSynonyms(stemmedLabels);
            }

            inds.add(hd);
        }
        return inds;
    }

    public List<HadithIndividual> getIndividualsOf(String uri) {
        OntClass c = ontModel.getOntClass(uri);
        List<HadithIndividual> inds = new ArrayList<HadithIndividual>();
        ExtendedIterator<Individual> iter = ontModel.listIndividuals(c);
        while (iter.hasNext()) {
            Individual ind = iter.next();
            HadithIndividual hd = new HadithIndividual();
            hd.setName(ind.getLocalName());
            hd.setName(ind.getLabel(null));
            hd.setUri(ind.getURI());
            inds.add(hd);
        }
        return inds;
    }
} 
