
package com.example.utselibrary.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Documents {

    private String title;
    private String primaryAuthor;
    private String genre;
    private String id;
    private String publisher;
    private String coverImageUrl;
    private int borrowLimit;
    private List<String> borrowers = new ArrayList<String>();

    public Documents() {
        // Empty constructor
    }

    public Documents(String title, String primaryAuthor, String id, String genre, String publisher, String coverImageUrl, int borrowLimit, String[] borrowers) {
        this.title = title;
        this.primaryAuthor = primaryAuthor;
        this.id = id;
        this.genre = genre;
        this.publisher = publisher;
        this.coverImageUrl = coverImageUrl;
        this.borrowLimit = borrowLimit;
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

    public String getPrimaryAuthor() {
        return primaryAuthor;
    }

    public void setPrimaryAuthor(String primaryAuthor) {
        this.primaryAuthor = primaryAuthor;
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



