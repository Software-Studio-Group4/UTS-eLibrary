package com.example.utselibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.example.utselibrary.Model.DocumentModel;
import com.example.utselibrary.Model.Documents;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

public class ViewAllBooksFragment extends Fragment {

    RecyclerView viewAllBooksList;
    ListView bookList;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter adapter;
    EditText searchTf;
    private static String TAG = "fbSearch";
    CollectionReference documentRef = fStore.collection("Documents");

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
        final List <String> bookIdList = new ArrayList<>();

        searchTf = getView().findViewById(R.id.searchTf);
        bookList = getView().findViewById(R.id.bookList);

        documentRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.getString("title"));
                                bookIdList.add(document.getId());
                            }
                            ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, list);
                            bookList.setAdapter(arrayAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String pos = bookIdList.get(i);
                Toast.makeText(getActivity().getApplicationContext(),   "clicked: " + pos, Toast.LENGTH_SHORT).show();
            }
        });
       // viewAllBooksList = getView().findViewById(R.id.viewAllBooksList);
/*


        viewAllBooksList = getView().findViewById(R.id.viewAllBooksList);

        searchTf = getView().findViewById(R.id.searchTf);

        // Collection
        Query query = documentRef.orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<DocumentModel> options = new FirestoreRecyclerOptions.Builder<DocumentModel>()
                .setQuery(query, DocumentModel.class).build();

        adapter = new FirestoreRecyclerAdapter<DocumentModel, DocumentsViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull final DocumentsViewHolder holder, int position, @NonNull DocumentModel model) {
                holder.bookTitleText.setText(model.getTitle());
                holder.authorNameText.setText("By " + model.getPrimaryAuthor());
                Picasso.get().load(model.getCoverImageUrl()).into(holder.coverImage);
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
*/
        searchTf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               /*Log.d(TAG, "Search has changed to: " + s.toString());
                Query query;
                if(s.toString().isEmpty()){
                    query = documentRef.orderBy("title", Query.Direction.ASCENDING);
                }
                else{
                query = documentRef.whereArrayContains("title", s.toString());
                }
                FirestoreRecyclerOptions<DocumentModel> options = new FirestoreRecyclerOptions.Builder<DocumentModel>()
                        .setQuery(query, DocumentModel.class).build();
                adapter.updateOptions(options);
                */
                Client client = new Client("9L80XXFOLT", "a01b448ff9270562e195ef32110d829a");
                Index index = client.getIndex("Documents");
                com.algolia.search.saas.Query q = new com.algolia.search.saas.Query(s.toString())
                        .setAttributesToRetrieve("title", "id")
                        .setHitsPerPage(50);
                index.searchAsync(q, new CompletionHandler() {
                    @Override
                    public void requestCompleted(JSONObject content, AlgoliaException error) {
                        try {
                            JSONArray hits = content.getJSONArray("hits");
                            List<String> list = new ArrayList<>();
                           bookIdList.clear();
                            for(int i = 0; i < hits.length(); i++){
                                JSONObject jsonObject = hits.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                String id = jsonObject.getString("id");
                                list.add(title);
                               bookIdList.add(id);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, list);
                            bookList.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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

    @Override
    public void onStart() {
        super.onStart();
        //adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
       // adapter.stopListening();
    }
}