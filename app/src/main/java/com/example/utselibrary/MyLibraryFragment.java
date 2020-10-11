package com.example.utselibrary;

import android.app.Activity;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

import com.example.utselibrary.Model.Documents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Collection;

import javax.annotation.Nullable;

public class MyLibraryFragment extends Fragment {
    RecyclerView borrowedBooks;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference documentRef = fStore.collection("Documents");
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String userID = fAuth.getCurrentUser().getUid();
    private DocumentAdapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    DocumentReference userRef = fStore.collection("Users").document(userID);
    CollectionReference documentsRef = fStore.collection("Documents");
    private RecyclerView myLibraryRecyclerView;
    private FirestoreRecyclerAdapter<Documents, DocumentsViewHolder> documentsAdapter;



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
        //borrowedBooks = getView().findViewById(R.id.borrowedBooks);

        myLibraryRecyclerView = view.findViewById(R.id.myLibraryRv);
        myLibraryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query booksQuery = documentsRef.whereArrayContains("borrowers", userID);
        FirestoreRecyclerOptions<Documents> options = new FirestoreRecyclerOptions.Builder<Documents>().setQuery(booksQuery, Documents.class).build();

        documentsAdapter = new FirestoreRecyclerAdapter<Documents, DocumentsViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull DocumentsViewHolder documentsViewHolder, int position, @NonNull Documents documentModel){
                Log.d("TAG : ", "onBindViewHolder: found " + documentModel.getTitle());
                documentsViewHolder.setBookDetails(documentModel.getTitle(), documentModel.getTitle(), getSnapshots().getSnapshot(position).getId());
            }
            @NonNull
            @Override
            public DocumentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
                return new DocumentsViewHolder(view);
            }
        };
        myLibraryRecyclerView.setAdapter(documentsAdapter);

        //setRecyclerView();

    }

//    private void setRecyclerView() {
//        Query query = documentRef.orderBy("title", Query.Direction.ASCENDING);
//
//        FirestoreRecyclerOptions<Documents> options = new FirestoreRecyclerOptions.Builder<Documents>()
//                .setQuery(query, Documents.class).build();
//
//        adapter = new DocumentAdapter(options);
//        borrowedBooks.setHasFixedSize(true);
//        borrowedBooks.setLayoutManager(new LinearLayoutManager(getContext()));
//        borrowedBooks.setAdapter(adapter);
//    }

    @Override
    public void onStart() {
        super.onStart();
        documentsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        documentsAdapter.stopListening();
    }

    /**********************************************************************************************
     * Private Class for the recycler
     ************************************************************************************************/
    private class DocumentsViewHolder extends RecyclerView.ViewHolder{
        private View view;

       DocumentsViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }

        void setBookDetails(String title, String authorName, final String bookID){
           //set the text
           TextView titleTv = view.findViewById(R.id.bookTitle);
           TextView authorNameTv = view.findViewById(R.id.authorName);
           titleTv.setText(title);
           authorNameTv.setText(authorName);

           //set the onclick
            ConstraintLayout bookItem = view.findViewById(R.id.bookItem);
            final Bundle bundle = new Bundle();
            bundle.putString("id", bookID);

            bookItem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    final Fragment BookDetailsFragment = new BookDetailsFragment();
                    final FragmentManager fm = getFragmentManager();
                    BookDetailsFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction
                            .setCustomAnimations(R.anim.right_to_left, R.anim.exit_right_to_left, R.anim.left_to_right, R.anim.exit_left_to_right)
                            .add(R.id.flFragment, BookDetailsFragment).addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }

}