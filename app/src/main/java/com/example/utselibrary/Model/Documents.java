package com.example.utselibrary.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;

public class Documents {
    @PropertyName("title") public String title;
    @PropertyName("ISBN") public String ISBN;
    @PropertyName("documentType") public String documentType;
    @PropertyName("genre") public String genre;
    @PropertyName("internalID") public String internalID;
    @PropertyName("publishLocation") public String publishLocation;
    @PropertyName("publisher") public String publisher;
    @PropertyName("releaseEdition") public String releaseEdition;
    @PropertyName("authors") public ArrayList<Authors> authors;
    @PropertyName("borrowers") public ArrayList<String> borrowers;
    @PropertyName("publicationDate") public Timestamp publicationDate;
    @PropertyName("currentBorrowed") public int currentBorrowed;
    @PropertyName("maxBorrowable") public int maxBorrowable;

    public Documents() {
        // Empty constructor
    }

    public Documents(String title, String ISBN, String documentType, String genre, String internalID, String publishLocation, String publisher, String releaseEdition, ArrayList<Authors> authors, ArrayList<String> borrowers, Timestamp publicationDate, int currentBorrowed, int maxBorrowable) {
        this.title = title;
        this.ISBN = ISBN;
        this.documentType = documentType;
        this.genre = genre;
        this.internalID = internalID;
        this.publishLocation = publishLocation;
        this.publisher = publisher;
        this.releaseEdition = releaseEdition;
        this.authors = authors;
        this.borrowers = borrowers;
        this.publicationDate = publicationDate;
        this.currentBorrowed = currentBorrowed;
        this.maxBorrowable = maxBorrowable;
    }

    @PropertyName("title")
    public String getTitle() {
        return title;
    }

    @PropertyName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @PropertyName("ISBN")
    public String getISBN() {
        return ISBN;
    }

    @PropertyName("ISBN")
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    @PropertyName("documentType")
    public String getDocumentType() {
        return documentType;
    }

    @PropertyName("documentType")
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @PropertyName("genre")
    public String getGenre() {
        return genre;
    }

    @PropertyName("genre")
    public void setGenre(String genre) {
        this.genre = genre;
    }

    @PropertyName("internalID")
    public String getInternalID() {
        return internalID;
    }

    @PropertyName("internalID")
    public void setInternalID(String internalID) {
        this.internalID = internalID;
    }

    @PropertyName("publishLocation")
    public String getPublishLocation() {
        return publishLocation;
    }

    @PropertyName("publishLocation")
    public void setPublishLocation(String publishLocation) {
        this.publishLocation = publishLocation;
    }

    @PropertyName("publisher")
    public String getPublisher() {
        return publisher;
    }

    @PropertyName("publisher")
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @PropertyName("releaseEdition")
    public String getReleaseEdition() {
        return releaseEdition;
    }

    @PropertyName("releaseEdition")
    public void setReleaseEdition(String releaseEdition) {
        this.releaseEdition = releaseEdition;
    }

    @PropertyName("authors")
    public ArrayList<Authors> getAuthors() {
        return authors;
    }

    @PropertyName("authors")
    public void setAuthors(ArrayList<Authors> authors) {
        this.authors = authors;
    }

    @PropertyName("borrowers")
    public ArrayList<String> getBorrowers() {
        return borrowers;
    }

    @PropertyName("borrowers")
    public void setBorrowers(ArrayList<String> borrowers) {
        this.borrowers = borrowers;
    }

    @PropertyName("publicationDate")
    public Timestamp getPublicationDate() {
        return publicationDate;
    }

    @PropertyName("publicationDate")
    public void setPublicationDate(Timestamp publicationDate) {
        this.publicationDate = publicationDate;
    }

    @PropertyName("currentBorrowed")
    public int getCurrentBorrowed() {
        return currentBorrowed;
    }

    @PropertyName("currentBorrowed")
    public void setCurrentBorrowed(int currentBorrowed) {
        this.currentBorrowed = currentBorrowed;
    }

    @PropertyName("maxBorrowable")
    public int getMaxBorrowable() {
        return maxBorrowable;
    }

    @PropertyName("maxBorrowable")
    public void setMaxBorrowable(int maxBorrowable) {
        this.maxBorrowable = maxBorrowable;
    }


}

