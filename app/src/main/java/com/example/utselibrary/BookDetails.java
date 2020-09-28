package com.example.utselibrary;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BookDetails extends AppCompatActivity {

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference documentReference = fStore.collection("Documents");
    String bookID = "BK01";
    TextView titleTf, authorTf, typeTf, genreTf, publisherTf, pubDateTf, pubLocTf, volTf, isbnTf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        Query appointmentQuery = documentReference.whereEqualTo("internalID", bookID);

        titleTf = findViewById(R.id.titleTf);
        authorTf = findViewById(R.id.authorTf);
        typeTf = findViewById(R.id.typeTf);
        genreTf = findViewById(R.id.genreTf);
        publisherTf = findViewById(R.id.publishTf);
        pubDateTf = findViewById(R.id.dateTf);
        pubLocTf = findViewById(R.id.locTf);
        volTf = findViewById(R.id.volTf);
        isbnTf = findViewById(R.id.iSBNTf);
    }
}