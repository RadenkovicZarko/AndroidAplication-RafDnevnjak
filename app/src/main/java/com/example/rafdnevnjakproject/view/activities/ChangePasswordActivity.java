package com.example.rafdnevnjakproject.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rafdnevnjakproject.R;
import com.example.rafdnevnjakproject.models.User;
import com.example.rafdnevnjakproject.view.fragments.DailyPlanFragment;
import com.example.rafdnevnjakproject.view.fragments.ProfileFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import timber.log.Timber;

public class ChangePasswordActivity extends AppCompatActivity {

    private User user;
    private Button btnSaveNewPassword;
    private Button btnCancel;
    private EditText password;
    private EditText retypedPassword;

    public static final String PROFILE_CHANGE_PASSWORD_SEND_DATA="SendDataProfileChangePassword";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        user=getIntent().getParcelableExtra(PROFILE_CHANGE_PASSWORD_SEND_DATA);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_home);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitleActionBar)).setText("Profile");

        Timber.plant(new Timber.DebugTree());
        init();
    }

    private void init()
    {
        initView();
        initValues();
        initListeners();
    }

    private void initValues() {

    }

    private void initView() {
        btnSaveNewPassword = findViewById(R.id.btnSaveNewPassword);
        password = findViewById(R.id.passwordEdt);
        retypedPassword = findViewById(R.id.retypePasswordEdt);
        btnCancel=findViewById(R.id.btnCancelPasswordChange);
    }

    private void initListeners()
    {
        btnSaveNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    String p=password.getText().toString();
                    String rp=retypedPassword.getText().toString();
                    if(p.isEmpty() || rp.isEmpty())
                    {
                        Toast.makeText(ChangePasswordActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    else if(!p.equals(rp))
                    {
                        Toast.makeText(ChangePasswordActivity.this, "Retype password again", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    else if(user.getPassword().equals(rp))
                    {
                        Toast.makeText(ChangePasswordActivity.this, "Password cannot be same as last one", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    else if(p.length()<5 || !Pattern.compile("[A-Z ]").matcher(p).find() ||
                            !Pattern.compile("[0-9 ]").matcher(p).find() ||
                            (p.contains("~") || p.contains("#") || p.contains("^") || p.contains("|") || p.contains("$")
                                    || p.contains("%") || p.contains("&") || p.contains("*") || p.contains("!")))
                    {
                        Toast.makeText(ChangePasswordActivity.this, "Password is not valid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        try {
                            user.setPassword(p);
                            String file=getFilesDir()+"/"+"fajl.txt";
                            BufferedReader reader = new BufferedReader(new FileReader(file));
                            List<String> lines = new ArrayList<>();
                            String line = reader.readLine();
                            while (line != null) {
                                lines.add(line);
                                line = reader.readLine();
                            }
                            reader.close();
                            // Modifying the file
                            lines.set(2, user.getPassword());
                            // Writing the modified file back to disk
                            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                            for (String newLine : lines) {
                                writer.write(newLine);
                                writer.newLine();
                            }
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent();
                        intent.putExtra(ProfileFragment.PROFILE_RECEIVED_MESSAGE, (Parcelable) user);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(ProfileFragment.PROFILE_RECEIVED_MESSAGE, (Parcelable) user);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
    }

}
