package com.search.entity;

public class HadithResult implements Comparable<HadithResult> {

    private String content;
    private double similarity;

    public HadithResult(String content) {
        this.content = content;
    }

    public HadithResult(String content, double similarity) {
        this.content = content;
        this.similarity = similarity;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }


    @Override
    public int compareTo(HadithResult o) {
        return Double.compare(similarity, o.similarity);
    }
}
