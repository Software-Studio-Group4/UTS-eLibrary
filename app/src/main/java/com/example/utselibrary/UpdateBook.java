package com.example.utselibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UpdateBook extends AppCompatActivity {
    EditText titleTf, authorTf, publisherTf, publishedYearTf, numOfCopiesTf, genreTf, descriptionTf, ISBNTf;
    Button addButton;
    TextView documentIDTf;
    ImageView bookImage;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    String imageUrl;
    FirebaseFirestore fStore;

   DocumentReference documentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        Intent receive_i = getIntent();
        Bundle my_bundle_received = receive_i.getExtras();
        my_bundle_received.get("item1");
        String id =  my_bundle_received.get("item1").toString();

        titleTf = findViewById(R.id.bookTitleTf);
        authorTf = findViewById(R.id.authorTf);
        publisherTf = findViewById(R.id.publisherTf);
        documentIDTf = findViewById(R.id.documentIDTf);
        publishedYearTf = findViewById(R.id.publishedYearTf);
        numOfCopiesTf = findViewById(R.id.numOfCopiesTf);
        genreTf = findViewById(R.id.genreTf);
        descriptionTf = findViewById(R.id.descriptionTf);
        addButton = findViewById(R.id.addBtn);
        bookImage = findViewById(R.id.bookImage);
        ISBNTf = findViewById(R.id.ISBNTf);

        documentRef = FirebaseFirestore.getInstance().collection("Documents").document(id);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String docID = documentSnapshot.getString("objectID");
                    String title = documentSnapshot.getString("title");
                    String author = documentSnapshot.getString("author");
                    String publisher = documentSnapshot.getString("publisher");
                    String publishedYear = documentSnapshot.getString("publishedYear");
                    long numofCopies = documentSnapshot.getLong("borrowLimit");
                    String desc = documentSnapshot.getString("description");
                    String genre = documentSnapshot.getString("genre");
                    imageUrl = documentSnapshot.getString("coverImageUrl");
                    String ISBN = documentSnapshot.getString("ISBN");
                    documentIDTf.setText(docID);
                    titleTf.setText(title);
                    authorTf.setText(author);
                    publisherTf.setText(publisher);
                    publishedYearTf.setText(publishedYear);
                    numOfCopiesTf.setText("" + numofCopies);
                    descriptionTf.setText(desc);
                    genreTf.setText(genre);
                    ISBNTf.setText(ISBN);
                    Picasso.get().load(imageUrl).into(bookImage);
                }
            }
        });




        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addBook();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addBook() throws JSONException {
        fStore = FirebaseFirestore.getInstance();
        final String title = titleTf.getText().toString().trim();
        final String author = authorTf.getText().toString().trim();
        final String genre = genreTf.getText().toString().trim();
        final String publisher = publisherTf.getText().toString().trim();
        final String algoliaId = documentIDTf.getText().toString().trim();
        final String publishedYear = publishedYearTf.getText().toString().trim();
        final String borrowLimitString = numOfCopiesTf.getText().toString().trim();
        final String description = descriptionTf.getText().toString().trim();
        final String ISBN = ISBNTf.getText().toString().trim();
        final String image = imageUrl;
        ArrayList<String> borrowers = new ArrayList<String>();
        final int borrowLimit;

        if (TextUtils.isEmpty(algoliaId)) {
            documentIDTf.setError("Please enter document ID");
            YoYo.with(Techniques.Shake).duration(700).playOn(documentIDTf);
            return;
        }

        if (TextUtils.isEmpty(title)) {
            titleTf.setError("Please enter document title");
            YoYo.with(Techniques.Shake).duration(700).playOn(titleTf);
            return;
        }
        if (TextUtils.isEmpty(ISBN)) {
            titleTf.setError("Please enter ISBN");
            YoYo.with(Techniques.Shake).duration(700).playOn(titleTf);
            return;
        }

        if (TextUtils.isEmpty(author)) {
            authorTf.setError("Please enter document author");
            YoYo.with(Techniques.Shake).duration(700).playOn(authorTf);
            return;
        }

        if (TextUtils.isEmpty(genre)) {
            genreTf.setError("Please enter document genre");
            YoYo.with(Techniques.Shake).duration(700).playOn(genreTf);
            return;
        }

        if (TextUtils.isEmpty(publisher)) {
            publisherTf.setError("Please enter document publisher");
            YoYo.with(Techniques.Shake).duration(700).playOn(publisherTf);
            return;
        }

        if (TextUtils.isEmpty(publishedYear)) {
            publishedYearTf.setError("Please enter document publishing year");
            YoYo.with(Techniques.Shake).duration(700).playOn(publishedYearTf);
            return;
        }

        if (TextUtils.isEmpty(borrowLimitString)) {
            numOfCopiesTf.setError("Please enter No. of document copies");
            YoYo.with(Techniques.Shake).duration(700).playOn(genreTf);
            return;
        } else {
            borrowLimit = Integer.parseInt(borrowLimitString);
        }

        if (TextUtils.isEmpty(description)) {
            descriptionTf.setError("Please enter document description");
            YoYo.with(Techniques.Shake).duration(700).playOn(descriptionTf);
            return;
        }

        final Map<String, Object> documentMap = new HashMap<>();
        // Form-filled fields
        documentMap.put("title", title);
        documentMap.put("author", author);
        documentMap.put("genre", genre);
        documentMap.put("publisher", publisher);
        documentMap.put("publishedYear", publishedYear);
        documentMap.put("coverImageUrl", image);
        documentMap.put("objectID", algoliaId);
        documentMap.put("borrowLimit", borrowLimit);
        documentMap.put("description", description);
        documentMap.put("ISBN", ISBN);

        documentRef.update(documentMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Client client = new Client("9L80XXFOLT", "a01b448ff9270562e195ef32110d829a");
                Index index = client.getIndex("Documents");

                List<JSONObject> array = new ArrayList<JSONObject>();

                try {
                    index.partialUpdateObjectAsync(
                            new JSONObject().put("title", title),
                            algoliaId,
                            null
                    );
                    index.partialUpdateObjectAsync(
                            new JSONObject().put("author", author),
                            algoliaId,
                            null
                    );
                    index.partialUpdateObjectAsync(
                            new JSONObject().put("genre", genre),
                            algoliaId,
                            null
                    );
                    index.partialUpdateObjectAsync(
                            new JSONObject().put("publisher", publisher),
                            algoliaId,
                            null
                    );
                    index.partialUpdateObjectAsync(
                            new JSONObject().put("publishedYear", publishedYear),
                            algoliaId,
                            null
                    );
                    index.partialUpdateObjectAsync(
                            new JSONObject().put("coverImageUrl", image),
                            algoliaId,
                            null
                    );
                    index.partialUpdateObjectAsync(
                            new JSONObject().put("borrowLimit", borrowLimit),
                            algoliaId,
                            null
                    );
                    index.partialUpdateObjectAsync(
                            new JSONObject().put("description", description),
                            algoliaId,
                            null
                    );
                    index.partialUpdateObjectAsync(
                            new JSONObject().put("ISBN", ISBN),
                            algoliaId,
                            null
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Document updated", Toast.LENGTH_SHORT).show();
            }
        });
        finish();
            }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
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
                pd.setMessage("Progress: " + (int) progressPercentage + "%");
            }
        });
    }
    }
