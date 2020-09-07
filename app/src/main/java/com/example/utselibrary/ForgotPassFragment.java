package com.example.utselibrary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import javax.annotation.Nullable;

public class ForgotPassFragment extends Fragment {

    Button resetBtn, closeBtn;
    EditText emailTf;
    TextView registerText;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ForgotPassFragment() {
        // Required empty public constructor
    }

    public static ForgotPassFragment newInstance(String param1, String param2) {
        ForgotPassFragment fragment = new ForgotPassFragment();
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
        return inflater.inflate(R.layout.forgot_pass_fragment, container, false);
    }

    /**********************************************************************************************
     * Password reset fragment
     * manipulates the fragment where user can reset their password
     ************************************************************************************************/

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get parent views
        registerText = getActivity().findViewById(R.id.registerText);
        registerText.setVisibility(View.INVISIBLE);
        // Get fragment views
        resetBtn = getView().findViewById(R.id.resetBtn);
        closeBtn = getView().findViewById(R.id.closeBtn);
        emailTf = getView().findViewById(R.id.emailTf);
        progressBar =getView().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        fAuth = FirebaseAuth.getInstance();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTf.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailTf.setError("PLease enter your SWUT email");
                    return;
                }

                if (email.contains("@student.swut.edu.au") || email.contains("@staff.swut.edu.au")) {
                    ;
                } else {
                    emailTf.setError("Please enter your SWUT email");
                    return;
                }

                resetBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            resetBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "Password reset email sent", Toast.LENGTH_LONG).show();
                            emailTf.getText().clear();
                        }
                    }
                });
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment LoginFragment = new LoginFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction
                        .setCustomAnimations(R.anim.bottom_to_top, R.anim.exit_bottom_to_top)
                        .replace(R.id.flFragment, LoginFragment);
                fragmentTransaction.commit();
                onStop();
            }
        });
    }
}