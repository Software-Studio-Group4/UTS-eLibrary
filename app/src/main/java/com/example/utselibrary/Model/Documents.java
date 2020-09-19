
package com.example.utselibrary.Model;

public class Documents {

    private String title;
    private String primaryAuthor;
    private String genre;
    private String id;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    private String publisher;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    public Documents() {
        // Empty constructor
    }

    public Documents(String title, String primaryAuthor, String id, String genre, String publisher, String imageUrl) {
        this.title = title;
        this.primaryAuthor = primaryAuthor;
        this.id = id;
        this.genre = genre;
        this.publisher = publisher;
        this.imageUrl = imageUrl;
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



