package com.ontology;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;

public class InfModelFactory {

	public static InfModel createInfModel(OntModel model, Reasoner reasoner){
		return ModelFactory.createInfModel(reasoner, model);
	}

}
