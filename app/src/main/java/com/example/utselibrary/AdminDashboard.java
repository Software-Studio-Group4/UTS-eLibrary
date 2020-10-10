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

    Button addBookBtn, searchBtn, manageBtn, requestsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Get views
        addBookBtn = findViewById(R.id.addBookBtn);
        searchBtn = findViewById(R.id.searchBtn);
        manageBtn = findViewById(R.id.manageBtn);
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
                addBookBtn.setBackgroundResource(R.drawable.add_book_icon_blue);
                searchBtn.setBackgroundResource(R.drawable.search_button);
                manageBtn.setBackgroundResource(R.drawable.manage_icon_grey);
                loadFragment((new AddBookFragment()));
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookBtn.setBackgroundResource(R.drawable.add_book_icon_grey);
                searchBtn.setBackgroundResource(R.drawable.search_button_pressed);
                manageBtn.setBackgroundResource(R.drawable.manage_icon_grey);
                loadFragment((new ViewAllBooksFragment()));
            }
        });

        manageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookBtn.setBackgroundResource(R.drawable.add_book_icon_grey);
                searchBtn.setBackgroundResource(R.drawable.search_button);

                manageBtn.setBackgroundResource(R.drawable.manage_icon_blue);
                loadFragment((new ViewUsersFragment()));
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