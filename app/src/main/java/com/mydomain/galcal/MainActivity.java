package com.mydomain.galcal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.bottomnavigation.LabelVisibilityMode;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.mydomain.galcal.addEvent.AddEventFragment;
import com.mydomain.galcal.calendar.CalendarFragment;
import com.mydomain.galcal.data.BackgroundImageInfo;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.home.HomeFragment;
import com.mydomain.galcal.settings.SettingsFragment;
import com.mydomain.galcal.week.WeekFragment;
import com.squareup.picasso.Picasso;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements BaseContract.BaseView {


    public boolean firstCreating;
    private String mToken;
    private String mUserName;
    private ArrayList<DayEventData> mEvents;
    private MainPresenter mPresenter;
    private BottomNavigationView mBottomNavigation;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    private CalendarFragment mCalendarFragment;
    private WeekFragment mWeekFragment;
    private AddEventFragment mAddEventFragment;
    private HomeFragment mHomeFragment;
    private SettingsFragment mSettingsFragment;

    private ImageView mImageViewBackground;
    private ProgressBar mProgressBar;
    //private View mTutorial;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstCreating = true;
        mImageViewBackground = (ImageView) findViewById(R.id.imageViewBackground);
        mProgressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Circle();
        mProgressBar.setIndeterminateDrawable(doubleBounce);
        mPresenter = new MainPresenter(this);
        mPresenter.onStart();
        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");
        mUserName = intent.getStringExtra("userName");
        mBottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        mBottomNavigation.setVisibility(View.INVISIBLE);
        mBottomNavigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        mFragmentManager = getSupportFragmentManager();

        mCalendarFragment = new CalendarFragment();
        //mCalendarFragment.setToken(mToken);

        mWeekFragment = new WeekFragment();
        mWeekFragment.setToken(mToken);

        mAddEventFragment = new AddEventFragment();
        mAddEventFragment.setToken(mToken);

        mHomeFragment = new HomeFragment();
        mHomeFragment.setToken(mToken);
        mHomeFragment.setUserName(mUserName);

        mSettingsFragment = new SettingsFragment();
        mSettingsFragment.setToken(mToken);


        //FragmentTransaction transaction = mFragmentManager.beginTransaction();
        //transaction.replace(R.id.main_container, mCalendarFragment).commit();
        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){    // попробовать не пересоздавать фрагмент (new SomeFragment() -> SomeFragment())
                    case R.id.mothCalendarView:

                        //mCalendarFragment.setEvents(mEvents);
                        mFragment = mCalendarFragment;
                        break;
                    case R.id.weekFragment:
                        //mWeekFragment.setEvents(mEvents);
                        mFragment = mWeekFragment;
                        break;
                    case R.id.addEventFragment:
                        mFragment = mAddEventFragment;
                        break;
                    case R.id.homeTabFragment:
                        mFragment = mHomeFragment;
                        break;
                    case R.id.settingsFragment:;
                        mFragment = mSettingsFragment;
                        break;
                }
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, mFragment).commit();
                return true;
            }
        });
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        //Toast.makeText(getApplicationContext(), "HELLO", Toast.LENGTH_SHORT).show();
        int s = 0;
        //mPresenter.fetchBackgroundImageInfo(mToken, date);
        //mTimer.schedule(mMyTimerTask, 1000, 10000);
        if(isNetworkAvailable()) {

            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {

                    mPresenter.fetchBackgroundImageInfo(mToken);

                }
            }, 0, 3, TimeUnit.HOURS);


                //mTimer.schedule(mMyTimerTask, 1000, 14400000);
                //mImageViewBackground.setImageDrawable(getResources().getDrawable(R.drawable.sss));
                fetchEventsForYear();

        }else{
            //Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT).show();
            Dialog dialog = getConnectionDialog("No internet connection");
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
            dialog.show();
        }
    }

    private Dialog getConnectionDialog(String message){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.connection_dialog);
        dialog.setTitle("");
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
        final TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);
        Button buttonOk = (Button) dialog.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }



    public void openHomeTab(){
        mBottomNavigation.setSelectedItemId(R.id.homeTabFragment);
    }

    public void openWeekTab(){
        mBottomNavigation.setSelectedItemId(R.id.weekFragment);
    }


    public void setBackgroundImage(String image){

            Picasso.with(getApplicationContext()) //передаем контекст приложения
                    .load(image)
                    .into(mImageViewBackground);

    }

    public void fetchEventsForYear(){
        mPresenter.fetchEventsForYear(mToken);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void setEventsForYear(ArrayList<DayEventData> events){
        mEvents = events;
        mCalendarFragment.setEvents(mEvents);
        mWeekFragment.setEvents(mEvents);
        if(firstCreating) {
            mBottomNavigation.setVisibility(View.VISIBLE);
            mBottomNavigation.setSelectedItemId(R.id.mothCalendarView);
            firstCreating = false;
            mBottomNavigation.setClickable(true);
          //  mTutorial.bringToFront();
        }
        mProgressBar.setVisibility(View.INVISIBLE);
      //  mTutorial.bringToFront();
    }

    public void updateHomeTab(){
        mHomeFragment.can = false;
        mHomeFragment.homeTabData = null;
    }

    public void setBackgroundInfo(BackgroundImageInfo info){
        mHomeFragment.setBackgroundImageInfo(info);
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
