package com.example.rafdnevnjakproject.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rafdnevnjakproject.R;
import com.example.rafdnevnjakproject.models.CalendarDate;
import com.example.rafdnevnjakproject.models.DateActivity;
import com.example.rafdnevnjakproject.view.activities.AddActivitiesActivity;
import com.example.rafdnevnjakproject.view.activities.DetailViewActivity;
import com.example.rafdnevnjakproject.view.activities.EditActivity;
import com.example.rafdnevnjakproject.view.recycler.adapter.DateActivityAdapter;
import com.example.rafdnevnjakproject.view.recycler.adapter.LauncherCallback;
import com.example.rafdnevnjakproject.view.recycler.differ.DateActivityDiffItemCallback;
import com.example.rafdnevnjakproject.viewmodels.DateActivityRecyclerViewModel;
import com.example.rafdnevnjakproject.viewmodels.SharedViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class DailyPlanFragment extends Fragment implements LauncherCallback {

    public static final String ADD_ACTIVITY_RECEIVED_MESSAGE="AddActivityKey";
    public static final String EDIT_ACTIVITY_RECEIVED_MESSAGE="EditActivityKey";
    private SharedViewModel sharedViewModel;

    private DateActivityAdapter dateActivityAdapter;
    private DateActivityRecyclerViewModel dateActivityRecyclerViewModel;

    private RecyclerView recyclerView;

    private CheckBox checkBox;
    private SearchView searchBar;
    private Button lowBtn;
    private Button midBtn;
    private Button highBtn;

    private ImageButton addBtn;
    private CalendarDate lastCalendarDate;


    ActivityResultLauncher<Intent> addActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    CalendarDate object = data.getParcelableExtra(ADD_ACTIVITY_RECEIVED_MESSAGE);
                    Collections.sort(object.getListOfActivities());
                    dateActivityRecyclerViewModel.storeDateActivities(object.getListOfActivities());
                    List<CalendarDate> list=sharedViewModel.getCalendarDateLiveData().getValue();
                    list.get(sharedViewModel.getSelected().getValue()).setListOfActivities(object.getListOfActivities());
                    sharedViewModel.storeCalendarDate(list);

                }
                else if(result.getResultCode() != Activity.RESULT_CANCELED){
                    Timber.e("Not good");
                }
            });

    ActivityResultLauncher<Intent> detailActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    CalendarDate object = data.getParcelableExtra(EDIT_ACTIVITY_RECEIVED_MESSAGE);
                    Collections.sort(object.getListOfActivities());
                    dateActivityRecyclerViewModel.storeDateActivities(object.getListOfActivities());
                    List<CalendarDate> list=sharedViewModel.getCalendarDateLiveData().getValue();
                    list.get(sharedViewModel.getSelected().getValue()).setListOfActivities(object.getListOfActivities());
                    sharedViewModel.storeCalendarDate(list);
