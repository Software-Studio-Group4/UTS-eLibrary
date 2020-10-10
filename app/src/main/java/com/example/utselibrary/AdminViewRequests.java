package com.example.utselibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utselibrary.Model.BookRequest;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class AdminViewRequests extends AppCompatActivity {
    private Button rejectBtn, reviewBtn, acceptBtn;
    private RecyclerView resultList;
    LinearLayoutManager linearLayoutManager;

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference bookRef = fStore.collection("BookRequests");
    private FirestoreRecyclerAdapter<BookRequest, RequestBookViewHolder> bookRequestAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_requests);
        resultList = findViewById(R.id.requestRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        resultList.setLayoutManager(linearLayoutManager);
        setRecyclerView();


    }

    private void setRecyclerView() {
        Query query = bookRef.orderBy("title", Query.Direction.DESCENDING);


        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Toast.makeText(AdminViewRequests.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AdminViewRequests.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        FirestoreRecyclerOptions<BookRequest> options = new FirestoreRecyclerOptions.Builder<BookRequest>().setQuery(query, BookRequest.class).build();

        bookRequestAdapter = new FirestoreRecyclerAdapter<BookRequest, RequestBookViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestBookViewHolder holder, int position, @NonNull BookRequest model) {
                RequestBookViewHolder.setRequestName(BookRequest.getTitle(), BookRequest.getPrimaryAuthor(), BookRequest.getGenre(), BookRequest.getPublisher());
            }

            @NonNull
            @Override
            public RequestBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list_layout, parent, false);
                return new RequestBookViewHolder(view);
            }
        };
        resultList.setAdapter(bookRequestAdapter);
    }

    public static class RequestBookViewHolder extends RecyclerView.ViewHolder {
        private static View view;

        RequestBookViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
        static void setRequestName(String title, String author, String genre, String publisher) {
            TextView titleName = view.findViewById(R.id.bookTitle);
            titleName.setText(title);
            TextView authorName = view.findViewById(R.id.authorName);
            authorName.setText(author);
            TextView genreName = view.findViewById(R.id.genre);
            genreName.setText(genre);
            TextView pubName = view.findViewById(R.id.publisher);
            pubName.setText(publisher);
        }

    }
}