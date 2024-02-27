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

import javax.xml.validation.Validator;

import timber.log.Timber;

public class RecyclerViewModel extends ViewModel {

    private final MutableLiveData<List<CalendarDate>> dates=new MutableLiveData<>();
    private ArrayList<CalendarDate> dateList = new ArrayList<>();

    public RecyclerViewModel() {
        Timber.plant(new Timber.DebugTree());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.JANUARY, 6);
        Date startDate = calendar.getTime();

        calendar.set(2025, Calendar.JANUARY, 1);
        Date endDate = calendar.getTime();

        calendar.setTime(startDate);


        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2023, Calendar.APRIL, 15);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2023, Calendar.APRIL, 17);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(2023, Calendar.APRIL, 12);
        Calendar calendar4 = Calendar.getInstance();
        calendar4.set(2023, Calendar.APRIL, 6);
        Calendar calendar5 = Calendar.getInstance();
        calendar5.set(2023, Calendar.APRIL, 26);
        Calendar calendar6 = Calendar.getInstance();
        calendar6.set(2023, Calendar.APRIL, 1);
        Calendar calendar7 = Calendar.getInstance();
        calendar7.set(2023, Calendar.APRIL, 21);
        Calendar calendar8 = Calendar.getInstance();
        calendar7.set(2023, Calendar.APRIL, 16);


        while (calendar.getTime().before(endDate)) {
            Date result = calendar.getTime();
            if(poredjenje(calendar1,calendar) || poredjenje(calendar4,calendar) || poredjenje(calendar7,calendar)||poredjenje(calendar8,calendar))
            {
                CalendarDate calendarDate=new CalendarDate(result);
                List<DateActivity> list1=new ArrayList<>();
                list1.add(new DateActivity(LocalTime.of(0,0),LocalTime.of(1,0),"Dentist2",3,result));
                list1.add(new DateActivity(LocalTime.of(16,0),LocalTime.of(17,0),"Dentist3",2,result));
                list1.add(new DateActivity(LocalTime.of(13,0),LocalTime.of(14,0),"Dentist1",1,result));
                calendarDate.setListOfActivities(list1);
                dateList.add(calendarDate);
            }
            else if(poredjenje(calendar2,calendar) || poredjenje(calendar5,calendar))
            {
                CalendarDate calendarDate=new CalendarDate(result);
                List<DateActivity> list2=new ArrayList<>();
                list2.add(new DateActivity(LocalTime.of(13,0),LocalTime.of(14,0),"Dentist1",2,result));
                list2.add(new DateActivity(LocalTime.of(16,0),LocalTime.of(17,0),"Dentist3",2,result));
                list2.add(new DateActivity(LocalTime.of(15,0),LocalTime.of(16,0),"Dentist2",3,result));
                calendarDate.setListOfActivities(list2);
                dateList.add(calendarDate);
            }
            else if(poredjenje(calendar3,calendar) || poredjenje(calendar6,calendar))
            {
                CalendarDate calendarDate=new CalendarDate(result);
                List<DateActivity> list3=new ArrayList<>();
                list3.add(new DateActivity(LocalTime.of(16,0),LocalTime.of(17,0),"Dentist3",3,result));
                list3.add(new DateActivity(LocalTime.of(17,0),LocalTime.of(20,0),"Dentist4",3,result));
                list3.add(new DateActivity(LocalTime.of(18,0),LocalTime.of(20,0),"Dentist5",3,result));
                list3.add(new DateActivity(LocalTime.of(19,0),LocalTime.of(20,0),"Dentist6",3,result));
                list3.add(new DateActivity(LocalTime.of(13,0),LocalTime.of(14,0),"Dentist1",3,result));
                list3.add(new DateActivity(LocalTime.of(15,0),LocalTime.of(16,0),"Dentist2",3,result));
                calendarDate.setListOfActivities(list3);
                dateList.add(calendarDate);
            }
            else
                dateList.add(new CalendarDate(result));
            calendar.add(Calendar.DATE, 1);
        }
        ArrayList<CalendarDate> listToSubmit = new ArrayList<>(dateList);
        dates.setValue(listToSubmit);
    }

    private boolean poredjenje(Calendar cal1, Calendar cal2)
    {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
    public MutableLiveData<List<CalendarDate>> getDates() {
        return dates;
    }
}
