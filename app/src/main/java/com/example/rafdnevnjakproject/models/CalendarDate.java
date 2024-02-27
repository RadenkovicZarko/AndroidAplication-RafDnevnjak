package com.example.rafdnevnjakproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CalendarDate implements Parcelable {
    protected Date date;
    protected List<DateActivity> listOfActivities;

    public CalendarDate(Date date) {
        this.date = date;
        listOfActivities=new ArrayList<>();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<DateActivity> getListOfActivities() {
        return listOfActivities;
    }

    public void setListOfActivities(List<DateActivity> listOfActivities) {
        this.listOfActivities = listOfActivities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarDate that = (CalendarDate) o;
        return Objects.equals(date, that.date) && Objects.equals(listOfActivities, that.listOfActivities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, listOfActivities);
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        String dateString = format.format(this.date);
        return  dateString;
    }



    //Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date.getTime());
        dest.writeTypedList(listOfActivities);
    }

    protected  CalendarDate(Parcel in) {
        date = new Date(in.readLong());
            listOfActivities = in.createTypedArrayList(DateActivity.CREATOR);
    }

    public static final Creator<CalendarDate> CREATOR = new Creator<CalendarDate>() {
        @Override
        public CalendarDate createFromParcel(Parcel in) {
            return new CalendarDate(in);
        }

        @Override
        public CalendarDate[] newArray(int size) {
            return new CalendarDate[size];
        }
    };

}
