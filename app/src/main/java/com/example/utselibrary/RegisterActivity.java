package com.example.utselibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import maes.tech.intentanim.CustomIntent;

/**********************************************************************************************
 * Parent Activity for Registration
 * manipulates the page where registration fragments are contained
 ************************************************************************************************/

public class RegisterActivity extends AppCompatActivity {

    Button nextBtn, backBtn, reverseBackBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        nextBtn = findViewById(R.id.nextBtn);
        backBtn = findViewById(R.id.backBtn);
        reverseBackBtn = findViewById(R.id.reverseBackBtn);
        reverseBackBtn.setVisibility(View.INVISIBLE);

        Fragment EmailPassFragment = new RegisterEmailPassFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.flFragment, EmailPassFragment);
        fragmentTransaction.commit();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                CustomIntent.customType(RegisterActivity.this, "fadein-to-fadeout");
            }
        });

        reverseBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment((new RegisterEmailPassFragment()));
                reverseBackBtn.setVisibility(View.INVISIBLE);
                backBtn.setVisibility(View.VISIBLE);
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

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction
                .setCustomAnimations(R.anim.right_to_left, R.anim.exit_right_to_left)
                .replace(R.id.flFragment, fragment);
        fragmentTransaction.commit();
    }

}
