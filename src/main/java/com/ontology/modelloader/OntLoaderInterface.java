package com.ontology.modelloader;

import org.apache.jena.ontology.OntModel;

public interface OntLoaderInterface {

	public OntModel getOntModel(String ontologyPath);
	public OntModel getOntModel(String ontologyPath, String dataPath);

}
