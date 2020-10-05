package com.example.utselibrary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class AdminBookDetailsFragment extends Fragment {

    Button backBtn, updateBtn, removeBtn;
    TextView titleTf;
    ImageView bookCover;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference cRef = fStore.collection("Documents");

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
     * Book details fragment
     * manipulates the fragment where book details are displayed
     ************************************************************************************************/

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // Get views
        backBtn = getView().findViewById(R.id.backBtn);
        updateBtn = getView().findViewById(R.id.updateBtn);
        removeBtn = getView().findViewById(R.id.removeBtn);
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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Page does not exist yet", Toast.LENGTH_SHORT).show();
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Page does not exist yet", Toast.LENGTH_SHORT).show();
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