package com.example.utselibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.utselibrary.Model.Documents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminViewRequests extends AppCompatActivity {
    private Button rejectBtn, reviewBtn, acceptBtn;
    private RecyclerView bookReqRV;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference documentRef = database.collection("Book Requests");
    private FirestoreRecyclerAdapter<Documents, SearchBook.SearchBookViewHolder> searchAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_requests);
    }
}