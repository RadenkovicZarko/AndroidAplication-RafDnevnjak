package com.example.rafdnevnjakproject.view.recycler.differ;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.rafdnevnjakproject.models.CalendarDate;

public class CalendarDateCallback extends DiffUtil.ItemCallback<CalendarDate> {

    @Override
    public boolean areItemsTheSame(@NonNull CalendarDate oldItem, @NonNull CalendarDate newItem) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(@NonNull CalendarDate oldItem, @NonNull CalendarDate newItem) {
        return false;
    }
}
