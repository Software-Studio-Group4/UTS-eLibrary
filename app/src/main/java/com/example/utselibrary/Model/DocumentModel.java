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

    public DocumentModel() {
        // Empty constructor
    }

    public DocumentModel(String title, String primaryAuthor, String coverImageUrl) {
        this.title = title;
        this.primaryAuthor = primaryAuthor;
        this.coverImageUrl = coverImageUrl;
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
