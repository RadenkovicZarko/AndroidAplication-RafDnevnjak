package com.example.rafdnevnjakproject.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;


import com.example.rafdnevnjakproject.R;
import com.example.rafdnevnjakproject.models.CalendarDate;
import com.example.rafdnevnjakproject.models.User;
import com.example.rafdnevnjakproject.view.activities.ChangePasswordActivity;
import com.example.rafdnevnjakproject.view.activities.LoginActivity;
import com.example.rafdnevnjakproject.viewmodels.RecyclerViewModel;
import com.example.rafdnevnjakproject.viewmodels.SharedViewModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.PropertyReference0Impl;
import timber.log.Timber;


public class ProfileFragment extends Fragment {

    private TextView tvName;
    private TextView tvEmail;
    private Button changePasswordBtn;
    private Button logOutBtn;
    private User user;

    public static final String PROFILE_RECEIVED_MESSAGE="ProfileChangePasswordKey";
    ActivityResultLauncher<Intent> profileChangePasswordResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    user=data.getParcelableExtra(PROFILE_RECEIVED_MESSAGE);
                    Toast.makeText(getActivity(), "Password is changed", Toast.LENGTH_SHORT).show();

                    try {
                        String file = getContext().getFilesDir() + "/" + "fajl.txt";
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        String line = reader.readLine();
                        while (line != null) {
                            Timber.e(line);
                            line = reader.readLine();

                        }
                        reader.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                else if(result.getResultCode() != Activity.RESULT_CANCELED){
                    Timber.e("Not good");
                }
            });

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        initView(view);
        initValues();
        initListeners();
    }

    private void initView(View view) {
        tvName=view.findViewById(R.id.tvName);
        tvEmail=view.findViewById(R.id.tvEmail);
        changePasswordBtn=view.findViewById(R.id.changePassword);
        logOutBtn=view.findViewById(R.id.logOut);
        SharedPreferences preferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        String serializedObject = preferences.getString(LoginActivity.LOGIN_KEY, null);
        Timber.e(serializedObject);
        Gson gson = new Gson();
        this.user = gson.fromJson(serializedObject, User.class);

    }
    private void initValues()
    {
        tvName.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
    }

    public void initListeners()
    {
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ChangePasswordActivity.class);
                intent.putExtra(ChangePasswordActivity.PROFILE_CHANGE_PASSWORD_SEND_DATA, (Parcelable) user);
                profileChangePasswordResultLauncher.launch(intent);
            }
        });
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences= getContext().getSharedPreferences(getContext().getPackageName(),Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

}