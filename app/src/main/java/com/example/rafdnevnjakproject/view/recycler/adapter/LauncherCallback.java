package com.example.rafdnevnjakproject.view.recycler.adapter;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;

public interface LauncherCallback {
    void editlaunchResult(int position);
    void deleteLaunchResult(int position);
}