//                    Timber.e("USPELO");
                }
                else if(result.getResultCode() != Activity.RESULT_CANCELED){
                    Timber.e("Not good");
                }
            });






    public DailyPlanFragment() {
        super(R.layout.fragment_daily);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(lastCalendarDate!=null)
        {
            ((TextView)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.tvTitleActionBar)).setText(lastCalendarDate.toString());

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("calendar", lastCalendarDate);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        dateActivityRecyclerViewModel = new ViewModelProvider(getActivity()).get(DateActivityRecyclerViewModel.class);
        if (savedInstanceState != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                lastCalendarDate = savedInstanceState.getParcelable("calendar",CalendarDate.class);
            }
        }

        init(view);
    }


    private void init(View view)
    {
        initView(view);
        initListeners();
        initRecycler();
        initObservers(view);
    }
    private void initView(View view) {

        checkBox=view.findViewById(R.id.my_checkbox);
        searchBar=view.findViewById(R.id.search_view);
        lowBtn=view.findViewById(R.id.lowBtn);
        midBtn=view.findViewById(R.id.midBtn);
        highBtn=view.findViewById(R.id.highBtn);
        recyclerView=view.findViewById(R.id.activityDateRecyclerView);
        addBtn=view.findViewById(R.id.addBtn);
        checkBox.setChecked(true);
    }

    private void initListeners(){
//        addBtn.setOnClickListener(v->{
//            if(sharedViewModel.getSelected().getValue()!=null) {
//                CalendarDate calendarDate=sharedViewModel.getCalendarDateLiveData().getValue().get(sharedViewModel.getSelected().getValue());
//                dateActivityRecyclerViewModel.addDateActivity(new DateActivity(LocalTime.of(13,0),LocalTime.of(14,0),"Homework",1,calendarDate.getDate()));
//                int index = sharedViewModel.getSelected().getValue();
//                List<CalendarDate> list=sharedViewModel.getCalendarDateLiveData().getValue();
//                list.get(index).getListOfActivities().add(new DateActivity(LocalTime.of(13,0),LocalTime.of(14,0),"Homework",1,calendarDate.getDate()));
//                sharedViewModel.storeCalendarDate(list);
//            }
//        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(sharedViewModel.getSelected().getValue()!=null)
                {
                    dateActivityRecyclerViewModel.removeOldActivities(!isChecked);
                }
            }
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                dateActivityRecyclerViewModel.filterActivities(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                dateActivityRecyclerViewModel.filterActivities(s);
                return true;
            }
        });

        lowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateActivityRecyclerViewModel.priorityFilterActivities(3);
            }
        });
        midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateActivityRecyclerViewModel.priorityFilterActivities(2);
            }
        });
        highBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateActivityRecyclerViewModel.priorityFilterActivities(1);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedViewModel.getSelected().getValue()==null )
                    return;
                Intent intent=new Intent(getActivity(), AddActivitiesActivity.class);
                CalendarDate calendarDate=sharedViewModel.getCalendarDateLiveData().getValue().get(sharedViewModel.getSelected().getValue());
                intent.putExtra(AddActivitiesActivity.SEND_DATA_ADD_ACTIVITY,calendarDate);
                addActivityResultLauncher.launch(intent);
            }
        });
    }

    private void initRecycler() {
        dateActivityAdapter=new DateActivityAdapter(new DateActivityDiffItemCallback(), dateActivity -> {
            Intent intent=new Intent(getActivity(), DetailViewActivity.class);
            CalendarDate calendarDate=sharedViewModel.getCalendarDateLiveData().getValue().get(sharedViewModel.getSelected().getValue());
            intent.putExtra(DetailViewActivity.SEND_DATA,calendarDate);
            int index=calendarDate.getListOfActivities().indexOf(dateActivity);
            intent.putExtra(DetailViewActivity.INDEX_SEND_DATA,index);
            detailActivityResultLauncher.launch(intent);
        },this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(dateActivityAdapter);
    }

    private void initObservers(View view) {
        dateActivityRecyclerViewModel.getDateActivities().observe(getActivity(), dateActivities -> {
            dateActivityAdapter.submitList(dateActivities);

        });
        sharedViewModel.getSelected().observe(getViewLifecycleOwner(), (index) -> {
//            getActivity().setTitle(sharedViewModel.getCalendarDateLiveData().getValue().get(index).toString());
            ((TextView)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.tvTitleActionBar)).setText(sharedViewModel.getCalendarDateLiveData().getValue().get(index).toString());
            lastCalendarDate=sharedViewModel.getCalendarDateLiveData().getValue().get(sharedViewModel.getSelected().getValue());
            if( sharedViewModel.getCalendarDateLiveData().getValue()!=null && !sharedViewModel.getCalendarDateLiveData().getValue().isEmpty()){
                Collections.sort(sharedViewModel.getCalendarDateLiveData().getValue().get(sharedViewModel.getSelected().getValue()).getListOfActivities());
                dateActivityRecyclerViewModel.storeDateActivities(sharedViewModel.getCalendarDateLiveData().getValue().get(sharedViewModel.getSelected().getValue()).getListOfActivities());
            }
            checkBox.setChecked(true);
        });
    }


    @Override
    public void editlaunchResult(int position) {
        Intent intent=new Intent(getActivity(), EditActivity.class);
        intent.putExtra(EditActivity.EDIT_ACTIVITY_SEND_DATA,sharedViewModel.getCalendarDateLiveData().getValue().get(sharedViewModel.getSelected().getValue()));
        intent.putExtra(EditActivity.EDIT_ACTIVITY_INDEX_SEND_DATA,position);
        detailActivityResultLauncher.launch(intent);
    }

    @Override
    public void deleteLaunchResult(int position)
    {
        List<CalendarDate> list=sharedViewModel.getCalendarDateLiveData().getValue();
        list.get(sharedViewModel.getSelected().getValue()).getListOfActivities().remove(position);
        List<DateActivity> da=list.get(sharedViewModel.getSelected().getValue()).getListOfActivities();
        dateActivityRecyclerViewModel.storeDateActivities(da);
        sharedViewModel.storeCalendarDate(list);
//        recyclerView.getRecycledViewPool().clear();
        dateActivityAdapter.notifyDataSetChanged();
    }
}