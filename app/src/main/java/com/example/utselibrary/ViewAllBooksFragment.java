package com.example.utselibrary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.utselibrary.Model.Documents;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.model.Document;

import javax.annotation.Nullable;

public class ViewAllBooksFragment extends Fragment {

    RecyclerView viewAllBooksList;
    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter adapter;

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
        // Get views
        viewAllBooksList = getView().findViewById(R.id.viewAllBooksList);
        fStore = FirebaseFirestore.getInstance();

        // Query
        Query documentQuery = fStore.collection("Documents");

        //
        FirestoreRecyclerOptions<Documents> options = new FirestoreRecyclerOptions.Builder<Documents>()
                .setQuery(documentQuery, Documents.class).build();

         adapter = new FirestoreRecyclerAdapter<Documents, DocumentsViewHolder>(options) {
            @NonNull
            @Override
            public DocumentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_layout, parent, false);
                return new DocumentsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DocumentsViewHolder holder, int position, @NonNull Documents model) {
                holder.bookTitleText.setText(model.getTitle());
                holder.authorNameText.setText((CharSequence) model.getAuthors());
            }
        };

         viewAllBooksList.setHasFixedSize(true);
         viewAllBooksList.setLayoutManager(new LinearLayoutManager(getContext()));
         viewAllBooksList.setAdapter(adapter);
    }

    private class DocumentsViewHolder extends RecyclerView.ViewHolder{

        TextView bookTitleText, authorNameText;

        public DocumentsViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitleText = itemView.findViewById(R.id.bookTitleText);
            authorNameText = itemView.findViewById(R.id.authorNameText);
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