package com.example.utselibrary.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;


import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class BorrowedDocument {
    private Date timeBorrowed;
    private Date dueDate;
    private boolean renewed;
    private String documentId;
    Calendar cal = Calendar.getInstance();

    //automatic borrow duration is set for a month.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public BorrowedDocument(String documentId){ ;

        timeBorrowed = Calendar.getInstance().getTime();
        int month = cal.get(Calendar.MONTH);
        cal.set(Calendar.MONTH, month + 1);
        dueDate = cal.getTime();
        renewed = false;
        this.documentId = documentId;
    }

    //empty constructor
    public BorrowedDocument (){

    }

    public Date getTimeBorrowed() {
        return timeBorrowed;
    }

    public void setTimeBorrowed(Date timeBorrowed) {
        this.timeBorrowed = timeBorrowed;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isRenewed() {
        return renewed;
    }

    public void setRenewed(boolean renewed) {
        this.renewed = renewed;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
