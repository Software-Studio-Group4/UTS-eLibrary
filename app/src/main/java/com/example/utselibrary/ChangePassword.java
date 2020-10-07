package com.example.utselibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {
    Button backBtn, saveBtn;
    EditText oldPassTf, newPassTf, confirmTf;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldPassTf = findViewById(R.id.oldPassTf);
        newPassTf = findViewById(R.id.newPassTf);
        confirmTf = findViewById(R.id.confirmTf);
        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.backBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = oldPassTf.getText().toString().trim();
                String newPass = newPassTf.getText().toString().trim();
                String confirmPass = confirmTf.getText().toString().trim();

                if (TextUtils.isEmpty(oldPass)) {
                    oldPassTf.setError("Please enter your old password");
                    return;
                }

                if (TextUtils.isEmpty(newPass)) {
                    newPassTf.setError("Please enter your new password");
                    return;
                }

                if (TextUtils.isEmpty(confirmPass)) {
                    confirmTf.setError("Please enter your new password again ");
                    return;
                }
                String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.{6,})";  //Password regex. Change this to change password complexity requirements
                Pattern passwordPattern = Pattern.compile(passwordRegex);
                Matcher passwordMatcher = passwordPattern.matcher(newPass);
                boolean isPasswordValid = passwordMatcher.find();

                //Checks for password complexity and lets users know what they're missing
                if (!isPasswordValid) {
                    if (!newPass.matches("(.{6,})")) {
                        newPassTf.setError("Password must contain 6 characters or more");
                        return;
                    } else if (!newPass.matches("(.*[a-z])")) {
                        newPassTf.setError("Password must contain at least one lowercase character");
                        return;
                    } else if (!newPass.matches("(.*[A-Z])")) {
                        newPassTf.setError("Password must contain at least one uppercase character");
                        return;
                    }
                }

                // Checks if passwords match
                if (newPass.equals(confirmPass)) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), oldPassTf.getText().toString().trim());

                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChangePassword.this, "Re-Authentication Success", Toast.LENGTH_LONG).show();
                                            user.updatePassword(newPassTf.getText().toString().trim())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ChangePassword.this, "Changing Password Success", Toast.LENGTH_LONG).show();
                                                                FirebaseAuth.getInstance().signOut();
                                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                            } else {
                                                                Toast.makeText(ChangePassword.this, "Changing Password Failed", Toast.LENGTH_LONG).show();
                                                            }

                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(ChangePassword.this, "Re-Authentication Failed", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });

                    } else {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }

                } else {
                    confirmTf.setError("Passwords do not match. Please try again");
                    return;


                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfilePageFragment.class));
            }
        });
}
}