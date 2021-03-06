package com.indexing.storage.entity;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Indexed
@Entity
@Table(name = "hadith_terms")
public class HadithTerm implements Serializable {

    @Id
    private String id;

    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.NO)
    @Column(name = "DIDdoc")
    private Long DIDdoc;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(name = "term")
    private String term;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(name = "cr")
    private String cr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
                "id=" + id +
                ", DIDdoc=" + DIDdoc +
                ", term='" + term + '\'' +
                ", cr='" + cr + '\'' +
                '}';
    }

    public HadithTerm() {
    }

    public HadithTerm(Long DIDdoc, String term, String cr) {
        this.id = UUID.randomUUID().toString();
        this.DIDdoc = DIDdoc;
        this.term = term;
        this.cr = cr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HadithTerm that = (HadithTerm) o;

        if (DIDdoc != null ? !DIDdoc.equals(that.DIDdoc) : that.DIDdoc != null) return false;
        if (term != null ? !term.equals(that.term) : that.term != null) return false;
        return cr != null ? cr.equals(that.cr) : that.cr == null;
    }

    @Override
    public int hashCode() {
        int result = DIDdoc != null ? DIDdoc.hashCode() : 0;
        result = 31 * result + (term != null ? term.hashCode() : 0);
        result = 31 * result + (cr != null ? cr.hashCode() : 0);
        return result;
    }
}
