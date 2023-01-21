package model;

import java.util.LinkedList;
import java.util.List;

public class Book {
    private String title;
    private String author;
    private LinkedList<String> tags;

    public Book(String title, String author, LinkedList<String> tags) {
        this.title = title;
        this.author = author;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public LinkedList<String> getTags() {
        return tags;
    }

    public String getAuthor() {
        return author;
    }
}
