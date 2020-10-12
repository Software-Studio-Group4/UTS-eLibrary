package com.example.utselibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.example.utselibrary.Model.Documents;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class AdminBookDetailsFragment extends Fragment {

    private static final String TAG = "Admin Book";
    Button backBtn, updateBtn, removeBtn;
    TextView titleTf, authorText, genreText, publishedYearText, descriptionText;
    ImageView bookCover;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference cRef = fStore.collection("Documents");
    Client client = new Client("9L80XXFOLT", "a01b448ff9270562e195ef32110d829a");
    Index index = client.getIndex("Documents");

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AdminBookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(String param1, String param2) {
        BookDetailsFragment fragment = new BookDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_admin_book_details, container, false);
    }

    /**********************************************************************************************
     * Admin Book details fragment
     * manipulates the fragment where admin accesses book details
     ************************************************************************************************/

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // Get views
        backBtn = getView().findViewById(R.id.backBtn);
        updateBtn = getView().findViewById(R.id.updateBtn);
        removeBtn = getView().findViewById(R.id.removeBtn);
        titleTf = getView().findViewById(R.id.titleText);
        bookCover = getView().findViewById(R.id.bookCover);
        authorText = getView().findViewById(R.id.authorText);
        genreText = getView().findViewById(R.id.genreText);
        publishedYearText = getView().findViewById(R.id.publishedYearText);
        descriptionText = getView().findViewById(R.id.descriptionText);

        final FragmentManager fm = getFragmentManager();
        final Fragment ViewAllBooksFragment = new ViewAllBooksFragment();

        // Get bookID
        Bundle bookID = this.getArguments();
        final String id = bookID.getString("id");



        final DocumentReference documentReference = cRef.document(id);
        //final DocumentReference objectID = fStore.collection("Documents").document("objectID");
        final String objectId;

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Documents document = documentSnapshot.toObject(Documents.class);

                    // Get fields from Firestore
                    String title = document.getTitle();
                    String bookCoverUrl = document.getCoverImageUrl();
                    String objectID = documentSnapshot.getString("objectID");
                    String author = document.getAuthor();
                    String genre = document.getGenre();
                    String publishedYear = document.getPublishedYear();
                    String description = document.getDescription();

                    // Set text views
                    titleTf.setText(title);
                    authorText.setText("Author: " + author);
                    genreText.setText("Genre: " + genre);
                    publishedYearText.setText("Published: " + publishedYear);
                    descriptionText.setText(description);
                    Picasso.get().load(bookCoverUrl).into(bookCover);
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bId = new Bundle();
                bId.putString("item1",id);
                Intent i=new Intent(getContext(), UpdateBook.class);
                i.putExtras(bId);
                startActivity(i);

                Toast.makeText(getContext(), "Page does not exist yet", Toast.LENGTH_SHORT).show();
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            final String objectID = documentSnapshot.getString("objectID");
                            documentReference.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            index.deleteObjectAsync(objectID, null);
                                            fm.popBackStack();
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });
                        }
                    }
                });

                Toast.makeText(getContext(), "Book Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.popBackStack();

            }
        });

    }
}

/*
AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
                builder.setMessage("Are you sure you want to remove this book?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        documentReference.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Query query = new Query()
                                                .setFilters(id)
                                                .setAroundLatLng(new AbstractQuery.LatLng(40.71, -74.01));
                                        index.deleteByAsync(query, null);
                                        fm.popBackStack();
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                        Toast.makeText(getContext(), "Page does not exist yet", Toast.LENGTH_SHORT).show();
                    }
                })
                        .setNegativeButton("Cancel", null);

                AlertDialog alert = builder.create();
                alert.show();
 */