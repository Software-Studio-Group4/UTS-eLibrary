package com.example.utselibrary;

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

public class UserDashboard extends AppCompatActivity {

    Button libraryBtn, searchBtn, profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dashboard);

        // Get views
        libraryBtn = findViewById(R.id.libraryBtn);
        searchBtn = findViewById(R.id.searchBtn);
        profileBtn = findViewById(R.id.profileBtn);

        // Fragment initialized
        Fragment LibraryFragment = new MyLibraryFragment();
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
