package com.example.utselibrary;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utselibrary.Model.BorrowedDocument;
import com.example.utselibrary.Model.Documents;
import com.example.utselibrary.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

public class BookDetailsFragment extends Fragment {

    Button backBtn, borrowBtn, returnBtn;
    TextView titleTf;
    ImageView bookCover;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference cRef = fStore.collection("Documents");

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public BookDetailsFragment() {
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
        return inflater.inflate(R.layout.book_details_fragment, container, false);
    }

    /**********************************************************************************************
     * Book details fragment
     * manipulates the fragment where book details are displayed
     ************************************************************************************************/

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // Get views
        backBtn = getView().findViewById(R.id.backBtn);
        borrowBtn = getView().findViewById(R.id.borrowBtn);
        returnBtn = getView().findViewById(R.id.returnBtn);
        titleTf = getView().findViewById(R.id.titleTf);
        bookCover = getView().findViewById(R.id.bookCover);

        final FragmentManager fm = getFragmentManager();
        final Fragment ViewAllBooksFragment = new ViewAllBooksFragment();

        // Get bookID
        Bundle bookID = this.getArguments();
        final String id = bookID.getString("id");

        final DocumentReference documentReference = cRef.document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String title = documentSnapshot.getString("title");
                    String bookCoverUrl = documentSnapshot.getString("coverImageUrl");

                    titleTf.setText(title);
                    Picasso.get().load(bookCoverUrl).into(bookCover);
                }
            }
        });

        borrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if the user has reached their max borrow allowance
                //check if the user already borrowed this book
                final DocumentReference userReference = fStore.collection("Users").document(FirebaseAuth.getInstance().getUid());


                userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final User user= documentSnapshot.toObject(User.class);
                        if(user.getBorrowedDocs().size() >= user.getMaxAllowed()){
                            Toast.makeText(getContext(), "You can't borrow any more books", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    //check to see if borrow limit has been reached first
                                    //if not, add the document to the user's borrowList and add the user's ID to the borrowers list in this document
                                    Documents document = documentSnapshot.toObject(Documents.class);
                                    if(document.getBorrowers().size() >= document.getBorrowLimit()){
                                        Toast.makeText(getContext(), "This book has reached its borrow limit", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    else if(document.getBorrowers().contains(FirebaseAuth.getInstance().getUid())){
                                        Toast.makeText(getContext(), "You already borrowed this book", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    else{
                                        List<String> borrowers = document.getBorrowers();
                                        if(borrowers.size() > 0 && borrowers.get(0) == ""){
                                            borrowers.clear();
                                        }
                                        borrowers.add(FirebaseAuth.getInstance().getUid());
                                        List<BorrowedDocument> borrowedDocs = new ArrayList<BorrowedDocument>();
                                        List<BorrowedDocument> borrowHistory = new ArrayList<BorrowedDocument>();
                                        borrowedDocs.addAll(user.getBorrowedDocs());
                                        borrowHistory.addAll(user.getBorrowHistory());

                                        BorrowedDocument newBorrow = new BorrowedDocument(id);

                                        borrowedDocs.add(newBorrow);
                                        borrowHistory.add(newBorrow);

                                        documentReference.update("borrowers", borrowers);
                                        userReference.update("borrowedDocs", borrowedDocs);
                                        userReference.update("borrowHistory", borrowHistory);

                                        Toast.makeText(getContext(), "Successfully borrowed" + titleTf.getText() , Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        });
                    }
                });




            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DocumentReference userReference = fStore.collection("Users").document(FirebaseAuth.getInstance().getUid());
                userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                            final User user= documentSnapshot.toObject(User.class);
                            if(user.hasBook(id)){

                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            Documents document = documentSnapshot.toObject(Documents.class);
                                                document.removeBorrower(FirebaseAuth.getInstance().getUid());
                                                user.returnBook(id);

                                                userReference.update("borrowedDocs", user.getBorrowedDocs());
                                                documentReference.update("borrowers", document.getBorrowers());
                                                Toast.makeText(getContext(), "You returned this book", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(getContext(), "You haven't borrowed this book", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
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