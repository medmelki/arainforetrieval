package com.indexing.storage.entity;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Indexed
@Entity
@Table(name = "hadith_terms")
public class HadithTerm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long DIDdoc;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(name = "term")
    private String term;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(name = "cr")
    private String cr;

    public Long getDIDdoc() {
        return DIDdoc;
    }

    public void setDIDdoc(Long DIDdoc) {
        this.DIDdoc = DIDdoc;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getCr() {
        return cr;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    @Override
    public String toString() {
        return "HadithTerm{" +
                "DIDdoc=" + DIDdoc +
                ", term='" + term + '\'' +
                ", cr='" + cr + '\'' +
                '}';
    }
}
