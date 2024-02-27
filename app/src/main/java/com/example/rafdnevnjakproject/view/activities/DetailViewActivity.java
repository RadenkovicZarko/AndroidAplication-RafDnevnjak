package com.example.rafdnevnjakproject.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.rafdnevnjakproject.R;
import com.example.rafdnevnjakproject.models.CalendarDate;
import com.example.rafdnevnjakproject.view.fragments.DailyPlanFragment;
import com.example.rafdnevnjakproject.view.fragments.DetailsOfActivityFragment;
import com.example.rafdnevnjakproject.view.viewpager.PagerAdapter;
import com.example.rafdnevnjakproject.view.viewpager.PagerAdapterDetailViewOfActivity;

import timber.log.Timber;

public class DetailViewActivity extends AppCompatActivity {
    public static final String SEND_DATA="DailyPlanFragmentSend";
    public static final String INDEX_SEND_DATA="DailyPlanFragmentIndex";
    private ViewPager viewPager;
    private CalendarDate calendarDate;
    private int index;
    private PagerAdapterDetailViewOfActivity pagerAdapterDetailViewOfActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        calendarDate=getIntent().getParcelableExtra(SEND_DATA);
        index=getIntent().getIntExtra(INDEX_SEND_DATA,0);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_home);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitleActionBar)).setText(calendarDate.toString());

        Timber.plant(new Timber.DebugTree());
        init();
    }


    private void init() {
        initView();
        initViewPager();
    }
    private void initView()
    {
        viewPager=findViewById(R.id.viewPagerDetailView);
    }

    private void initViewPager() {
        pagerAdapterDetailViewOfActivity=new PagerAdapterDetailViewOfActivity(getSupportFragmentManager(),calendarDate);
        viewPager.setAdapter(pagerAdapterDetailViewOfActivity);
        viewPager.setCurrentItem(index);
    }

    public void storeNewCalendarDate(CalendarDate calendarDate)
    {
        Timber.e(calendarDate.getListOfActivities().get(1).getTitle());
        this.calendarDate=calendarDate;

        pagerAdapterDetailViewOfActivity.setData(calendarDate);
        pagerAdapterDetailViewOfActivity.notifyDataSetChanged();
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public CalendarDate getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(CalendarDate calendarDate) {
        this.calendarDate = calendarDate;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(DailyPlanFragment.EDIT_ACTIVITY_RECEIVED_MESSAGE,calendarDate);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
