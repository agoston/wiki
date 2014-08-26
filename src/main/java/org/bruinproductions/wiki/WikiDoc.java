package org.bruinproductions.wiki;

public class WikiDoc<T> {
    private T document;

    public WikiDoc() {}

    public WikiDoc(T document) {
        this.document = document;
    }

    public T getDocument() {
        return document;
    }

    public void setDocument(T document) {
        this.document = document;
    }
}
