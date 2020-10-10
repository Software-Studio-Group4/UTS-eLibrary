package com.example.utselibrary.Model;

import android.util.Log;
import android.util.Pair;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class User {
    int borrowAmount;
    List<BorrowedDocument> borrowHistory = new ArrayList<BorrowedDocument>(); //pair of borrow time + bookID
    List<BorrowedDocument> borrowedDocs = new ArrayList<BorrowedDocument>();
    String emailAddress;
    String firstName;
    String lastName;
    boolean isLecturer;
    boolean isSuspended;
    int maxAllowed;
    String phoneNumber;
    String uniID;


    public User() {
//empty constructor
    }

    public User(int borrowAmount) {
        this.borrowAmount = borrowAmount;
    }


    public int getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(int borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public List<BorrowedDocument> getBorrowHistory() {
        return borrowHistory;
    }

    public void setBorrowHistory(List<BorrowedDocument> borrowHistory) {
        this.borrowHistory = borrowHistory;
    }

    public List<BorrowedDocument> getBorrowedDocs() {
        return borrowedDocs;
    }

    public void setBorrowedDocs(List<BorrowedDocument> borrowedDocs) {
        this.borrowedDocs = borrowedDocs;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isLecturer() {
        return isLecturer;
    }

    public void setLecturer(boolean lecturer) {
        isLecturer = lecturer;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public int getMaxAllowed() {
        return maxAllowed;
    }

    public void setMaxAllowed(int maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUniID() {
        return uniID;
    }

    public void setUniID(String uniID) {
        this.uniID = uniID;
    }


    public boolean hasBook(String id){
        for(BorrowedDocument borrowedDoc : borrowedDocs){
            Log.d("DEBUGGER", "checking to for " + id  + " against " + borrowedDoc.getDocumentId());
            if(borrowedDoc.getDocumentId().equals(id)){

                return true;
            }
        }
        return false;
    }

    public void returnBook(String id){
        Iterator itr = borrowedDocs.iterator();
        while(itr.hasNext()){
            while(itr.hasNext()){
                BorrowedDocument doc = (BorrowedDocument) itr.next();
                if(doc.getDocumentId().equals(id)){
                    itr.remove();
                }
            }
        }
    }
}
