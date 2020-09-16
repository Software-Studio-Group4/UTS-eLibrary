package com.example.utselibrary.Model;

import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentModel {

    private String title;
    private String primaryAuthor;

    public DocumentModel() {
        // Empty constructor
    }

    public DocumentModel(String title, String primaryAuthor) {
        this.title = title;
        this.primaryAuthor = primaryAuthor;
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

}
