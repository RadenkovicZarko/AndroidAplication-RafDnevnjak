package com.example.rafdnevnjakproject.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rafdnevnjakproject.models.CalendarDate;
import com.example.rafdnevnjakproject.models.DateActivity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DateActivityRecyclerViewModel extends ViewModel {

    private final MutableLiveData<List<DateActivity>> dateActivities=new MutableLiveData<>();

    private List<DateActivity> dateActivityList=new ArrayList<>();

    public DateActivityRecyclerViewModel() {

    }

    public MutableLiveData<List<DateActivity>> getDateActivities() {
        return dateActivities;
    }

    public void removeOldActivities(boolean isChecked)
    {
        if(dateActivityList.isEmpty())
            return;
        List<DateActivity> newList= new ArrayList<>();;
        if(isChecked) {
            for (DateActivity da : dateActivityList) {
                Date currentDate = new Date();
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.setTime(currentDate);
                Calendar dateActivityCalendar = Calendar.getInstance();
                dateActivityCalendar.setTime(da.getDate());

                int currentYear = currentCalendar.get(Calendar.YEAR);
                int currentMonth = currentCalendar.get(Calendar.MONTH);
                int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

                int otherYear = dateActivityCalendar.get(Calendar.YEAR);
                int otherMonth = dateActivityCalendar.get(Calendar.MONTH);
                int otherDay = dateActivityCalendar.get(Calendar.DAY_OF_MONTH);

                if ((currentYear < otherYear || (currentYear == otherYear && currentMonth < otherMonth)
                        || (currentYear == otherYear && currentMonth == otherMonth && currentDay < otherDay))) {
                    newList.add(da);
                } else if (currentYear == otherYear && currentMonth == otherMonth && currentDay == otherDay) {
                    if (da.getTimeFrom().compareTo(LocalTime.now()) > 0) {
                        newList.add(da);
                    }
                }
            }

        }
        else
        {
            newList.addAll(dateActivityList);
        }
        dateActivities.setValue(newList);
    }

    public void filterActivities(String filter)
    {
        List<DateActivity> filteredList = dateActivityList.stream().filter(c -> c.getTitle().toLowerCase().startsWith(filter.toLowerCase())).collect(Collectors.toList());
        dateActivities.setValue(filteredList);
    }

    public void priorityFilterActivities(Integer i)
    {
        List<DateActivity> filteredList = dateActivityList.stream().filter(c -> c.getPriority().equals(i)).collect(Collectors.toList());
        dateActivities.setValue(filteredList);
    }

    public void storeDateActivities(List<DateActivity> dal)
    {
        dateActivityList.clear();
        dateActivityList.addAll(dal);
        dateActivities.setValue(dal);
    }
    public void addDateActivity(DateActivity dateActivity) {
        ArrayList<DateActivity> list=new ArrayList<>(dateActivities.getValue());
        list.add(dateActivity);
        dateActivityList.add(dateActivity);
        dateActivities.setValue(list);
    }

    public void removeDateActivity(int index)
    {
        dateActivityList.remove(index);
        dateActivities.setValue(dateActivityList);
    }

    public List<DateActivity> getDateActivityList() {
        return dateActivityList;
    }

    public void setDateActivityList(List<DateActivity> dateActivityList) {
        this.dateActivityList = dateActivityList;
    }
}
