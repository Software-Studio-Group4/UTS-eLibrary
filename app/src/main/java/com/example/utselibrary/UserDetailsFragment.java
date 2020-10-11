package com.example.utselibrary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.search.saas.AbstractQuery;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.example.utselibrary.Model.Documents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class UserDetailsFragment extends Fragment {

    private static final String TAG = "Admin Book";
    Button backBtn, fineBtn, suspendBtn;
    TextView fullNameTf, borrowAmountTf;
    ListView borrowedDocsList;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference cRef = fStore.collection("Users");
    Client client = new Client("9L80XXFOLT", "a01b448ff9270562e195ef32110d829a");
    Index index = client.getIndex("Documents");
    CollectionReference documentRef = fStore.collection("Documents");
    DocumentReference borrowedDocumentRef = fStore.collection("Users").document("borrowedDocs");
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public UserDetailsFragment() {
        // Required empty public constructor
    }

    public static UserDetailsFragment newInstance(String param1, String param2) {
      UserDetailsFragment fragment = new UserDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    /**********************************************************************************************
     * Book details fragment
     * manipulates the fragment where book details are displayed
     ************************************************************************************************/

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // Get views
        backBtn = getView().findViewById(R.id.backBtn);
       fineBtn = getView().findViewById(R.id.fineBtn);
        suspendBtn = getView().findViewById(R.id.suspendBtn);
        fullNameTf = getView().findViewById(R.id.fullNameTf);
        borrowAmountTf = getView().findViewById(R.id.borrowAmountTf);
        borrowedDocsList = getView().findViewById(R.id.borrowedDocslist);

        final FragmentManager fm = getFragmentManager();
        final Fragment ViewAllBooksFragment = new ViewAllBooksFragment();

        // Get bookID
        Bundle userID = this.getArguments();
        final String id = userID.getString("id");

        final DocumentReference documentReference = cRef.document(id);
        //final DocumentReference objectID = fStore.collection("Documents").document("objectID");
        final String objectId;

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String fullName = documentSnapshot.getString("fullName");
                    String objectID = documentSnapshot.getString("objectID");
                    long borrowAmount = documentSnapshot.getLong("borrowAmount");
                    fullNameTf.setText(fullName);
                    borrowAmountTf.setText("Books Currently Borrowed: " + borrowAmount);
                }
            }
        });
        // Collection

        fineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Page does not exist yet", Toast.LENGTH_SHORT).show();
            }
        });

        suspendBtn.setOnClickListener(new View.OnClickListener() {
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
    private class DocumentsViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitleText, authorNameText;
        ImageView coverImage;

        public DocumentsViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitleText = itemView.findViewById(R.id.bookTitleText);
            authorNameText = itemView.findViewById(R.id.authorNameText);
            coverImage = itemView.findViewById(R.id.coverImage);
        }
    }
}