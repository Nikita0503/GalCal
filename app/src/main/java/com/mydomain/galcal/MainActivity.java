package com.mydomain.galcal;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mydomain.galcal.addEvent.AddEventFragment;
import com.mydomain.galcal.calendar.CalendarFragment;
import com.mydomain.galcal.home.HomeFragment;
import com.mydomain.galcal.settings.SettingsFragment;
import com.mydomain.galcal.week.WeekFragment;

public class MainActivity extends AppCompatActivity {

    private String mToken;
    private BottomNavigationView mBottomNavigation;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");
        mBottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        mFragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, new CalendarFragment()).commit();
        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){    // попробовать не пересоздавать фрагмент (new SomeFragment() -> SomeFragment())
                    case R.id.mothCalendarView:
                        mFragment = new CalendarFragment();
                        break;
                    case R.id.weekFragment:
                        mFragment = new WeekFragment();
                        break;
                    case R.id.addEventFragment:
                        mFragment = new AddEventFragment();
                        break;
                    case R.id.homeTabFragment:
                        mFragment = new HomeFragment();
                        break;
                    case R.id.settingsFragment:
                        SettingsFragment fragment = new SettingsFragment();
                        fragment.setToken(mToken);
                        mFragment = fragment;
                        break;
                }
                final FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, mFragment).commit();
                return true;
            }
        });
    }


}
