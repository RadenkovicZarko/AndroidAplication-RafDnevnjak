package com.example.rafdnevnjakproject.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rafdnevnjakproject.R;
import com.example.rafdnevnjakproject.models.CalendarDate;
import com.example.rafdnevnjakproject.models.DateActivity;
import com.example.rafdnevnjakproject.view.fragments.DailyPlanFragment;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class EditActivity extends AppCompatActivity {

    public static final String EDIT_ACTIVITY_SEND_DATA="EditActivitySend";
    public static final String EDIT_ACTIVITY_INDEX_SEND_DATA="EditActivityIndex";
    private CalendarDate calendarDate;
    private int index;

    private Button lowBtn;
    private Button midBtn;
    private Button highBtn;
    private Button saveBtn;
    private Button cancelBtn;
    private EditText edtTitle;
    private TimePicker tpFrom;
    private TimePicker tpTo;
    private EditText edtDescription;
    private int selected=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        calendarDate=getIntent().getParcelableExtra(EDIT_ACTIVITY_SEND_DATA);
        index=getIntent().getIntExtra(EDIT_ACTIVITY_INDEX_SEND_DATA,0);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_home);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitleActionBar)).setText(calendarDate.toString());

        Timber.plant(new Timber.DebugTree());
        init();
    }

    private void init() {
        initView();
        initValues();
        initListeners();
    }
    private void initView()
    {
        lowBtn=findViewById(R.id.btnLowEditActivity);
        midBtn=findViewById(R.id.btnMidEditActivity);
        highBtn=findViewById(R.id.btnHighEditActivity);
        saveBtn =findViewById(R.id.btnSaveEditActivity);
        cancelBtn=findViewById(R.id.btnCancelEditActivity);
        edtTitle=findViewById(R.id.edtTitleEditActivity);
        tpFrom=findViewById(R.id.tpFromEditActivity);
        tpTo=findViewById(R.id.tpToEditActivity);
        tpFrom.setIs24HourView(true);
        tpTo.setIs24HourView(true);
        edtDescription=findViewById(R.id.edtDescriptionEditActivity);
    }
    private void initValues()
    {
        DateActivity da=calendarDate.getListOfActivities().get(index);
        if(da.getPriority()==1)
        {
            selected=1;
            lowBtn.setAlpha(0.3f);
            midBtn.setAlpha(0.3f);
            highBtn.setAlpha(1);
        }
        else if(da.getPriority()==2)
        {
            selected=2;
            lowBtn.setAlpha(0.3f);
            midBtn.setAlpha(1);
            highBtn.setAlpha(0.3f);
        }
        else
        {
            selected=3;
            lowBtn.setAlpha(1);
            midBtn.setAlpha(0.3f);
            highBtn.setAlpha(0.3f);
        }

        edtTitle.setText(da.getTitle());
        edtDescription.setText(da.getDescription());
        tpTo.setIs24HourView(true);
        tpFrom.setHour(da.getTimeFrom().getHour());
        tpFrom.setMinute(da.getTimeFrom().getMinute());
        tpTo.setHour(da.getTimeTo().getHour());
        tpTo.setMinute(da.getTimeTo().getMinute());
    }

    private void initListeners()
    {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selected!=0 && !edtTitle.getText().toString().isEmpty() && tpFrom!=null && tpTo!=null && !edtDescription.getText().toString().isEmpty())
                {
                    Intent returnIntent=new Intent();

                    int tpFromHour = tpFrom.getHour();
                    int tpFromMin = tpFrom.getMinute();
                    int tpToHour = tpTo.getHour();
                    int tpToMin = tpTo.getMinute();

                    LocalTime from=LocalTime.of(tpFromHour,tpFromMin);
                    LocalTime to=LocalTime.of(tpToHour,tpToMin);
                    DateActivity dateActivity=new DateActivity();
                    dateActivity.setDate(calendarDate.getDate());
                    dateActivity.setDescription(edtDescription.getText().toString());
                    dateActivity.setTitle(edtTitle.getText().toString());
                    dateActivity.setPriority(selected);
                    dateActivity.setTimeFrom(from);
                    dateActivity.setTimeTo(to);
                    if(from.compareTo(to)>0)
                    {
                        Toast.makeText(EditActivity.this, "From time must be before to time", Toast.LENGTH_SHORT).show();
                    }
                    else if(inTheSameTime(calendarDate.getListOfActivities(),dateActivity)){
                        List<DateActivity> list=calendarDate.getListOfActivities();
                        list.get(index).setDate(calendarDate.getDate());
                        list.get(index).setDescription(edtDescription.getText().toString());
                        list.get(index).setTitle(edtTitle.getText().toString());
                        list.get(index).setPriority(selected);
                        list.get(index).setTimeFrom(from);
                        list.get(index).setTimeTo(to);
                        calendarDate.setListOfActivities(list);
                        returnIntent.putExtra(DailyPlanFragment.EDIT_ACTIVITY_RECEIVED_MESSAGE,calendarDate);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(EditActivity.this, "There is activity at that time", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(EditActivity.this, "You must fill all fields and select priority of your activity", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent=new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });

        lowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected=3;
                midBtn.setAlpha(0.3f);
                highBtn.setAlpha(0.3f);
                lowBtn.setAlpha(1);
            }
        });
        midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected=2;
                lowBtn.setAlpha(0.3f);
                highBtn.setAlpha(0.3f);
                midBtn.setAlpha(1);
            }
        });

        highBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected=1;
                lowBtn.setAlpha(0.3f);
                midBtn.setAlpha(0.3f);
                highBtn.setAlpha(1);
            }
        });
    }


    private boolean inTheSameTime(List<DateActivity> list, DateActivity dateActivity)
    {
        int i=-1;
        for(DateActivity da:list) {
            i++;
            if(i==index)
                continue;
            if (da.getTimeFrom().compareTo(dateActivity.getTimeFrom()) <= 0 && da.getTimeTo().compareTo(dateActivity.getTimeTo()) <= 0) {
                continue;
            } else if (da.getTimeFrom().compareTo(dateActivity.getTimeFrom()) >= 0 && da.getTimeTo().compareTo(dateActivity.getTimeTo()) >= 0)
            {
                continue;
            }
            else
                return false;
        }
        return true;

    }


}
