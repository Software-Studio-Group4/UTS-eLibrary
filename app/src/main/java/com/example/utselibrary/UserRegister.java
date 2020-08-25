package com.example.utselibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.utselibrary.UserRegister.RegisterPassPge.getPass;

public class UserRegister extends AppCompatActivity {
    private static String email;
    EditText emailTf;
    Button nextBtn, backBtn;

    public static String getEmail() {
        return email;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_email);

        nextBtn = findViewById(R.id.nextBtn);
        emailTf = findViewById(R.id.emailTf);
        backBtn = findViewById(R.id.backBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailTf.getText().toString().trim();
                String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";  //Email regex. Change this to change email format required.
                Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = emailPattern.matcher(email);
                boolean isValid = matcher.find();

                if (TextUtils.isEmpty(email)) {
                    emailTf.setError("Cannot have an empty field");
                    return;
                }
                if (!isValid) {
                    emailTf.setError("Not a valid email address");
                    return;
                }
                startActivity(new Intent(getApplicationContext(), RegisterPassPge.class));


            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    /**********************************************************************************************
     * RegisterPassPge
     *manipulates the page where the patient registers password
     ************************************************************************************************/
    public static class RegisterPassPge extends AppCompatActivity {
        private static String password;
        EditText passwordTf;
        Button nextBtn2, backBtn;

        public static String getPass() { return password; }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.user_register_pass);
            passwordTf = findViewById(R.id.passwordTf);
            nextBtn2 = findViewById(R.id.nextBtn2);
            backBtn = findViewById(R.id.backBtn);

            nextBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    password = passwordTf.getText().toString().trim();
                    String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.{6,})";  //Password regex. Change this to change password complexity requirements
                    Pattern emailPattern = Pattern.compile(passwordRegex);
                    Matcher matcher = emailPattern.matcher(password);
                    boolean isValid = matcher.find();

                    if (TextUtils.isEmpty(password)) {
                        passwordTf.setError("Cannot have Empty Field");
                        return;
                    }
                    if (!isValid) {                                            //Checks for password complexity and lets users know what they're missing
                        if (!password.matches("(.{6,})")) {
                            passwordTf.setError("Password must contain 6 characters or more");
                            return;
                        } else if (!password.matches("(.*[a-z])")) {
                            passwordTf.setError("Password must contain at least one lowercase character");
                            return;
                        } else if (!password.matches("(.*[A-Z])")) {
                            passwordTf.setError("Password must contain at least one uppercase character");
                            return;
                        }
                    }
                    startActivity(new Intent(getApplicationContext(), RegisterDetailsPge.class));

                }
            });

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), UserRegister.class));

                }
            });
        }

        @Override
        public void finish() {
            super.finish();
        }
    }

    /**********************************************************************************************
     * RegisterDetailsPge
     * manipulates the page where the patient inputs registration details (such as name, address etc)
     ************************************************************************************************/
    public static class RegisterDetailsPge extends AppCompatActivity {
        String userEmail = getEmail();
        String userPass = getPass();


        EditText firstNameTf, lastNameTf, courseTf, phoneNumberTf;
        Button nextBtn3, backBtn3;
        ProgressBar progressBar;

        FirebaseAuth fAuth;
        FirebaseFirestore fStore;
        String userID;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.user_register_details);

            // GET ALL THE OBJECTS FROM THE VIEW TO MANIPULATE
            nextBtn3 = findViewById(R.id.nextBtn3);
            backBtn3 = findViewById(R.id.backBtn3);
            firstNameTf = findViewById(R.id.firstNameTf);
            lastNameTf = findViewById(R.id.lastNameTf);
            courseTf = findViewById(R.id.courseTf);
            phoneNumberTf = findViewById(R.id.phoneNumberTf);
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);

            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            phoneNumberTf.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    final String phoneNumber = phoneNumberTf.getText().toString().trim();
                    if (phoneNumber.length() > 2) {
                        String code = phoneNumber.substring(0, 2);
                        if (!code.equals("04")) {
                            phoneNumberTf.setError("Must be a valid number");
                            Toast toast = Toast.makeText(RegisterDetailsPge.this, "Invalid Number entered!", Toast.LENGTH_SHORT);
                            TextView v = toast.getView().findViewById(android.R.id.message);
                            toast.getView().setBackgroundColor(Color.RED);
                            v.setTextColor(Color.WHITE);
                            toast.show();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            nextBtn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextBtn3.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    final String firstName = firstNameTf.getText().toString().trim();
                    final String lastName = lastNameTf.getText().toString().trim();
                    final String course = courseTf.getText().toString().trim();
                    final String phoneNumber = phoneNumberTf.getText().toString().trim();


                    if (TextUtils.isEmpty(firstName)) {
                        firstNameTf.setError("Cannot have Empty Field");
                        progressBar.setVisibility(View.INVISIBLE);
                        nextBtn3.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (TextUtils.isEmpty(lastName)) {
                        lastNameTf.setError("Cannot have Empty Field");
                        progressBar.setVisibility(View.INVISIBLE);
                        nextBtn3.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (TextUtils.isEmpty(phoneNumber)) {
                        phoneNumberTf.setError("Cannot have Empty Field");
                        progressBar.setVisibility(View.INVISIBLE);
                        nextBtn3.setVisibility(View.VISIBLE);
                        return;
                    }

                    if (TextUtils.isEmpty(course)) {
                        courseTf.setError("Cannot have Empty Field");
                        progressBar.setVisibility(View.INVISIBLE);
                        nextBtn3.setVisibility(View.VISIBLE);
                        return;
                    }


                    fAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("Users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("Email", userEmail);
                                user.put("First Name", firstName);
                                user.put("Last Name", lastName);
                                user.put("Phone Number", phoneNumber);
                                user.put("Course", course);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                            startActivity(new Intent(getApplicationContext(), RegisterFinishPge.class));
                                            progressBar.setVisibility(View.INVISIBLE);
                                            nextBtn3.setVisibility(View.VISIBLE);
                                    }
                                });;
                            } else {
                                Toast.makeText(RegisterDetailsPge.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                nextBtn3.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            });

            backBtn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), RegisterPassPge.class));
                }
            });
        }

        @Override
        public void finish() {
            super.finish();
        }
    }
    /**********************************************************************************************
     * RegisterFinishPge
     * manipulates the page where it informs the patient that the registration is complete
     ************************************************************************************************/
    public static class RegisterFinishPge extends AppCompatActivity {

        Button loginBtn;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.user_register_end);

            loginBtn = findViewById(R.id.loginBtn);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), UserLogin.class));
                }
            });

        }

        @Override
        public void finish() {
            super.finish();
        }
    }


    /**********************************************************************************************
     * OTHER METHODS
     ************************************************************************************************/
    @Override
    public void finish() {
        super.finish();

    } // Fade transition
}
