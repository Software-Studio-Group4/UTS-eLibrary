package com.example.utselibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.utselibrary.Model.Documents;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BookRequest extends AppCompatActivity {
    private EditText titleTf, authorTf, typeTf, genreTf, pubTf, locTf, dateTf, volTf;
    private Button backBtn, requestBtn;

    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference documentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request);

        titleTf = findViewById(R.id.titleTf);
        authorTf = findViewById(R.id.authorTf);
        typeTf = findViewById(R.id.typeTf);
        genreTf = findViewById(R.id.genreTf);
        /*
        pubTf = findViewById(R.id.pubTf);
        locTf = findViewById(R.id.locTf);
        dateTf = findViewById(R.id.dateTf);
        volTf = findViewById(R.id.volTf);
        */

        documentRef = FirebaseDatabase.getInstance().getReference("bookrequests");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestBook();
            }
        });
    }

    private void requestBook() {
        String title = titleTf.getText().toString().trim();
        String author = authorTf.getText().toString().trim();
        String type = typeTf.getText().toString().trim();
        String genre = genreTf.getText().toString().trim();
        /*
        String publisher = pubTf.getText().toString().trim();
        String location = locTf.getText().toString().trim();
        String date = dateTf.getText().toString().trim();
        String volume = volTf.getText().toString().trim();
        */

        /*
        String id = documentRef.push().getKey();

        BookRequest br = new BookRequest(title, author, type, genre);

        documentRef.child(id).setValue(br);
        */

        Toast.makeText(this, "Request Submitted", Toast.LENGTH_SHORT).show();

        if (TextUtils.isEmpty(title)) {
            titleTf.setError("Please enter title");
            YoYo.with(Techniques.Shake).duration(700).playOn(titleTf);
        }
        if (TextUtils.isEmpty(author)) {
            authorTf.setError("Please enter author");
            YoYo.with(Techniques.Shake).duration(700).playOn(authorTf);
        }
        if (TextUtils.isEmpty(type)) {
            typeTf.setError("Please enter type");
            YoYo.with(Techniques.Shake).duration(700).playOn(typeTf);
        }
        if (TextUtils.isEmpty(genre)) {
            genreTf.setError("Please enter genre");
            YoYo.with(Techniques.Shake).duration(700).playOn(genreTf);
        }
        /*
        if (TextUtils.isEmpty(publisher)) {
            pubTf.setError("Please enter title");
            YoYo.with(Techniques.Shake).duration(700).playOn(pubTf);
        }
        if (TextUtils.isEmpty(location)) {
            locTf.setError("Please enter title");
            YoYo.with(Techniques.Shake).duration(700).playOn(locTf);
        }
        if (TextUtils.isEmpty(date)) {
            dateTf.setError("Please enter title");
            YoYo.with(Techniques.Shake).duration(700).playOn(dateTf);
        }
        if (TextUtils.isEmpty(volume)) {
            volTf.setError("Please enter title");
            YoYo.with(Techniques.Shake).duration(700).playOn(volTf);
        }
        */
    }
}