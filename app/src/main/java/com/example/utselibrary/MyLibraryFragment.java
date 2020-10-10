package com.example.utselibrary;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.utselibrary.Model.Documents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class MyLibraryFragment extends Fragment {
    RecyclerView borrowedBooks;
    FirestoreRecyclerAdapter adapter;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference documentRef = fStore.collection("Documents");
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String userID = fAuth.getCurrentUser().getUid();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MyLibraryFragment() { }

    public static MyLibraryFragment newInstance(String param1, String param2) {
        MyLibraryFragment fragment = new MyLibraryFragment();
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
        return inflater.inflate(R.layout.library_fragment, container, false);
    }

    /**********************************************************************************************
     * User library fragment
     * manipulates the fragment where the users borrowed books are displayed
     ************************************************************************************************/

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        borrowedBooks = getView().findViewById(R.id.borrowedBooks);

        //setRecyclerView();
        //documentRef.whereEqualTo("borrowers", userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          //  @Override
          //  public void onComplete(@NonNull Task<QuerySnapshot> task) {

          //  }
       // });
        Query query = documentRef.orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Documents> options = new FirestoreRecyclerOptions.Builder<Documents>()
                .setQuery(query, Documents.class).build();

        adapter = new FirestoreRecyclerAdapter<Documents, DocumentsViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull final DocumentsViewHolder holder, int position, @NonNull Documents model) {
                holder.bookTitleText.setText(model.getTitle());
                holder.authorNameText.setText("By " + model.getAuthor());
                Picasso.get().load(model.getCoverImageUrl()).into(holder.coverImage);
            }

            @NonNull
            @Override
            public DocumentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_layout, parent, false);
                return new DocumentsViewHolder(view);
            }
        };
        borrowedBooks.setHasFixedSize(true);
        borrowedBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        borrowedBooks.setAdapter(adapter);
    }

    private void setRecyclerView() {
        Query query = documentRef.orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Documents> options = new FirestoreRecyclerOptions.Builder<Documents>()
                .setQuery(query, Documents.class).build();

        adapter = new FirestoreRecyclerAdapter<Documents, DocumentsViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull final DocumentsViewHolder holder, int position, @NonNull Documents model) {
                holder.bookTitleText.setText(model.getTitle());
                holder.authorNameText.setText("By " + model.getAuthor());
                Picasso.get().load(model.getCoverImageUrl()).into(holder.coverImage);
            }

            @NonNull
            @Override
            public DocumentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_layout, parent, false);
                return new DocumentsViewHolder(view);
            }
        };

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