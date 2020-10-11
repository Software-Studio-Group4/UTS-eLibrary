package com.example.utselibrary.Model;

public class BookRequestModel {
    private  String title;
    private  String author;
    private  String genre;
    private  String publisher;
    private String id;


    private String imageUrl;

    public  String getPublisher() {
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



    public  String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    public BookRequestModel() {
        // Empty constructor
    }

    public BookRequestModel(String title, String author, String id, String genre, String publisher) {
        this.title = title;
        this.author = author;
        this.id = id;
        this.genre = genre;
        this.publisher = publisher;
    }

    public  String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public  String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author =  author;
    }
}
