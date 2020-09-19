package com.example.utselibrary;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;

import com.example.utselibrary.Model.Documents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchBook extends AppCompatActivity {
    private EditText searchTf;
    private Button backBtn;
    private RecyclerView resultList;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference documentRef = database.collection("Documents");
    private FirestoreRecyclerAdapter<Documents, SearchBookViewHolder> searchAdapter;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        searchTf = findViewById(R.id.searchTf);
        resultList = findViewById(R.id.resultRecyclerView);
        backBtn = findViewById(R.id.backBtn);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,true);
        resultList.setLayoutManager(linearLayoutManager);
        setRecyclerView();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });


}
    private void setRecyclerView(){
        Query query = documentRef.orderBy("title", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Documents> options = new FirestoreRecyclerOptions.Builder<Documents>().setQuery(query, Documents.class).build();

        searchAdapter = new FirestoreRecyclerAdapter<Documents, SearchBookViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchBookViewHolder holder, int position, @NonNull Documents model) {
            }


            @NonNull
            @Override
            public SearchBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
                return new SearchBookViewHolder(view);
            }
        };
        resultList.setAdapter(searchAdapter);
    }

    public static class SearchBookViewHolder extends RecyclerView.ViewHolder {
        private View view;

        SearchBookViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
        @SuppressLint("SetTextI18n")
        void setTitle(String title) {
            TextView textView = view.findViewById(R.id.bookTextView);
            textView.setText("Title: " + title);


    }

}
}