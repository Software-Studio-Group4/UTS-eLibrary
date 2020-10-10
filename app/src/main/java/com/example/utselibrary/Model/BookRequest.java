package com.example.utselibrary.Model;

public class BookRequest {
    private static String title;
    private static String primaryAuthor;
    private static String genre;
    private static String publisher;
    private String id;


    private String imageUrl;

    public static String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public static String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    public BookRequest() {
        // Empty constructor
    }

    public BookRequest(String title, String primaryAuthor, String id, String genre, String publisher) {
        this.title = title;
        this.primaryAuthor = primaryAuthor;
        this.id = id;
        this.genre = genre;
        this.publisher = publisher;
    }

    public static String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static String getPrimaryAuthor() {
        return primaryAuthor;
    }

    public void setPrimaryAuthor(String primaryAuthor) {
        this.primaryAuthor =  primaryAuthor;
    }
}
