package com.ontology.rdfstore;


import org.apache.jena.rdf.model.Model;

public interface IRDFStore {

    public void storeRDFModel(Model dataModel);

    public Model getModel();

    public void clearStore();

    public void closeStore();
}
