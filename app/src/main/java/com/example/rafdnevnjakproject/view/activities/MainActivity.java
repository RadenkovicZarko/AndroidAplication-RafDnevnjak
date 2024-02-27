package com.example.rafdnevnjakproject.view.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;


import com.example.rafdnevnjakproject.models.CalendarDate;
import com.example.rafdnevnjakproject.view.fragments.CalendarFragment;
import com.example.rafdnevnjakproject.view.viewpager.PagerAdapter;
import com.example.rafdnevnjakproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_home);
//        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitleActionBar)).setText("USPE0");
        init();
    }

    private void init() {
        initViewPager();
        initNavigation();
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        findViewById(R.id.navigation_1).setAlpha(1); findViewById(R.id.navigation_2).setAlpha(0.3f); findViewById(R.id.navigation_3).setAlpha(0.3f);
    }

    private void initNavigation() {

        ((BottomNavigationView)findViewById(R.id.bottomNavigation)).setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                // setCurrentItem metoda viewPager samo obavesti koji je Item trenutno aktivan i onda metoda getItem u adapteru setuje odredjeni fragment za tu poziciju
                case R.id.navigation_1: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_1, false); break;
                case R.id.navigation_2: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_2, false); break;
                case R.id.navigation_3: ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitleActionBar)).setText("Profile"); viewPager.setCurrentItem(PagerAdapter.FRAGMENT_3, false); break;
            }
            switch (item.getItemId()) {
                // setCurrentItem metoda viewPager samo obavesti koji je Item trenutno aktivan i onda metoda getItem u adapteru setuje odredjeni fragment za tu poziciju
                case R.id.navigation_1: findViewById(R.id.navigation_1).setAlpha(1); findViewById(R.id.navigation_2).setAlpha(0.3f); findViewById(R.id.navigation_3).setAlpha(0.3f); break;
                case R.id.navigation_2: findViewById(R.id.navigation_2).setAlpha(1); findViewById(R.id.navigation_1).setAlpha(0.3f); findViewById(R.id.navigation_3).setAlpha(0.3f); break;
                case R.id.navigation_3: findViewById(R.id.navigation_3).setAlpha(1); findViewById(R.id.navigation_1).setAlpha(0.3f); findViewById(R.id.navigation_2).setAlpha(0.3f); break;
            }
            return true;

        });
        ((BottomNavigationView)findViewById(R.id.bottomNavigation)).setItemIconTintList(ColorStateList.valueOf(Color.BLACK));
        ((BottomNavigationView)findViewById(R.id.bottomNavigation)).setItemTextColor(ColorStateList.valueOf(Color.BLACK));

    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }
}