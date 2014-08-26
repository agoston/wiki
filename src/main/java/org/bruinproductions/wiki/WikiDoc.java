package org.bruinproductions.wiki;

public class WikiDoc<T> {
    private T document;
    private int revision;

    public WikiDoc() {}

    public WikiDoc(T document, int revision) {
        this.document = document;
        this.revision = revision;
    }

    public T getDocument() {
        return document;
    }

    public void setDocument(T document) {
        this.document = document;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }
}
