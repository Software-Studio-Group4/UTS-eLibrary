package com.example.utselibrary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePage extends AppCompatActivity {
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String userID = fAuth.getCurrentUser().getUid();
    DocumentReference documentReference = fStore.collection("Users").document(userID);
    TextView firstNameTf, lastNameTf, mobileNoTf, uniIdTf, totalFinesTf;
    Button changeDetailsBtn, changePassBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page2);
        firstNameTf = findViewById(R.id.firstNameTf);
        lastNameTf = findViewById(R.id.lastNameTf);
        mobileNoTf = findViewById(R.id.mobileNoTf);
        uniIdTf = findViewById(R.id.uniIdTf);
        totalFinesTf = findViewById(R.id.totalFinesTf);
        backBtn = findViewById(R.id.backBtn);
        changeDetailsBtn = findViewById(R.id.changeDetBtn);
        changePassBtn = findViewById(R.id.changePassBtn);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String fiName = documentSnapshot.getString("firstName");
                    String laName = documentSnapshot.getString("lastName");
                    String mo = documentSnapshot.getString("phoneNumber");
                    String unId = documentSnapshot.getString("uniID");

                    firstNameTf.setText(fiName); // Display first name
                    lastNameTf.setText(laName); // Display last name
                    mobileNoTf.setText(mo); // Display mobile number
                    uniIdTf.setText(unId); // Display uni ID
                } else {
                    Toast.makeText(ProfilePage.this, "Error: Details cannot be displayed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilePage.this, "Error: Details cannot be displayed", Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });
        changeDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangeDetails.class));
            }
        });

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    startActivity(new Intent(getApplicationContext(), ChangePassword.class));
            }
        });

    }
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
