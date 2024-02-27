package com.example.rafdnevnjakproject.view.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;


import com.example.rafdnevnjakproject.models.User;
import com.example.rafdnevnjakproject.viewmodels.SplashViewModel;
import com.example.rafdnevnjakproject.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.Timer;
import java.util.regex.Pattern;

import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {
    private SplashViewModel splashViewModel;
    public static final String LOGIN_KEY = "loginKey";
    private Button loginButton;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;

    private TextView errorEmail;
    private TextView errorUsername;
    private TextView errorPassword;
    private final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        // Handle the splash screen transition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return false;
//            Boolean value = splashViewModel.isLoading().getValue();
//            if (value == null) return false;
//            return value;
        });

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String message = sharedPreferences.getString(LOGIN_KEY, null);
        if(message!=null)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);
        init();
    }
    private void init() {
        initView();
        initListeners();
    }


    private void initView() {
        loginButton=findViewById(R.id.loginButton);
        etUsername=findViewById(R.id.etUsername);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        errorEmail=findViewById(R.id.errorEmail);
        errorUsername=findViewById(R.id.errorUsername);
        errorPassword=findViewById(R.id.errorPassword);
    }

    private void initListeners() {
        loginButton.setOnClickListener(v -> {
            int lineNum = 3;
            String line=null;
            String e=null;
            String n=null;
            try {
                requestPermissions(PERMISSIONS,1);
                String file = getFilesDir() + "/" + "fajl.txt";
                BufferedReader reader = new BufferedReader(new FileReader(file));
                line = reader.readLine();
                int count = 1;
                while (line != null  && count <= lineNum) {
                    Timber.e(line);
                    if(count==1)
                        e=line;
                    else if(count==2)
                        n=line;
                    if (count == lineNum) {
                        break;
                    }
                    count++;
                    line = reader.readLine();

                }
                reader.close();
                Timber.e(line);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            String username = String.valueOf(etUsername.getText());
            String email = String.valueOf(etEmail.getText());
            String password = String.valueOf(etPassword.getText());
            if (username.isEmpty()) {
                errorUsername.setVisibility(View.VISIBLE);
                errorUsername.setText("Username cannot be empty");
//                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            }
            else if(email.isEmpty())
            {
                errorEmail.setVisibility(View.VISIBLE);
                errorEmail.setText("Email cannot be empty");
            }
            else if(password.isEmpty()){
                errorPassword.setVisibility(View.VISIBLE);
                errorPassword.setText("Password cannot be empty");

            }else if(!email.contains("@"))
                Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show();
            else if(password.length()<5 || !Pattern.compile("[A-Z ]").matcher(password).find() ||
                    !Pattern.compile("[0-9 ]").matcher(password).find() ||
                    (password.contains("~") || password.contains("#") || password.contains("^") || password.contains("|") || password.contains("$")
                    || password.contains("%") || password.contains("&") || password.contains("*") || password.contains("!"))) {
                Toast.makeText(this, "Password is not valid", Toast.LENGTH_SHORT).show();
            }
            else if(!password.equals(line)) {
                Toast.makeText(this, "Password is not correct", Toast.LENGTH_SHORT).show();
            }
            else if(!username.equals(n))
            {
                Toast.makeText(this, "Username is not correct", Toast.LENGTH_SHORT).show();

            }
            else if(!email.equals(e))
            {
                Toast.makeText(this, "Email is not correct", Toast.LENGTH_SHORT).show();

            }
            else
            {

                Gson gson = new Gson();
                String json = gson.toJson(new User(username,email,password));

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(LOGIN_KEY,json);
                editor.apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back press disabled", Toast.LENGTH_SHORT).show();
    }

    public static String serializeObject(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public static <T> T getObjectFromString(String serializedString) throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.decode(serializedString, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        return (T) object;
    }



}
