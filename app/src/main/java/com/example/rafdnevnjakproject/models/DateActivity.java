package com.example.rafdnevnjakproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalTime;
import java.util.Date;

public class DateActivity implements Comparable<DateActivity>, Parcelable {
    private LocalTime timeFrom;
    private LocalTime timeTo;
    private String title;
    private Integer priority;
    private Date date;
    private String description;

    public DateActivity() {
    }

    public DateActivity(LocalTime timeFrom, LocalTime timeTo, String title, Integer priority, Date date) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.title = title;
        this.priority = priority;
        this.date = date;
        this.description="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sagittis dapibus dui, nec venenatis enim euismod a.";
    }

    public DateActivity(LocalTime timeFrom, LocalTime timeTo, String title, Integer priority, Date date, String description) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.title = title;
        this.priority = priority;
        this.date = date;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LocalTime getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(LocalTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public LocalTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(LocalTime timeTo) {
        this.timeTo = timeTo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(DateActivity dateActivity) {
        return this.timeFrom.compareTo(dateActivity.getTimeFrom());
    }

    @Override
    public String toString() {
        return "DateActivity{" +
                "title='" + title + '\'' +
                ", priority=" + priority +
                ", date=" + date +
                '}';
    }
    //Parcable deo

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(timeFrom.toNanoOfDay());
        dest.writeLong(timeTo.toNanoOfDay());
        dest.writeString(title);
        dest.writeInt(priority);
        dest.writeLong(date.getTime());
        dest.writeString(description);
    }

    protected DateActivity(Parcel in) {
        timeFrom=LocalTime.ofNanoOfDay(in.readLong());
        timeTo=LocalTime.ofNanoOfDay(in.readLong());
        title=in.readString();
        priority=in.readInt();
        date = new Date(in.readLong());
        description=in.readString();
    }

    public static final Creator<DateActivity> CREATOR = new Creator<DateActivity>() {
        @Override
        public DateActivity createFromParcel(Parcel in) {
            return new DateActivity(in);
        }

        @Override
        public DateActivity[] newArray(int size) {
            return new DateActivity[size];
        }
    };



}
