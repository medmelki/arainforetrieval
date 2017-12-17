package com.ontology.search;

public class Relation extends OntTerm {

    public enum Type {ObjectProperty, DataProperty}

    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
