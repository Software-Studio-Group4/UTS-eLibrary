package com.example.utselibrary;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class RegisterDetailsFragment extends Fragment {

    Button nextBtn, reverseBackBtn;
    EditText firstNameTf, lastNameTf, uniIDTf, mobileNumTf;
    ProgressBar progressBar;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RegisterDetailsFragment() {
        // Required empty public constructor
    }

    public static RegisterDetailsFragment newInstance(String param1, String param2) {
        RegisterDetailsFragment fragment = new RegisterDetailsFragment();
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
        return inflater.inflate(R.layout.register_details_fragment, container, false);
    }

    /**********************************************************************************************
     * Register Details Fragment
     * manipulates the fragment where user enters their details & registration is completed
     ************************************************************************************************/

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Parent Activity views
        nextBtn = getActivity().findViewById(R.id.nextBtn);
        reverseBackBtn = getActivity().findViewById(R.id.reverseBackBtn);
        progressBar = getActivity().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        // Register Details views
        firstNameTf = getView().findViewById(R.id.firstNameTf);
        lastNameTf = getView().findViewById(R.id.lastNameTf);
        uniIDTf = getView().findViewById(R.id.uniIDTf);
        mobileNumTf = getView().findViewById(R.id.mobileNumTf);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        mobileNumTf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String mobileNumber = mobileNumTf.getText().toString().trim();
                if (mobileNumber.length() > 2) {
                    String code = mobileNumber.substring(0, 2);
                    if (!code.equals("04")) {
                        mobileNumTf.setError("Must be a valid number");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        final Fragment RegisterComplete = new RegisterCompleteFragment();
        final FragmentManager fm = getFragmentManager();

        Bundle EmailPassBundle = this.getArguments();
        final String email = EmailPassBundle.getString("email");
        final String password = EmailPassBundle.getString("password");
        final boolean isLecturer = EmailPassBundle.getBoolean("isLecturer");

        if (isLecturer) {
            uniIDTf.setHint("Staff ID");
        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

               final String firstName = firstNameTf.getText().toString().trim();
               final String lastName = lastNameTf.getText().toString().trim();
               final String uniID = uniIDTf.getText().toString().trim();
               final String mobileNumber = mobileNumTf.getText().toString().trim();

                if (TextUtils.isEmpty(firstName)) {
                    firstNameTf.setError("Please enter your first name");
                    YoYo.with(Techniques.Shake).duration(700).playOn(firstNameTf);
                    progressBar.setVisibility(View.INVISIBLE);
                    nextBtn.setVisibility(View.VISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    lastNameTf.setError("Please enter your last name");
                    YoYo.with(Techniques.Shake).duration(700).playOn(lastNameTf);
                    progressBar.setVisibility(View.INVISIBLE);
                    nextBtn.setVisibility(View.VISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(uniID)) {
                    uniIDTf.setError("Please enter your university ID");
                    YoYo.with(Techniques.Shake).duration(700).playOn(uniIDTf);
                    progressBar.setVisibility(View.INVISIBLE);
                    nextBtn.setVisibility(View.VISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(mobileNumber)) {
                    mobileNumTf.setError("Please enter your mobile number");
                    YoYo.with(Techniques.Shake).duration(700).playOn(mobileNumTf);
                    progressBar.setVisibility(View.INVISIBLE);
                    nextBtn.setVisibility(View.VISIBLE);
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userID = fAuth.getCurrentUser().getUid();
                            final DocumentReference documentReference = fStore.collection("Users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            // Form filled fields
                            user.put("emailAddress", email);
                            user.put("firstName", firstName);
                            user.put("lastName", lastName);
                            user.put("phoneNumber", mobileNumber);
                            user.put("uniID", uniID);
                            user.put("objectID", uniID);

                            // Non-form fields
                            user.put("isLecturer", isLecturer);
                            user.put("isSuspended", false);
                            user.put("maxAllowed", 5);
                            user.put("borrowAmount", 0);
                            user.put("borrowedDocs", new ArrayList<Map>());
                            user.put("borrowHistory", new ArrayList<Map>());
                            user.put("fullName", firstName + " " + lastName);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                    fragmentTransaction
                                            .setCustomAnimations(R.anim.right_to_left, R.anim.exit_right_to_left)
                                            .replace(R.id.flFragment, RegisterComplete);
                                    fragmentTransaction.commit();

                                    reverseBackBtn.setVisibility(View.INVISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    nextBtn.setVisibility(View.VISIBLE);

                                    String id = documentReference.getId();
                                    Client client = new Client("9L80XXFOLT", "a01b448ff9270562e195ef32110d829a");
                                    Index index = client.getIndex("Users");

                                    List<JSONObject> array = new ArrayList<JSONObject>();

                                    try {
                                        array.add(
                                                new JSONObject().put("objectID", uniID).put("emailAddress", email)
                                                        .put("firstName", firstName).put("lastName", lastName)
                                                        .put("id", id).put("phoneNumber", mobileNumber).put("fullName", firstName + " " + lastName)
                                        );
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    index.addObjectsAsync(new JSONArray(array), null);
                                    Toast.makeText(getActivity().getApplicationContext(), "Document added", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Error creating account" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            nextBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
    }

}