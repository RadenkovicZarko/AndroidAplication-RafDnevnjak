package com.example.rafdnevnjakproject.view.recycler.differ;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.rafdnevnjakproject.models.CalendarDate;
import com.example.rafdnevnjakproject.models.DateActivity;

public class DateActivityDiffItemCallback extends DiffUtil.ItemCallback<DateActivity> {


    @Override
    public boolean areItemsTheSame(@NonNull DateActivity oldItem, @NonNull DateActivity newItem) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(@NonNull DateActivity oldItem, @NonNull DateActivity newItem) {
        return false;
    }
}