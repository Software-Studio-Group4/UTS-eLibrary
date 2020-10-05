package com.example.utselibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBookFragment extends Fragment {

    EditText titleTf, authorTf, publisherTf;
    Button addButton;
    Spinner genreSpinner;
    ImageView bookImage;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    String imageUrl;
    FirebaseFirestore fStore;

    DatabaseReference documentRef;

    
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBookFragment newInstance(String param1, String param2) {
        AddBookFragment fragment = new AddBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        titleTf = getView().findViewById(R.id.bookTitleTf);
        authorTf = getView().findViewById(R.id.authorTf);
        publisherTf = getView().findViewById(R.id.publisherTf);
        addButton = getView().findViewById(R.id.addBtn);
        genreSpinner = getView().findViewById(R.id.genreSpinner);
        bookImage = getView().findViewById(R.id.bookImage);
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
        final String genre = genreSpinner.getSelectedItem().toString();
        final String publisher = publisherTf.getText().toString().trim();
        final String image = imageUrl;

        final Map<String, String> documentMap = new HashMap<>();

        documentMap.put("title", title);
        documentMap.put("author", author);
        documentMap.put("genre", genre);
        documentMap.put("publisher", publisher);
        documentMap.put("coverImageUrl", image);


        fStore.collection("Documents").add(documentMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();
                Client client = new Client("9L80XXFOLT", "a01b448ff9270562e195ef32110d829a");
                Index index = client.getIndex("Documents");

                List<JSONObject> array = new ArrayList<JSONObject>();

                try {
                    array.add(
                            new JSONObject().put("title", title).put("author", author).put("genre", genre).put("publisher", publisher).put("coverImageUrl", image).put("id", id)
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                index.addObjectsAsync(new JSONArray(array), null);
                Toast.makeText(getActivity().getApplicationContext(), "Document added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error = e.getMessage();

                Toast.makeText(getActivity().getApplicationContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

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
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            bookImage.setImageURI(imageUri);
            uploadImage();
        }
    }

    private void uploadImage() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
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
                        Toast.makeText(getActivity().getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
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

    