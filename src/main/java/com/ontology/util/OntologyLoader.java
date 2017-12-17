package com.ontology.util;

import com.ontology.Config;
import com.ontology.modelloader.FileOntModelLoader;
import com.ontology.rdfstore.MySQLRDFStore;
import com.ontology.search.OntDictionary;
import org.apache.jena.ontology.OntModel;

import java.sql.SQLException;

public class OntologyLoader {

    public static OntDictionary loadOntology() throws ClassNotFoundException, SQLException {
        FileOntModelLoader foml = new FileOntModelLoader();
        OntModel ontModel = foml.getOntModel(Config.ontologyFile);

        //connect to the RDF store (Database)
        MySQLRDFStore rdfStore = new MySQLRDFStore(Config.DB_URL, Config.DB_USER, Config.DB_PASSWD);

        rdfStore.getModel();

        return new OntDictionary(ontModel, rdfStore);
    }
}
