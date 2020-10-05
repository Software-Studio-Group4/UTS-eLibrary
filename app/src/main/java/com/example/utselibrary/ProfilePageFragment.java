package com.example.utselibrary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nullable;

import maes.tech.intentanim.CustomIntent;

public class ProfilePageFragment extends Fragment {

    TextView userNameTf, emailTf, mobileNumTf;
    Button logoutBtn, finesBtn;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String userID = fAuth.getCurrentUser().getUid();
    DocumentReference userDocRef = fStore.collection("Users").document(userID);

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfilePageFragment() {
    }

    public static ProfilePageFragment newInstance(String param1, String param2) {
        ProfilePageFragment fragment = new ProfilePageFragment();
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
        return inflater.inflate(R.layout.profile_page_fragment, container, false);
    }

    /**********************************************************************************************
     * User Profile Fragment
     * manipulates the fragment where the users profile is displayed
     ************************************************************************************************/

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get views
        userNameTf = getView().findViewById(R.id.userNameTf);
        emailTf = getView().findViewById(R.id.emailTf);
        mobileNumTf = getView().findViewById(R.id.mobileNumTf);
        logoutBtn = getView().findViewById(R.id.logoutBtn);
        finesBtn = getView().findViewById(R.id.finesBtn);

        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String firstName = documentSnapshot.getString("firstName");
                    String lastName = documentSnapshot.getString("lastName");
                    String emailAddress = documentSnapshot.getString("emailAddress");
                    String mobileNum = documentSnapshot.getString("phoneNumber");
                    String uniID = documentSnapshot.getString("uniID");

                    String userName = firstName + " " + lastName + " ";
                    userNameTf.setText(userName + "(" + uniID + ")");
                    emailTf.setText(emailAddress);
                    mobileNumTf.setText(mobileNum);
                } else {
                    Toast.makeText(getContext(), "Error: Details cannot be displayed", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error: Details cannot be displayed", Toast.LENGTH_LONG).show();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), MainActivity.class));
                CustomIntent.customType(getContext(), "fadein-to-fadeout");
                getActivity().finish();
            }
        });

        finesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Fines page does not exist yet", Toast.LENGTH_LONG).show();
            }
        });
    }
}