package com.example.utselibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import javax.annotation.Nullable;

import maes.tech.intentanim.CustomIntent;

public class AdminDashboardFragment extends Fragment {

    Button viewUsersBtn, viewRequestsBtn, logoutBtn;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AdminDashboardFragment() {
        // Required empty public constructor
    }

    public static AdminDashboardFragment newInstance(String param1, String param2) {
        AdminDashboardFragment fragment = new AdminDashboardFragment();
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
        return inflater.inflate(R.layout.admin_dashboard_fragment, container, false);
    }

    /**********************************************************************************************
     * Admin Dashboard Fragment
     * manipulates the fragment where the admin can navigate to other pages/logout
     ************************************************************************************************/
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get views
        viewUsersBtn = getView().findViewById(R.id.viewUsersBtn);
        viewRequestsBtn = getView().findViewById(R.id.viewRequestsBtn);
        logoutBtn = getView().findViewById(R.id.logoutBtn);

        //Initialize fragments
        final Fragment ViewUsersFragment = new ViewUsersFragment();
        final FragmentManager fm = getFragmentManager();

        viewUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(R.id.flFragment, ViewUsersFragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        viewRequestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminViewRequests.class));
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
    }

}