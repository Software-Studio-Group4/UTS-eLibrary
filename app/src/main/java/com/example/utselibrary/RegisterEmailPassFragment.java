package com.example.utselibrary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

public class RegisterEmailPassFragment extends Fragment {

    Button nextBtn, backBtn, reverseBackBtn;
    EditText emailTf, passwordTf, confirmPassTf;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RegisterEmailPassFragment() {
        // Required empty public constructor
    }

    public static RegisterEmailPassFragment newInstance(String param1, String param2) {
        RegisterEmailPassFragment fragment = new RegisterEmailPassFragment();
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
        return inflater.inflate(R.layout.register_email_pass_fragment, container, false);
    }

    /**********************************************************************************************
     * Register Email and Password Fragment
     * manipulates the fragment where user enters their email and password
     ************************************************************************************************/

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Parent Activity views
        nextBtn = getActivity().findViewById(R.id.nextBtn);
        backBtn = getActivity().findViewById(R.id.backBtn);
        reverseBackBtn = getActivity().findViewById(R.id.reverseBackBtn);

        // Register Email & Pass Fragment views
        emailTf = getView().findViewById(R.id.emailTf);
        passwordTf = getView().findViewById(R.id.passwordTf);
        confirmPassTf = getView().findViewById(R.id.confirmPasswordTf);

        final Fragment RegisterDetailsFragment = new RegisterDetailsFragment();
        final FragmentManager fm = getFragmentManager();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailTf.getText().toString();
                final String password = passwordTf.getText().toString().trim();
                final String confirmPass = confirmPassTf.getText().toString().trim();
                boolean isLecturer = false;

                if (TextUtils.isEmpty(email)) {
                    emailTf.setError("Enter your SWUT email");
                    return;
                }

                // Checks email format
                String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";  //Email regex. Change this to change email format required.
                Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
                Matcher emailMatcher = emailPattern.matcher(email);
                boolean isEmailValid = emailMatcher.find();
                if (!isEmailValid) {
                    emailTf.setError("Invalid email address.");
                    return;
                }

                // Checks if email contains university email handler
                if (email.contains("@student.swut.edu.au") || email.contains("@staff.swut.edu.au")) {
                    if (email.contains("@staff.swut.edu.au")) {
                        isLecturer = true;
                    } else {
                        ;
                    }
                } else {
                    emailTf.setError("Please enter your SWUT email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordTf.setError("Please choose a strong password");
                    return;
                }

                if (TextUtils.isEmpty(confirmPass)) {
                    confirmPassTf.setError("Please enter your password again");
                    return;
                }

                String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.{6,})";  //Password regex. Change this to change password complexity requirements
                Pattern passwordPattern = Pattern.compile(passwordRegex);
                Matcher passwordMatcher = passwordPattern.matcher(password);
                boolean isPasswordValid = passwordMatcher.find();

                //Checks for password complexity and lets users know what they're missing
                if (!isPasswordValid) {
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

                // Checks if passwords match
                if (password.equals(confirmPass)) {
                    ;
                } else {
                    confirmPassTf.setError("Passwords do not match. Please try again");
                    return;
                }

                Bundle EmailPassBundle = new Bundle();
                EmailPassBundle.putString("email", emailTf.getText().toString());
                EmailPassBundle.putString("password", passwordTf.getText().toString());
                EmailPassBundle.putBoolean("isLecturer", isLecturer);
                RegisterDetailsFragment.setArguments(EmailPassBundle);

                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction
                        .setCustomAnimations(R.anim.right_to_left, R.anim.exit_right_to_left)
                        .replace(R.id.flFragment, RegisterDetailsFragment);
                fragmentTransaction.commit();

                // Register Details Fragment launched

                backBtn.setVisibility(View.INVISIBLE);
                reverseBackBtn.setVisibility(View.VISIBLE);
            }
        });
    }

}
