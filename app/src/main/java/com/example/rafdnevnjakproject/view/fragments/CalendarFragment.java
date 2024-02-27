package com.example.rafdnevnjakproject.view.fragments;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TintableCheckedTextView;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rafdnevnjakproject.R;

import com.example.rafdnevnjakproject.models.CalendarDate;
import com.example.rafdnevnjakproject.view.activities.MainActivity;
import com.example.rafdnevnjakproject.view.recycler.adapter.CalendarAdapter;
import com.example.rafdnevnjakproject.view.recycler.differ.CalendarDateCallback;
import com.example.rafdnevnjakproject.view.viewpager.PagerAdapter;
import com.example.rafdnevnjakproject.viewmodels.RecyclerViewModel;
import com.example.rafdnevnjakproject.viewmodels.SharedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import timber.log.Timber;

public class CalendarFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewModel recyclerViewModel;
    private CalendarAdapter calendarAdapter;

    private int lastVisibleItemPosition;
    private int firstVisibleItemPosition;
    private LinearLayoutManager layoutManager;
    private SharedViewModel sharedViewModel;

    private int mScrollPosition = 0;


    public CalendarFragment() {
        super(R.layout.fragment_calendar);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mScrollPosition!=0) {
            RecyclerView recyclerView = getView().findViewById(R.id.my_recycler_view);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            layoutManager.onRestoreInstanceState(layoutManager
                    .onSaveInstanceState());
            layoutManager.scrollToPosition(mScrollPosition);
        }
        HashMap<String,Integer> map=new HashMap<>();
        int maxi=Integer.MIN_VALUE;
        String maxiMonth="";
        String maxiYear="";
        for(int i=firstVisibleItemPosition;i<lastVisibleItemPosition;i++)
        {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            String monthString = sdf.format(recyclerViewModel.getDates().getValue().get(i).getDate());
            String year = dateFormat.format(recyclerViewModel.getDates().getValue().get(i).getDate());
            if(map.containsKey(monthString)){
                map.put(monthString,map.get(monthString)+1);
                if(map.get(monthString)>maxi)
                {
                    maxi=map.get(monthString);
                    maxiMonth=monthString;
                    maxiYear=year;
                }
            }
            else {
                map.put(monthString, 1);
                if(map.get(monthString)>maxi)
                {
                    maxi=map.get(monthString);
                    maxiMonth=monthString;
                    maxiYear=year;
                }
            }
        }
        if(mScrollPosition!=0)
            ((TextView) ((AppCompatActivity) getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.tvTitleActionBar)).setText(maxiMonth + " " + maxiYear + ".");


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            mScrollPosition = savedInstanceState.getInt("scroll_position");
        }
        recyclerViewModel = new ViewModelProvider(getActivity()).get(RecyclerViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.storeCalendarDate(recyclerViewModel.getDates().getValue());

        init(view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("scroll_position", mScrollPosition);
    }


    private void init(View view) {
        initView(view);
        initListeners();
        initRecycler();
        initObservers();
    }

    private void initListeners()
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    HashMap<String,Integer> map=new HashMap<>();
                    int maxi=Integer.MIN_VALUE;
                    String maxiMonth="";
                    String maxiYear="";
                    for(int i=layoutManager.findFirstVisibleItemPosition();i<layoutManager.findLastVisibleItemPosition();i++)
                    {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                        String monthString = sdf.format(recyclerViewModel.getDates().getValue().get(i).getDate());
                        String year = dateFormat.format(recyclerViewModel.getDates().getValue().get(i).getDate());
                        if(map.containsKey(monthString)){
                            map.put(monthString,map.get(monthString)+1);
                            if(map.get(monthString)>maxi)
                            {
                                maxi=map.get(monthString);
                                maxiMonth=monthString;
                                maxiYear=year;
                            }
                        }
                        else {
                            map.put(monthString, 1);
                            if(map.get(monthString)>maxi)
                            {
                                maxi=map.get(monthString);
                                maxiMonth=monthString;
                                maxiYear=year;
                            }
                        }
                    }

//                    getActivity().setTitle(maxiMonth+" "+maxiYear+".");
                    ((TextView)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.tvTitleActionBar)).setText(maxiMonth+" "+maxiYear+".");
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                mScrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findFirstCompletelyVisibleItemPosition();
                firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition();

            }
        });
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.my_recycler_view);
        layoutManager= new GridLayoutManager(getActivity(),7);
    }


    private void initObservers() {
        recyclerViewModel.getDates().observe(getActivity(), calendarDates -> {
                calendarAdapter.submitList(calendarDates);
        });

        sharedViewModel.getCalendarDateLiveData().observe(getViewLifecycleOwner(), calendarDateList -> {
            calendarAdapter.submitList(calendarDateList);
            calendarAdapter.notifyDataSetChanged();
        });
    }


    private void initRecycler() {
        calendarAdapter=new CalendarAdapter(new CalendarDateCallback(),calendarDate -> {
            ((MainActivity)this.requireActivity()).getViewPager().setCurrentItem(PagerAdapter.FRAGMENT_2, false);
            BottomNavigationView bottomNavigationView=((MainActivity)this.requireActivity()).findViewById(R.id.bottomNavigation);
            MenuItem menuItem=bottomNavigationView.getMenu().findItem(R.id.navigation_2);
            menuItem.setChecked(true);
            sharedViewModel.storeSelectedDate(recyclerViewModel.getDates().getValue().indexOf(calendarDate));
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(calendarAdapter);


        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.JANUARY, 6);
        Date startDate = calendar.getTime();

        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int daysBetween =(int) ChronoUnit.DAYS.between(localStartDate, localEndDate);
        recyclerView.scrollToPosition(daysBetween-14);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String monthString = sdf.format(new Date());
        String year = dateFormat.format(new Date());

        ((TextView)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.tvTitleActionBar)).setText(monthString+" "+year+".");

//        getActivity().setTitle(monthString+" "+year+".");
    }

}
