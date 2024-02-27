package com.example.rafdnevnjakproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rafdnevnjakproject.models.CalendarDate;

import java.util.List;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<List<CalendarDate>> calendarDateLiveData = new MutableLiveData<List<CalendarDate>>();
    private final MutableLiveData<Integer> selected = new MutableLiveData<>();
    public MutableLiveData<List<CalendarDate>> getCalendarDateLiveData() {
        return calendarDateLiveData;
    }

    public void storeCalendarDate(List<CalendarDate> calendarDateList){
        calendarDateLiveData.setValue(calendarDateList);
    }

    public MutableLiveData<Integer> getSelected() {
        return selected;
    }

    public void storeSelectedDate(Integer selected)
    {
        this.selected.setValue(selected);
    }
}
