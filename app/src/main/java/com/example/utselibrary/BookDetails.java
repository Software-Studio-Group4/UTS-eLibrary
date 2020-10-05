package com.example.utselibrary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class BookDetails extends AppCompatActivity {

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    CollectionReference cRef = fStore.collection("Documents");
    TextView titleTf, authorTf, genreTf, publisherTf;
    ImageView bookCover;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("pos");

        titleTf = findViewById(R.id.titleTf);
        authorTf = findViewById(R.id.authorTf);
        genreTf = findViewById(R.id.genreTf);
        publisherTf = findViewById(R.id.publisherTf);
        bookCover = findViewById(R.id.bookCover);
        backBtn = findViewById(R.id.backBtn);

        final DocumentReference documentReference = cRef.document(id);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String title = documentSnapshot.getString("title");
                    String author = documentSnapshot.getString("author");
                    String genre = documentSnapshot.getString("genre");
                    String publisher = documentSnapshot.getString("publisher");
                    String bookCoverUrl = documentSnapshot.getString("coverImageUrl");


                    titleTf.setText(title);
                    authorTf.setText(author);
                    genreTf.setText(genre);
                    publisherTf.setText(publisher);
                    Picasso.get().load(bookCoverUrl).into(bookCover);
                } else {
                    Toast.makeText(BookDetails.this, "Error: Details cannot be displayed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BookDetails.this, "Error: Details cannot be displayed", Toast.LENGTH_SHORT).show();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), UserDashboard.class));
            }
        });

    }
}