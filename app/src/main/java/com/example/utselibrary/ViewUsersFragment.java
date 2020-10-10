package com.example.utselibrary;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.ListView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

import maes.tech.intentanim.CustomIntent;


public class ViewUsersFragment extends Fragment {

    private static final String TAG = "ViewUser";
    ListView userList;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    EditText searchTf;
    CollectionReference documentRef = fStore.collection("Users");
    String pos;

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String userID = fAuth.getCurrentUser().getUid();;
    DocumentReference adminRef = fStore.collection("Admin").document(userID);
    Button logoutBtn;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ViewUsersFragment() {
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
        return inflater.inflate(R.layout.fragment_view_users, container, false);
    }

    /**********************************************************************************************
     * View All Books Fragment
     * manipulates the fragment where all books are displayed
     ************************************************************************************************/

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get views
        final List <String> userIdList = new ArrayList<>();
        //logoutBtn = getView().findViewById(R.id.logoutBtn);

        Client client = new Client("9L80XXFOLT", "a01b448ff9270562e195ef32110d829a");
        final Index index = client.getIndex("Users");

        searchTf = getView().findViewById(R.id.searchTf);
        userList = getView().findViewById(R.id.userList);

        final Fragment UserDetailsFragment = new UserDetailsFragment();
        final FragmentManager fm = getFragmentManager();


        /*logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        documentRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> firstList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                firstList.add(document.getString("fullName"));
                               userIdList.add(document.getId());
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, firstList);
                            userList.setAdapter(arrayAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pos = userIdList.get(i);
                final Bundle userID = new Bundle();
               userID.putString("id", pos);

                final String[] algoliaID = new String[1];
                            Toast.makeText(getActivity().getApplicationContext(),   "Admin clicked: " + pos, Toast.LENGTH_SHORT).show();
                            UserDetailsFragment.setArguments(userID);
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                           fragmentTransaction
                                   .add(R.id.flFragment, UserDetailsFragment).addToBackStack(null);
                          fragmentTransaction.commit();


              /* pos = bookIdList.get(i);
                Toast.makeText(getActivity().getApplicationContext(),   "clicked: " + pos, Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getActivity().getApplicationContext(), BookDetails.class);
                intent1.putExtra("pos", pos);
                startActivity(intent1);
                */

            }
        });
        searchTf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                com.algolia.search.saas.Query q = new com.algolia.search.saas.Query(s.toString())
                        .setAttributesToRetrieve("fullName", "id")
                        .setHitsPerPage(50);
                index.searchAsync(q, new CompletionHandler() {
                    @Override
                    public void requestCompleted(JSONObject content, AlgoliaException error) {
                        try {
                            JSONArray hits = content.getJSONArray("hits");
                            List<String> list = new ArrayList<>();
                            userIdList.clear();
                            for(int i = 0; i < hits.length(); i++){
                                JSONObject jsonObject = hits.getJSONObject(i);
                                String title = jsonObject.getString("fullName");
                                String id = jsonObject.getString("id");
                                list.add(title);
                                userIdList.add(id);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, list);
                            userList.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });



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