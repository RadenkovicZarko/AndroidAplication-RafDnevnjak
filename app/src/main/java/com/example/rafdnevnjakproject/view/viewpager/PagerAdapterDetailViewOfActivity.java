package com.example.rafdnevnjakproject.view.viewpager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.rafdnevnjakproject.models.CalendarDate;
import com.example.rafdnevnjakproject.models.DateActivity;
import com.example.rafdnevnjakproject.view.fragments.DetailsOfActivityFragment;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class PagerAdapterDetailViewOfActivity extends FragmentStatePagerAdapter {

    private CalendarDate calendarDate;
    private List<DetailsOfActivityFragment> listOfFragments=new ArrayList<>();

    public PagerAdapterDetailViewOfActivity(@NonNull FragmentManager fm, CalendarDate calendarDate) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.calendarDate = calendarDate;
        Timber.plant(new Timber.DebugTree());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        DetailsOfActivityFragment detailsOfActivityFragment=new DetailsOfActivityFragment(this.calendarDate,position);
        listOfFragments.add(detailsOfActivityFragment);
        return detailsOfActivityFragment;
    }

    @Override
    public int getCount() {
        return calendarDate.getListOfActivities().size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Da";
    }
    @Override
    public int getItemPosition(Object object) {
// POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }
    public void setData(CalendarDate calendarDate)
    {
        this.calendarDate=calendarDate;
        int i=0;
        for (DetailsOfActivityFragment f:listOfFragments)
        {
            String time=calendarDate.getListOfActivities().get(i).getTimeFrom().toString()+" "+calendarDate.getListOfActivities().get(i).getTimeTo().toString();
            f.setData(calendarDate.getListOfActivities().get(i).getTitle(),time,calendarDate.getListOfActivities().get(i).getDescription());
        }
        notifyDataSetChanged();
    }
}
