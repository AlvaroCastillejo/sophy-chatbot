package model;

import java.util.LinkedList;

public class Frase {
    private String frase;
    private LinkedList<String> tags;

    public Frase(String line, LinkedList<String> tags) {
        frase = line;
        this.tags = tags;
    }

    public String getFrase() {
        return frase;
    }

    public LinkedList<String> getTags() {
        return tags;
    }
}
