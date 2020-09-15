package com.example.utselibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.SharedMemory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nullable;

import maes.tech.intentanim.CustomIntent;

public class LoginFragment extends Fragment {

    Button loginBtn;
    EditText emailTf, passwordTf;
    TextView forgotPassTV, registerText;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    private String email;
    private String password;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    /**********************************************************************************************
     * User and admin login fragment
     * manipulates the fragment where user and admin logs in
     ************************************************************************************************/

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // Get fragment views
        loginBtn = getView().findViewById(R.id.loginBtn);
        emailTf = getView().findViewById(R.id.emailTf);
        passwordTf = getView().findViewById(R.id.passwordTf);
        forgotPassTV = getView().findViewById(R.id.forgotPassTV);
        progressBar = getView().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Initialize fragment manager
        final Fragment ForgotPassFragment = new ForgotPassFragment();
        final FragmentManager fm = getFragmentManager();

        // Auto sign in user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            loginBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Signing in, please wait", Toast.LENGTH_SHORT).show();
            userID = fAuth.getCurrentUser().getUid();
            DocumentReference adminRef = fStore.collection("Admin").document(userID);
            adminRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        startActivity(new Intent(getActivity(), AdminDashboard.class));
                        CustomIntent.customType(getActivity(), "left-to-right");
                        progressBar.setVisibility(View.INVISIBLE);
                        loginBtn.setVisibility(View.VISIBLE);
                        getActivity().finish();
                    } else {
                        startActivity(new Intent(getActivity(), UserDashboard.class));
                        CustomIntent.customType(getActivity(), "left-to-right");
                        progressBar.setVisibility(View.INVISIBLE);
                        loginBtn.setVisibility(View.VISIBLE);
                        getActivity().finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    loginBtn.setVisibility(View.VISIBLE);
                }
            });
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailTf.getText().toString().trim();
                password = passwordTf.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailTf.setError("Enter your SWUT email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordTf.setError("Enter your password");
                    return;
                }

                loginBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                // Firebase sign in
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference adminRef = fStore.collection("Admin").document(userID);
                            adminRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        startActivity(new Intent(getActivity(), AdminDashboard.class));
                                        CustomIntent.customType(getActivity(), "left-to-right");
                                        progressBar.setVisibility(View.INVISIBLE);
                                        loginBtn.setVisibility(View.VISIBLE);
                                        getActivity().finish();
                                    } else {
                                        startActivity(new Intent(getActivity(), UserDashboard.class));
                                        CustomIntent.customType(getActivity(), "left-to-right");
                                        progressBar.setVisibility(View.INVISIBLE);
                                        loginBtn.setVisibility(View.VISIBLE);
                                        getActivity().finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    loginBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            loginBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }


        });

        forgotPassTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction
                        .setCustomAnimations(R.anim.bottom_to_up, R.anim.exit_bottom_to_top)
                        .replace(R.id.flFragment, ForgotPassFragment);
                fragmentTransaction.commit();
            }
        });

    }
}