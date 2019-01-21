package com.mydomain.galcal;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mydomain.galcal.addEvent.AddEventFragment;
import com.mydomain.galcal.calendar.CalendarFragment;
import com.mydomain.galcal.data.BackgroundImageInfo;
import com.mydomain.galcal.home.HomeFragment;
import com.mydomain.galcal.settings.SettingsFragment;
import com.mydomain.galcal.week.WeekFragment;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements BaseContract.BaseView {

    private String mToken;
    private String mUserName;
    private MainPresenter mPresenter;
    private BottomNavigationView mBottomNavigation;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    private HomeFragment mHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenter(this);
        mPresenter.onStart();
        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");
        mUserName = intent.getStringExtra("userName");
        mBottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        mFragmentManager = getSupportFragmentManager();

        mHomeFragment = new HomeFragment();
        mHomeFragment.setToken(mToken);
        mHomeFragment.setUserName(mUserName);

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
                        mFragment = mHomeFragment;
                        break;
                    case R.id.settingsFragment:
                        SettingsFragment fragmentSettings = new SettingsFragment();
                        fragmentSettings.setToken(mToken);
                        mFragment = fragmentSettings;
                        break;
                }
                final FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, mFragment).commit();
                return true;
            }
        });
    }



    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }
}
