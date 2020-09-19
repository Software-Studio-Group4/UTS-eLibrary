package com.example.utselibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.utselibrary.Model.DocumentModel;
import com.example.utselibrary.Model.Documents;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddBook extends AppCompatActivity {
    EditText titleTf, authorTf, publisherTf;
    Button addButton;
    Spinner genreSpinner;
    ImageView bookImage;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    String imageUrl;

    DatabaseReference documentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        titleTf = findViewById(R.id.bookTitleTf);
        authorTf = findViewById(R.id.authorTf);
        publisherTf = findViewById(R.id.publisherTf);
        addButton = findViewById(R.id.addBtn);
        genreSpinner = findViewById(R.id.genreSpinner);
        bookImage = findViewById(R.id.bookImage);
        documentRef = FirebaseDatabase.getInstance().getReference("documents");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        
        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBook();
            }
        });
    }

    private void addBook() {
        String title = titleTf.getText().toString().trim();
        String author = authorTf.getText().toString().trim();
        String genre = genreSpinner.getSelectedItem().toString();
        String publisher = publisherTf.getText().toString().trim();
        String image = imageUrl;

        String id = documentRef.push().getKey();

        Documents doc = new Documents(title, author, id, genre, publisher, image);

        documentRef.child(id).setValue(doc);

        Toast.makeText(this, "Document Added", Toast.LENGTH_SHORT).show();

        if (TextUtils.isEmpty(title)) {
            titleTf.setError("Please enter title");
            YoYo.with(Techniques.Shake).duration(700).playOn(titleTf);
        }


        if (TextUtils.isEmpty(author)) {
            authorTf.setError("Please enter author");
            YoYo.with(Techniques.Shake).duration(700).playOn(authorTf);
    }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            bookImage.setImageURI(imageUri);
            uploadImage();
        }
    }

    private void uploadImage() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading image...");
        pd.show();

        final String key = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + key);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl = uri.toString();
                                    }
                                });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progressPercentage = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) progressPercentage + "%" );
            }
        });
    }
}