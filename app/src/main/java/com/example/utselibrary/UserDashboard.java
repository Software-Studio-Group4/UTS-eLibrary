package com.example.utselibrary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**********************************************************************************************
 * Parent Activity for User Dashboard
 * manipulates the page where user dashboard fragments are contained
 ************************************************************************************************/

public class UserDashboard extends AppCompatActivity {

    Button libraryBtn, searchBtn, profileBtn, bookRequestBtn;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String userID = fAuth.getCurrentUser().getUid();
    DocumentReference userDocRef = fStore.collection("Users").document(userID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dashboard);

        // Get views
        libraryBtn = findViewById(R.id.libraryBtn);
        searchBtn = findViewById(R.id.searchBtn);
        profileBtn = findViewById(R.id.profileBtn);
        bookRequestBtn = findViewById(R.id.bookRequestBtn);

        // Fragment initialized
        Fragment LibraryFragment = new MyLibraryFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.flFragment, LibraryFragment);
        fragmentTransaction.commit();

        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Boolean isLecturer = documentSnapshot.getBoolean("isLecturer");
                    if (isLecturer == true){
                        bookRequestBtn.setVisibility(View.VISIBLE);
                        bookRequestBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(getApplicationContext(), BookRequest.class));
                            }
                        });
                    }

                } else {
                    Toast.makeText(UserDashboard.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserDashboard.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
/*
        bookRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BookRequest.class));
            }
        });

        */
        libraryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libraryBtn.setBackgroundResource(R.drawable.library_button_pressed);
                searchBtn.setBackgroundResource(R.drawable.search_button);
                profileBtn.setBackgroundResource(R.drawable.profile_button);
                loadFragment((new MyLibraryFragment()));
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libraryBtn.setBackgroundResource(R.drawable.library_button);
                searchBtn.setBackgroundResource(R.drawable.search_button_pressed);
                profileBtn.setBackgroundResource(R.drawable.profile_button);
                loadFragment((new ViewAllBooksFragment()));
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libraryBtn.setBackgroundResource(R.drawable.library_button);
                searchBtn.setBackgroundResource(R.drawable.search_button);
                profileBtn.setBackgroundResource(R.drawable.profile_button_pressed);
                loadFragment((new ProfilePageFragment()));
            }
        });
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    // Load fragment method
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction
                .replace(R.id.flFragment, fragment);
        fragmentTransaction.commit();
    }
}
