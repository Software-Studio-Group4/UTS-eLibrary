package com.example.utselibrary.Model;

import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentModel {

    private String title;
    private String primaryAuthor;
    private String coverImageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    private String genre;

    public DocumentModel() {
        // Empty constructor
    }

    public DocumentModel(String title, String primaryAuthor, String coverImageUrl, String id, String genre) {
        this.title = title;
        this.primaryAuthor = primaryAuthor;
        this.coverImageUrl = coverImageUrl;
        this.id = id;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrimaryAuthor() {
        return primaryAuthor;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

}
