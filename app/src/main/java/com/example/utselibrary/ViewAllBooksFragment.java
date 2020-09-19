package com.example.utselibrary;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.utselibrary.Model.DocumentModel;
import com.example.utselibrary.Model.Documents;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ViewAllBooksFragment extends Fragment {

    RecyclerView viewAllBooksList;
    FirebaseFirestore fStore;
    DatabaseReference ref;
    FirebaseRecyclerAdapter<Documents, DocumentsViewHolder> adapter;
    FirebaseRecyclerOptions<Documents> options;
    EditText searchTf;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ViewAllBooksFragment() {
        // Required empty public constructor
    }

    public static ViewAllBooksFragment newInstance(String param1, String param2) {
        ViewAllBooksFragment fragment = new ViewAllBooksFragment();
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
        return inflater.inflate(R.layout.view_all_books_fragment, container, false);
    }

    /**********************************************************************************************
     * View All Books Fragment
     * manipulates the fragment where all books are displayed
     ************************************************************************************************/

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        searchTf = getActivity().findViewById(R.id.searchTf);
        // Get views
        viewAllBooksList = getView().findViewById(R.id.viewAllBooksList);
        fStore = FirebaseFirestore.getInstance();
        setRecyclerView();

    }

    private void setRecyclerView() {

        // Collection
        ref = FirebaseDatabase.getInstance().getReference().child("documents");

        options = new FirebaseRecyclerOptions.Builder<Documents>().setQuery(ref,Documents.class).build();

        adapter = new FirebaseRecyclerAdapter<Documents, DocumentsViewHolder>(options) {
            @Override

            @SuppressLint("SetTextI18n")
            protected void onBindViewHolder(@NonNull DocumentsViewHolder holder, int position, @NonNull Documents model) {
                holder.bookTitleText.setText(model.getTitle());
                holder.authorNameText.setText("By " + model.getPrimaryAuthor());
                Picasso.get().load(model.getImageUrl()).into(holder.coverImage);
            }



            @NonNull
            @Override
            public DocumentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_layout, parent, false);
                return new DocumentsViewHolder(view);
            }
        };
        viewAllBooksList.setHasFixedSize(true);
        viewAllBooksList.setLayoutManager(new LinearLayoutManager(getContext()));
        viewAllBooksList.setAdapter(adapter);
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

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}