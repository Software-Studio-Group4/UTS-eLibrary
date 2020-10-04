package com.example.utselibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangeDetails extends AppCompatActivity {
    Button backBtn, saveBtn;
    EditText firstNameTf, lastNameTf, mobileNumTf;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_details);
        firstNameTf = findViewById(R.id.firstNameTf);
        lastNameTf = findViewById(R.id.lastNameTf);
        mobileNumTf = findViewById(R.id.phoneTf);
        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.backBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        final DocumentReference documentReference = fStore.collection("Users").document(userID);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameTf.getText().toString().trim();
                String lastName = lastNameTf.getText().toString().trim();
                String mobileNo = mobileNumTf.getText().toString().trim();
                 if (!TextUtils.isEmpty(firstName)) {
                     documentReference.update("firstName", firstName).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void aVoid) {
                             Toast.makeText(ChangeDetails.this, "Details Successfully Updated", Toast.LENGTH_LONG).show();
                         }
                     })
                             .addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull Exception e) {
                                     Toast.makeText(ChangeDetails.this,"Error", Toast.LENGTH_LONG).show();
                                 }
                             });
                }
                 if (!TextUtils.isEmpty(lastName)) {
                    documentReference.update("lastName", lastName).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ChangeDetails.this, "Details Successfully Updated", Toast.LENGTH_LONG).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChangeDetails.this,"Error", Toast.LENGTH_LONG).show();
                                }
                            });
                }
                 if (!TextUtils.isEmpty(mobileNo)) {
                    documentReference.update("phoneNumber", mobileNo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ChangeDetails.this, "Details Successfully Updated", Toast.LENGTH_LONG).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChangeDetails.this,"Error", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }

        });

    }
}
