package com.ontology.util;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntTools;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.JenaException;

public class ResnikCalculator {

    public static double calcRelatedness(OntModel m, OntClass u, OntClass v) {
        Resource root = m.getProfile().THING();
        if (root == null) {
            throw new JenaException("The given OntModel has a language profile that does not define a generic root class (such as owl:Thing)");
        }
        root = root.inModel(m);
        int num = OntTools.findShortestPath(m, root, OntTools.getLCA(m, u, v), null).size();
        int c1_len = OntTools.findShortestPath(m, root, u, null).size();
        int c2_len = OntTools.findShortestPath(m, root, v, null).size();

        return ((double) (2 * num)) / c1_len + c2_len;
    }
}
