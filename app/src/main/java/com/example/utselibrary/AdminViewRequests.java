package com.example.utselibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
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


import com.example.utselibrary.Model.BookRequestModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class AdminViewRequests extends AppCompatActivity {
    Button rejectBtn, acceptBtn, backBtn;
    RecyclerView resultList;
    LinearLayoutManager linearLayoutManager;

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference bookRef = fStore.collection("BookRequests");
    FirestoreRecyclerAdapter<BookRequestModel, RequestBookViewHolder> bookRequestAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_requests);
        resultList = findViewById(R.id.requestRecyclerView);
        resultList.setLayoutManager(new LinearLayoutManager(this));
        backBtn = findViewById(R.id.backBtn);
        Query query = bookRef.orderBy("title", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<BookRequestModel> options = new FirestoreRecyclerOptions.Builder<BookRequestModel>().setQuery(query, BookRequestModel.class).build();

        bookRequestAdapter = new FirestoreRecyclerAdapter<BookRequestModel, RequestBookViewHolder>(options){
          @Override
          protected void onBindViewHolder(@NonNull RequestBookViewHolder requestBookViewHolder, int position, @NonNull BookRequestModel bookRequest){
              requestBookViewHolder.setDetails(bookRequest.getTitle(), bookRequest.getAuthor());
              Log.d("LOG", "onBindViewHolder: found book" + bookRequest.getTitle());
          }

          @NonNull
            @Override
            public RequestBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
              View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
              return new RequestBookViewHolder(view);
          }
        };
        resultList.setAdapter(bookRequestAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
            }
        });
    }


    @Override
    protected void onStart(){
        super.onStart();
        bookRequestAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (bookRequestAdapter != null) {
            bookRequestAdapter.stopListening();
        }
    }
/**********************************************************************************************
 * Private Class for the recycler view
 ************************************************************************************************/
    private class RequestBookViewHolder extends RecyclerView.ViewHolder{
        private View view;

        RequestBookViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }

        //set details
    void setDetails(String title, String authorName){
        TextView titleTv = view.findViewById(R.id.bookTitle);
        TextView authorNameTv = view.findViewById(R.id.authorName);
        titleTv.setText(title);
        authorNameTv.setText(authorName);
    }

    }
    /*private void setRecyclerView() {
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
        FirestoreRecyclerOptions<BookRequestModel> options = new FirestoreRecyclerOptions.Builder<BookRequestModel>().setQuery(query, BookRequestModel.class).build();

        bookRequestAdapter = new FirestoreRecyclerAdapter<BookRequestModel, RequestBookViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestBookViewHolder holder, int position, @NonNull BookRequestModel model) {
                RequestBookViewHolder.setRequestName(BookRequestModel.getTitle(), BookRequestModel.getauthor(), BookRequestModel.getGenre(), BookRequestModel.getPublisher());
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

    }*/
}