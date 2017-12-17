package com.ontology.modelloader;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

public class FileOntModelLoader implements OntLoaderInterface {

    public OntModel getOntModel(String ontologyPath) {
        return getOntModel(ontologyPath, null);
    }

    public OntModel getOntModel(String ontologyPath, String dataPath) {
        if (ontologyPath == null)
            return null;
        OntModel schemaModel, dataModel;
        schemaModel = ModelFactory.createOntologyModel();
        FileManager.get().readModel(schemaModel, ontologyPath);
        if (dataPath != null) {
            dataModel = ModelFactory.createOntologyModel();
            FileManager.get().readModel(dataModel, dataPath);
            dataModel.add(schemaModel);
            schemaModel.add(dataModel);
        }
        return schemaModel;
    }

}
