package com.example.utselibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BookRequest extends AppCompatActivity {
    private EditText titleTf, authorTf, genreTf, pubTf;
    private Button backBtn, requestBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request);

        titleTf = findViewById(R.id.titleText);
        authorTf = findViewById(R.id.authorTf);
        pubTf = findViewById(R.id.pubTf);
        genreTf = findViewById(R.id.genreTf);
        requestBtn = findViewById(R.id.requestBtn);
        backBtn = findViewById(R.id.backBtn);

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestBook();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDashboard.class));
            }
        });

    }

    private void requestBook() {
        String title = titleTf.getText().toString().trim();
        String author = authorTf.getText().toString().trim();
        String publisher = pubTf.getText().toString().trim();
        String genre = genreTf.getText().toString().trim();



        if (TextUtils.isEmpty(title)) {
            titleTf.setError("Please enter title");
            YoYo.with(Techniques.Shake).duration(700).playOn(titleTf);
        }
        if (TextUtils.isEmpty(author)) {
            authorTf.setError("Please enter author");
            YoYo.with(Techniques.Shake).duration(700).playOn(authorTf);
        }
        if (TextUtils.isEmpty(publisher)) {
            pubTf.setError("Please enter publisher");
            YoYo.with(Techniques.Shake).duration(700).playOn(pubTf);
        }
        if (TextUtils.isEmpty(genre)) {
            genreTf.setError("Please enter genre");
            YoYo.with(Techniques.Shake).duration(700).playOn(genreTf);
        } else {

            FirebaseFirestore fStore = FirebaseFirestore.getInstance();
            DocumentReference bookRef = fStore.collection("BookRequests").document();

            Map<String, Object> bookRequest = new HashMap<>();

            bookRequest.put("title", title);
            bookRequest.put("author", author);
            bookRequest.put("publisher", publisher);
            bookRequest.put("genre", genre);

            bookRef.set(bookRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(BookRequest.this, "Request Submitted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BookRequest.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }
}