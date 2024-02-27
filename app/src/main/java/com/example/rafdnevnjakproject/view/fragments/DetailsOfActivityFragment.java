package com.example.rafdnevnjakproject.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.rafdnevnjakproject.R;
import com.example.rafdnevnjakproject.models.CalendarDate;
import com.example.rafdnevnjakproject.models.DateActivity;
import com.example.rafdnevnjakproject.view.activities.AddActivitiesActivity;
import com.example.rafdnevnjakproject.view.activities.DetailViewActivity;
import com.example.rafdnevnjakproject.view.activities.EditActivity;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.List;

import timber.log.Timber;

public class DetailsOfActivityFragment extends Fragment {
    private DateActivity dateActivity;
    private CalendarDate calendarDate;
    private int index;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvDescription;
    private Button btnEdit;
    private Button btnDelete;


    ActivityResultLauncher<Intent> editActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    CalendarDate object = data.getParcelableExtra(DailyPlanFragment.EDIT_ACTIVITY_RECEIVED_MESSAGE);
//                    dateActivityRecyclerViewModel.storeDateActivities(object.getListOfActivities());
//                    List<CalendarDate> list=sharedViewModel.getCalendarDateLiveData().getValue();
//                    list.get(sharedViewModel.getSelected().getValue()).setListOfActivities(object.getListOfActivities());
//                    sharedViewModel.storeCalendarDate(list);
                    ((DetailViewActivity)getActivity()).setIndex(index);
                    ((DetailViewActivity)getActivity()).storeNewCalendarDate(object);
                }
                else if(result.getResultCode() != Activity.RESULT_CANCELED){
                    Timber.e("Not good");
                }
            });
    public DetailsOfActivityFragment(CalendarDate calendarDate,int index){
        super(R.layout.fragment_details_of_activity);
        this.calendarDate=calendarDate;
        this.index=index;
        this.dateActivity=calendarDate.getListOfActivities().get(index);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        initView(view);
        initListeners(view);
    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTime=view.findViewById(R.id.tvTime);
        tvDescription=view.findViewById(R.id.tvDescription);
        btnDelete=view.findViewById(R.id.btnDeleteDetails);
        btnEdit=view.findViewById(R.id.btnEditDetails);
        tvTitle.setText(dateActivity.getTitle());
        tvTime.setText(dateActivity.getTimeFrom().toString()+" "+dateActivity.getTimeTo().toString());
        tvDescription.setText(dateActivity.getDescription());

    }

    private void initListeners(View view) {
//        view.findViewById(R.id.storeBtn).setOnClickListener(v -> {
//            String userInput = ((EditText)view.findViewById(R.id.inputEt)).getText().toString().trim();
//            sharedViewModel.storeUserInput(userInput);
//        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), EditActivity.class);
                intent.putExtra(EditActivity.EDIT_ACTIVITY_SEND_DATA,calendarDate);
                intent.putExtra(EditActivity.EDIT_ACTIVITY_INDEX_SEND_DATA,index);
                editActivityResultLauncher.launch(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, "Do you really want to delete this activity?", Snackbar.LENGTH_LONG);
                snackbar.setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        calendarDate.getListOfActivities().remove(index);
                        intent.putExtra(DailyPlanFragment.EDIT_ACTIVITY_RECEIVED_MESSAGE,calendarDate);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                    }
                });
                snackbar.show();
            }
        });

    }


    public void setData(String title,String time,String desc)
    {
        tvTitle.setText(title);
        tvTime.setText(time);
        tvDescription.setText(desc);
    }
}
