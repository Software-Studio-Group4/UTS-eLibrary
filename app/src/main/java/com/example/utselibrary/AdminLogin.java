package com.example.utselibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLogin extends AppCompatActivity {
    EditText emailTf, passwordTf;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        backBtn = findViewById(R.id.backBtn);
        progressBar.setVisibility(View.INVISIBLE);
        emailTf = findViewById(R.id.emailTf);
        passwordTf = findViewById(R.id.passwordTf);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                final String userEmail = emailTf.getText().toString().trim();
                final String userPass = passwordTf.getText().toString().trim();
                if (TextUtils.isEmpty(userEmail)) {
                    emailTf.setError("Cannot have Empty Field");
                    return;
                }
                if (TextUtils.isEmpty(userPass)) {
                    passwordTf.setError("Cannot have Empty Field");
                    return;
                }

                fAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference staffRef = fStore.collection("Admin").document(userID);
                            staffRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                                        progressBar.setVisibility(View.INVISIBLE);
                                        loginBtn.setVisibility(View.VISIBLE);

                                    } else {
                                        Toast.makeText(AdminLogin.this, "Invalid account", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        loginBtn.setVisibility(View.VISIBLE);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminLogin.this, "Database Error", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    loginBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            Toast.makeText(AdminLogin.this, "Invalid Username or password", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            loginBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
    }
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}