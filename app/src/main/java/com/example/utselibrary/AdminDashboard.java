package com.example.utselibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import maes.tech.intentanim.CustomIntent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**********************************************************************************************
 * Parent Activity for User Dashboard
 * manipulates the page where user dashboard fragments are contained
 ************************************************************************************************/

public class AdminDashboard extends AppCompatActivity {

    Button addBookBtn, searchBtn, profileBtn,requestsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Get views
        addBookBtn = findViewById(R.id.addBookBtn);
        searchBtn = findViewById(R.id.searchBtn);
        profileBtn = findViewById(R.id.profileBtn);
        requestsBtn = findViewById(R.id.requestsBtn);

        // Fragment initialized
        Fragment LibraryFragment = new AddBookFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.flFragment, LibraryFragment);
        fragmentTransaction.commit();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();

        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookBtn.setBackgroundResource(android.R.drawable.ic_input_add);
                searchBtn.setBackgroundResource(R.drawable.search_button);
                profileBtn.setBackgroundResource(R.drawable.profile_button);
                loadFragment((new AddBookFragment()));
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookBtn.setBackgroundResource(R.drawable.library_button);
                searchBtn.setBackgroundResource(R.drawable.search_button_pressed);
                profileBtn.setBackgroundResource(R.drawable.profile_button);
                loadFragment((new ViewAllBooksFragment()));
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookBtn.setBackgroundResource(R.drawable.library_button);
                searchBtn.setBackgroundResource(R.drawable.search_button);
                profileBtn.setBackgroundResource(R.drawable.profile_button_pressed);
                loadFragment((new ProfilePageFragment()));
            }
        });
        requestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminViewRequests.class));
            }
        }
        );
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

/*public class AdminDashboard extends AppCompatActivity {
    Button addBookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        //addBookBtn = findViewById(R.id.addBookBtn);

       // addBookBtn.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View view) {
               // startActivity(new Intent(getApplicationContext(), AddBook.class));
            }
        //});
    //}

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        CustomIntent.customType(AdminDashboard.this, "right-to-left");
        finish();
    }
}*/