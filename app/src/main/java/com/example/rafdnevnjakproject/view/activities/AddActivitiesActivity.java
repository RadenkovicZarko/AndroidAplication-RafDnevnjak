package com.example.rafdnevnjakproject.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.rafdnevnjakproject.R;
import com.example.rafdnevnjakproject.models.CalendarDate;
import com.example.rafdnevnjakproject.models.DateActivity;
import com.example.rafdnevnjakproject.view.fragments.DailyPlanFragment;
import com.example.rafdnevnjakproject.view.viewpager.PagerAdapterDetailViewOfActivity;

import java.time.LocalTime;
import java.util.List;

import timber.log.Timber;

public class AddActivitiesActivity extends AppCompatActivity {


    public static final String SEND_DATA_ADD_ACTIVITY="AddActivitySendData";
    private CalendarDate calendarDate;
    private Button lowBtn;
    private Button midBtn;
    private Button highBtn;
    private Button createBtn;
    private Button cancelBtn;
    private EditText edtTitle;
    private TimePicker tpFrom;
    private TimePicker tpTo;
    private EditText edtDescription;
    private int selected=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);
        calendarDate=getIntent().getParcelableExtra(SEND_DATA_ADD_ACTIVITY);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_home);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitleActionBar)).setText(calendarDate.toString());

        Timber.plant(new Timber.DebugTree());
        init();
    }


    private void init() {
        initView();
        initListeners();
    }
    private void initView()
    {
        lowBtn=findViewById(R.id.btnLowAddActivity);
        midBtn=findViewById(R.id.btnMidAddActivity);
        highBtn=findViewById(R.id.btnHighAddActivity);
        createBtn=findViewById(R.id.btnCreateAddActivity);
        cancelBtn=findViewById(R.id.btnCancelAddActivity);
        edtTitle=findViewById(R.id.edtTitle);
        tpFrom=findViewById(R.id.tpFrom);
        tpTo=findViewById(R.id.tpTo);
        tpFrom.setIs24HourView(true);
        tpTo.setIs24HourView(true);
        edtDescription=findViewById(R.id.edtDescription);
    }

    private void initListeners()
    {
        createBtn.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(AddActivitiesActivity.this, "From time must be before to time", Toast.LENGTH_SHORT).show();
                    }
                    else if(inTheSameTime(calendarDate.getListOfActivities(),dateActivity)){
                        calendarDate.getListOfActivities().add(dateActivity);
                        returnIntent.putExtra(DailyPlanFragment.ADD_ACTIVITY_RECEIVED_MESSAGE,calendarDate);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(AddActivitiesActivity.this, "There is activity at that time", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AddActivitiesActivity.this, "You must fill all fields and select priority of your activity", Toast.LENGTH_SHORT).show();
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
        for(DateActivity da:list) {
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
