
package com.example.utselibrary.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Documents {

    private String title;
    private String author;
    private String genre;
    private String id;
    private String publisher;
    private String coverImageUrl;
    private String publishedYear;
    private String description;
    private int borrowLimit;
    private List<String> borrowers = new ArrayList<String>();

    public Documents() {
        // Empty constructor
    }

    public Documents(String title, String author, String id, String genre, String publisher, String coverImageUrl, String description, int borrowLimit, String publishedYear, String[] borrowers) {
        this.title = title;
        this.author = author;
        this.id = id;
        this.genre = genre;
        this.publisher = publisher;
        this.coverImageUrl = coverImageUrl;
        this.borrowLimit = borrowLimit;
        this.publishedYear = publishedYear;
        this.description = description;
        for(int i = 0; i < borrowers.length; i++) {
            this.borrowers.add(borrowers[i]);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String imageUrl) {
        this.coverImageUrl = imageUrl;
    }

    public int getBorrowLimit() {
        return borrowLimit;
    }

    public void setBorrowLimit(int borrowLimit) {
        this.borrowLimit = borrowLimit;
    }

    public String getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(String publishedYear) {
        this.publishedYear = publishedYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getBorrowers() {
        return borrowers;
    }

    public void setBorrowers(List<String> borrowers) {
        this.borrowers = borrowers;
    }

    public void removeBorrower(String id){
        Iterator itr = borrowers.iterator();
        while(itr.hasNext()){
            String user = (String) itr.next();
            if(user.equals(id)){
                itr.remove();
            }
        }
    }

}



