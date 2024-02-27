package com.example.rafdnevnjakproject.view.recycler.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rafdnevnjakproject.R;
import com.example.rafdnevnjakproject.models.CalendarDate;
import com.example.rafdnevnjakproject.models.DateActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.function.Consumer;

import timber.log.Timber;

public class CalendarAdapter extends ListAdapter<CalendarDate, CalendarAdapter.ViewHolder> {

    private final Consumer<CalendarDate> onCalendarDateClicked;

    public CalendarAdapter(@NonNull DiffUtil.ItemCallback<CalendarDate> diffCallback, Consumer<CalendarDate> onCalendarDateClicked) {
        super(diffCallback);
        this.onCalendarDateClicked = onCalendarDateClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_date_item, parent, false);
            view.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.border_shape));
            return new ViewHolder(view, parent.getContext(), position -> {
            CalendarDate calendarDate = getItem(position);
            onCalendarDateClicked.accept(calendarDate);
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CalendarDate calendarDate = getItem(position);
        holder.bind(calendarDate);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        public ViewHolder(@NonNull View itemView, Context context, Consumer<Integer> onItemClicked) {
            super(itemView);
            Timber.plant(new Timber.DebugTree());
            this.context = context;
            itemView.setOnClickListener(v -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClicked.accept(getBindingAdapterPosition());
                    itemView.getRootView().getRootView().findViewById(R.id.navigation_2).setAlpha(1); itemView.getRootView().getRootView().findViewById(R.id.navigation_1).setAlpha(0.3f); itemView.getRootView().getRootView().findViewById(R.id.navigation_3).setAlpha(0.3f);
                }
            });
        }

        public void bind(CalendarDate calendarDate) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("d");
            String dayString = sdf.format(calendarDate.getDate());
            ((TextView) itemView.findViewById(R.id.dateTv)).setText(dayString);


            int maxiPriority=findMaxPriority(calendarDate);

            if(maxiPriority==1 && calendarDate.getListOfActivities().size()>0)
            {
                itemView.setBackgroundColor(Color.RED);
            }
            else if(maxiPriority==2 && calendarDate.getListOfActivities().size()>0)
            {
                itemView.setBackgroundColor(Color.YELLOW);
            }
            else if(maxiPriority==3 && calendarDate.getListOfActivities().size()>0)
            {
                itemView.setBackgroundColor(Color.GREEN);
            }
            else
            {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.background));
            }
        }

        private int findMaxPriority(CalendarDate calendarDate)
        {
            int min=4;

            for(DateActivity dateActivity:calendarDate.getListOfActivities())
            {

                if(dateActivity.getPriority()<min)
                    min=dateActivity.getPriority();
            }
            return min;
        }

    }


}
