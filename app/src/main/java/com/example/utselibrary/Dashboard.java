package com.example.utselibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import maes.tech.intentanim.CustomIntent;

public class Dashboard extends AppCompatActivity {

    Button profileBtn, bookBtn, logoutBtn, searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        profileBtn = findViewById(R.id.profileBtn);
        bookBtn = findViewById(R.id.bookBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        searchBtn = findViewById(R.id.searchBook);

    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
            }
        });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BookDetails.class));
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                CustomIntent.customType(Dashboard.this, "right-to-left");
                finish();
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchBook.class));
            }
        });
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onRestart() {
        super.onRestart();
    }

    public void onDestroy() {
        super.onDestroy();
    }

}