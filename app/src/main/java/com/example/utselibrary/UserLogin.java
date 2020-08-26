package com.example.utselibrary;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserLogin extends AppCompatActivity {
    private EditText emailTf, passwordTf;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        emailTf = findViewById(R.id.emailTf);
        passwordTf = findViewById(R.id.passwordTf);
        final Button userLoginBtn = findViewById(R.id.userLoginBtn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        userLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLoginBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                String email = emailTf.getText().toString().trim();
                String password = passwordTf.getText().toString().trim();

                if (TextUtils.isEmpty(password)) {
                    passwordTf.setError("Cannot have Empty Field");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    emailTf.setError("Cannot have Empty Field");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference patientRef = fStore.collection("Users").document(userID);
                            patientRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                        progressBar.setVisibility(View.INVISIBLE);
                                        userLoginBtn.setVisibility(View.VISIBLE);
                                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                    } else {
                                        Toast.makeText(UserLogin.this, "Invalid account", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        userLoginBtn.setVisibility(View.VISIBLE);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserLogin.this, "Database Error", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    userLoginBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            Toast.makeText(UserLogin.this, "Invalid Username or password", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            userLoginBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    public void backBtnPressed(View view) {
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void finish() {
        super.finish();
    }
}