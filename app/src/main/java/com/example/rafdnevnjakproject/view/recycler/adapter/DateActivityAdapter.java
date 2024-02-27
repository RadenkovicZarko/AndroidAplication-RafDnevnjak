package com.example.rafdnevnjakproject.view.recycler.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rafdnevnjakproject.R;
import com.example.rafdnevnjakproject.models.CalendarDate;
import com.example.rafdnevnjakproject.models.DateActivity;
import com.example.rafdnevnjakproject.view.activities.EditActivity;
import com.google.android.material.snackbar.Snackbar;

import java.nio.channels.CancelledKeyException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import timber.log.Timber;

public class DateActivityAdapter extends ListAdapter<DateActivity, DateActivityAdapter.ViewHolder> {

    private final Consumer<DateActivity> onDateActivityClicked;
    private LauncherCallback launcherCallback;

    public DateActivityAdapter(@NonNull DiffUtil.ItemCallback<DateActivity> diffCallback, Consumer<DateActivity> onDateActivityClicked, LauncherCallback launcherCallback) {
        super(diffCallback);
        this.onDateActivityClicked = onDateActivityClicked;
        this.launcherCallback=launcherCallback;
    }

    @NonNull
    @Override
    public DateActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_activity_item, parent, false);
        return new DateActivityAdapter.ViewHolder(view, parent.getContext(), position -> {
            DateActivity dateActivity = getItem(position);
            onDateActivityClicked.accept(dateActivity);
        });
    }


    @Override
    public void onBindViewHolder(@NonNull DateActivityAdapter.ViewHolder holder, int position) {
        DateActivity dateActivity = getItem(position);
        holder.bind(dateActivity,launcherCallback,position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;


        public ViewHolder(@NonNull View itemView, Context context, Consumer<Integer> onItemClicked) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(v -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClicked.accept(getBindingAdapterPosition());
                }
            });
        }



        public void bind(DateActivity dateActivity,LauncherCallback launcherCallback,int position) {
//            ((TextView) itemView.findViewById(R.id.dateTv)).setText(dayString);
            ((TextView) itemView.findViewById(R.id.timeTv)).setText(dateActivity.getTimeFrom().toString()+" - "+dateActivity.getTimeTo().toString());
            ((TextView) itemView.findViewById(R.id.activityTv)).setText(dateActivity.getTitle());
            // TODO to set color of priority if()
            if (dateActivity.getPriority() == 1)
            {
                ((ImageView) itemView.findViewById(R.id.datePictureIv)).setBackgroundColor(Color.RED);
            }
            else if(dateActivity.getPriority()==2)
            {
                ((ImageView) itemView.findViewById(R.id.datePictureIv)).setBackgroundColor(Color.YELLOW);
            }
            else {
                ((ImageView) itemView.findViewById(R.id.datePictureIv)).setBackgroundColor(Color.GREEN);
            }

            Date currentDate = new Date();
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(currentDate);
            Calendar dateActivityCalendar = Calendar.getInstance();
            dateActivityCalendar.setTime(dateActivity.getDate());

            int currentYear = currentCalendar.get(Calendar.YEAR);
            int currentMonth = currentCalendar.get(Calendar.MONTH);
            int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

            int otherYear = dateActivityCalendar.get(Calendar.YEAR);
            int otherMonth = dateActivityCalendar.get(Calendar.MONTH);
            int otherDay = dateActivityCalendar.get(Calendar.DAY_OF_MONTH);

            // Compare the dates based on day, month, and year
            if (currentYear > otherYear || (currentYear == otherYear && currentMonth > otherMonth)
                    || (currentYear == otherYear && currentMonth == otherMonth && currentDay > otherDay)) {
                itemView.setBackgroundColor(Color.GRAY);
            } else if(currentYear == otherYear && currentMonth == otherMonth && currentDay == otherDay){
                if(dateActivity.getTimeFrom().compareTo(LocalTime.now())<0)
                {
                    itemView.setBackgroundColor(Color.GRAY);
                }
                else
                    itemView.setBackgroundColor(Color.WHITE);
            }
            else {
                itemView.setBackgroundColor(Color.WHITE);
            }


            ((ImageButton)itemView.findViewById(R.id.editBtn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launcherCallback.editlaunchResult(position);
                }
            });

            ((ImageButton)itemView.findViewById(R.id.deleteBtn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar snackbar = Snackbar.make(view, "Do you really want to delete this activity?", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Yes", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            launcherCallback.deleteLaunchResult(position);
                        }
                    });
                    snackbar.show();
                }

            });



        }

    }


}