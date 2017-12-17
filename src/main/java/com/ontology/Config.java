package com.ontology;

public class Config {

	public static String curDir = System.getProperty("user.dir");

	public static final String ontologyFile = "onto/Hadith2.owl";
	public static final String baseUri = "http://www.semanticweb.org/iyad32/ontologies/2014/6/untitled-ontology-13.owl#";
	public static final String rdfsPrefix = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String rdfPrefix = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	
	
	public static String DB_URL = "jdbc:mysql://localhost/hadithdb";
    public static String DB_USER = "root";
    public static String DB_PASSWD = "root";
}
