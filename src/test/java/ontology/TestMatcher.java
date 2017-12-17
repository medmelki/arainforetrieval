package ontology;

import com.ontology.Config;
import com.ontology.modelloader.FileOntModelLoader;
import com.ontology.rdfstore.MySQLRDFStore;
import com.ontology.search.OntDictionary;
import com.ontology.search.OntMatcher;
import com.ontology.search.OntTerm;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class TestMatcher {


    @Test
    public void testMatcher() throws SQLException, ClassNotFoundException {

        FileOntModelLoader foml = new FileOntModelLoader();
        OntModel ontModel = foml.getOntModel(Config.ontologyFile);

        //connect to the RDF store (Database)
        MySQLRDFStore rdfStore = new MySQLRDFStore(Config.DB_URL, Config.DB_USER, Config.DB_PASSWD);

        rdfStore.getModel();

        OntDictionary dictionary = new OntDictionary(ontModel, rdfStore);


        // TODO : change to pure parent class
        OntClass c = ontModel.getOntClass("http://www.semanticweb.org/iyad32/ontologies/2014/6/untitled-ontology-13.owl#Cures");
        ExtendedIterator classes = c.listSubClasses();
        while (classes.hasNext()) {
            OntClass essaClasse = (OntClass) classes.next();
            String vClasse = essaClasse.getLocalName();
            if (essaClasse.hasSubClass()) {
                System.out.println("Classe: " + vClasse);
                OntClass cla = ontModel.getOntClass(Config.baseUri + vClasse);
                for (Iterator i = cla.listSubClasses(true); i.hasNext(); ) {
                    OntClass cc = (OntClass) i.next();
//                    System.out.print("   " + cc.getLocalName() + " " + "\n");
                }
            }
        }

        String query = "عسل";
        OntMatcher matcher = new OntMatcher(dictionary);
        // TODO : replace with qwords
        List<OntTerm> terms = matcher.match(query);

        for (OntTerm term : terms) {
            System.out.println(term.getName());
        }
    }
}
