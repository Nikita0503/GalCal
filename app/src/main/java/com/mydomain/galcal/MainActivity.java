package com.mydomain.galcal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mydomain.galcal.addEvent.AddEventFragment;
import com.mydomain.galcal.calendar.CalendarFragment;
import com.mydomain.galcal.data.BackgroundImageInfo;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.data.new_data.Day;
import com.mydomain.galcal.editEvent.EditEventFragment;
import com.mydomain.galcal.home.HomeFragment;
import com.mydomain.galcal.settings.SettingsFragment;
import com.mydomain.galcal.week.WeekFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements BaseContract.BaseView {

    private String mFirstLoginTime;
    public CalendarDay tutorialDay;
    public boolean tutorialGoTo1;
    public boolean tutorialGoTo2;
    private boolean downloaded;
    public boolean firstCreating;
    public boolean showSteps;
    public boolean isTutirial;
    private String mToken;
    private String mUserName;
    private ArrayList<DayEventData> mEvents;
    private ArrayList<Day> mDays;
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


    private ImageView mUnlockedPhoto;
    private TextView mTextViewThatsIt;
    private TextView mTextViewExit;

    private SharedPreferences mPref;
    private EditEventFragment mEditFragment;
    private FirebaseAnalytics mFBanalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFBanalytics = FirebaseAnalytics.getInstance(this);
        //DateFormat df = DateFormat.getTimeInstance();
        //df.setTimeZone(TimeZone.getTimeZone("gmt"));
        //String gmtTime = df.format(new Date());
        //Log.d("TIME", gmtTime);
        //TimeZone timeZone = TimeZone.getDefault();
        //Log.d("TIME", (timeZone.getRawOffset()/3600000)+"");
        tutorialDay = null;
        tutorialGoTo1 = false;
        tutorialGoTo2 = false;
        downloaded = false;
        mPref = getSharedPreferences("GalCal", MODE_PRIVATE);

        String tutorial = mPref.getString("tutorial", "");
        String userName = mPref.getString("userName", "no");
        mFirstLoginTime = mPref.getString("firstLoginTime", null);
        if(mFirstLoginTime!=null){
            Log.d("FIRSTLOGINTIME", mFirstLoginTime);
        }else{
            Log.d("FIRSTLOGINTIME", "is null");
        }

        Bundle params = new Bundle();
        params.putString("user", userName);
        mFBanalytics.logEvent(userName, params);
        if(!tutorial.equals("")){
            isTutirial = false;

        }else{
            //isTutirial = true;
            isTutirial = false;
        }

        mUnlockedPhoto = (ImageView) findViewById(R.id.imageViewGirl);
        mUnlockedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    tutorialGoTo2 = false;
                    mTextViewExit.setVisibility(View.GONE);
                    mUnlockedPhoto.setVisibility(View.GONE);
                    mTextViewThatsIt.setVisibility(View.GONE);
                    mEditFragment.setBackground();
                    //mCalendarFragment.showCalendar();
                    updateCalendarTab();
                    finishTutorial();
            }
        });
        mTextViewThatsIt = (TextView) findViewById(R.id.textViewT);
        mTextViewExit = (TextView) findViewById(R.id.textViewExit);
        showSteps = false;
        firstCreating = true;

        mImageViewBackground = (ImageView) findViewById(R.id.imageViewBackground);
        //mProgressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Circle();
        //mProgressBar.setIndeterminateDrawable(doubleBounce);
        mPresenter = new MainPresenter(this);
        mPresenter.onStart();
        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");
        mUserName = intent.getStringExtra("userName");
        //mFirstLoginTime = intent.getStringExtra("firstLoginTime");
        mBottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        mBottomNavigation.setVisibility(View.INVISIBLE);
        mBottomNavigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        if (isTutirial) {




            mBottomNavigation.getMenu().getItem(0).setIcon(null);
            mBottomNavigation.getMenu().getItem(1).setIcon(null);
            mBottomNavigation.getMenu().getItem(3).setIcon(null);
            mBottomNavigation.getMenu().getItem(4).setIcon(null);
           // mBottomNavigation.getMenu().getItem(0).setVisible(false);
           // mBottomNavigation.getMenu().getItem(1).setVisible(false);
           // mBottomNavigation.getMenu().getItem(3).setVisible(false);
           // mBottomNavigation.getMenu().getItem(4).setVisible(false);
        }
        mFragmentManager = getSupportFragmentManager();

        mCalendarFragment = new CalendarFragment();
        //mCalendarFragment.setToken(mToken);
        mCalendarFragment.setToken(mToken);
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
                Bundle params = new Bundle();
                params.putString("event_type", "click");
                int id = item.getItemId();
                switch (id){    // попробовать не пересоздавать фрагмент (new SomeFragment() -> SomeFragment())
                    case R.id.mothCalendarView:
                        if(showSteps){
                            if(isTutirial) {
                                mAddEventFragment.hideStep7();
                                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                                transaction.replace(R.id.main_container, mFragment).commit();
                                mCalendarFragment = new CalendarFragment();
                                mCalendarFragment.setToken(mToken);
                                mCalendarFragment.setEvents(mEvents);
                            }
                        }
                        //mCalendarFragment.setEvents(mEvents);
                        mFragment = mCalendarFragment;
                        mFBanalytics.logEvent("month_tab", params);
                        break;
                    case R.id.weekFragment:
                        //mWeekFragment.setEvents(mEvents);
                        mFragment = mWeekFragment;
                        mFBanalytics.logEvent("week_tab", params);
                        break;
                    case R.id.addEventFragment:
                        mFragment = mAddEventFragment;
                        mFBanalytics.logEvent("add_event_tab", params);
                        break;
                    case R.id.homeTabFragment:
                        mFragment = mHomeFragment;
                        mFBanalytics.logEvent("home_tab", params);
                        break;
                    case R.id.settingsFragment:;
                        mFragment = mSettingsFragment;
                        mFBanalytics.logEvent("settings_tab", params);
                        break;
                }
                if(!tutorialGoTo1 && !tutorialGoTo2) {
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.main_container, mFragment).commit();
                    transaction.addToBackStack(null);
                }
                if(tutorialGoTo1){
                    mFragment = mAddEventFragment;
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.main_container, mFragment).commit();
                    transaction.addToBackStack(null);
                }
                if(tutorialGoTo2){
                    mFragment = mCalendarFragment;
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.main_container, mFragment).commit();
                    transaction.addToBackStack(null);
                }
                return true;

            }
        });
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        int s = 0;


        if(isNetworkAvailable()) {
            if(!isTutirial) {
                Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        if(isNewbie()) {
                            mPresenter.fetchNewbieBackgroundImageInfo(mToken);
                            //mPresenter.fetchBackgroundImageInfo(mToken);
                        }else{
                            mPresenter.fetchBackgroundImageInfo(mToken);
                        }
                    }
                }, 0, 3, TimeUnit.HOURS);

            }
                //mTimer.schedule(mMyTimerTask, 1000, 14400000);
                //mImageViewBackground.setImageDrawable(getResources().getDrawable(R.drawable.sss));
                fetchEventsForYear();
                //mPresenter.fetchEventsForYearNew(mToken);
        }else{
            //Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT).show();
            Dialog dialog = getConnectionDialog("No internet connection");

            dialog.show();
        }
    }



    private boolean isNewbie(){
        Date dateNow = new Date();
        //dateNow.setTime(dateNow.getTime()+3600000*25);
        dateNow.setTime(dateNow.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        //Log.d("FIRSTTIMELOGIN", dateFormat.getTimeZone().getRawOffset()+"");
        Log.d("FIRSTTIMELOGINnow", dateFormat.format(dateNow));
        //mFirstLoginTime = "2019-05-23T10:41:18Z";
        if(mFirstLoginTime==null){
            Log.d("FIRSTTIMELOGIN", "No newbie without checking");
            return false;
        }
        try {
            //Date dateNow = new Date();
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
            //Date firstLoginTimeDate = dateFormat.parse(mFirstLoginTime);
            Date firstLoginTimeDate = dateFormat.parse(mFirstLoginTime);
            long milliseconds = dateNow.getTime() - firstLoginTimeDate.getTime();
            int hours = (int) (milliseconds / (60 * 60 * 1000));
            Log.d("FIRSTTIMELOGINfirst", dateFormat.format(firstLoginTimeDate));
            Log.d("FIRSTTIMELOGINdiff", String.valueOf(hours));
            if(Math.abs(hours)>=24){
                Log.d("FIRSTTIMELOGIN", "No newbie");
                return false;
            }else{
                Log.d("FIRSTTIMELOGIN", "Newbie");
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    private Dialog getConnectionDialog(String message){
        final Dialog dialog = new Dialog(MainActivity.this, R.style.DialogTheme);
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

    public void showStep10(){
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(isNewbie()) {
                    //mPresenter.fetchBackgroundImageInfo(mToken);
                    mPresenter.fetchNewbieBackgroundImageInfo(mToken);
                }else{
                    mPresenter.fetchBackgroundImageInfo(mToken);
                }
            }
        }, 0, 3, TimeUnit.HOURS);
    }

    public void showAddEventElements(){
        mAddEventFragment.showAll();
    }

    public void openHomeTab(){
        mBottomNavigation.setSelectedItemId(R.id.homeTabFragment);
    }

    public void openWeekTab(){
        mBottomNavigation.setSelectedItemId(R.id.mothCalendarView);
    }

    public void updateCalendarTab(){
        tutorialDay = null;
        mCalendarFragment = new CalendarFragment();
        mCalendarFragment.setToken(mToken);
        mCalendarFragment.setEvents(mEvents);
        mFragment = mCalendarFragment;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, mFragment).commit();
    }

    private void finishTutorial(){
        isTutirial = false;
        showItem2();
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("tutorial", "+");
        editor.commit();
    }

    public void showItem1(){
        mBottomNavigation.getMenu().getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_month_tab));
        mBottomNavigation.getMenu().getItem(2).setIcon(null);
    }

    public void showItem2(){
        mBottomNavigation.getMenu().getItem(2).setIcon(getResources().getDrawable(R.drawable.ic_add_event_tab));
        mBottomNavigation.getMenu().getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_week_tab));
        mBottomNavigation.getMenu().getItem(3).setIcon(getResources().getDrawable(R.drawable.ic_home_tab));
        mBottomNavigation.getMenu().getItem(4).setIcon(getResources().getDrawable(R.drawable.ic_settings_tab));
    }

    public void setBackgroundImage(String image){
        //mProgressBar.setVisibility(View.VISIBLE);
            if(isTutirial){
                mUnlockedPhoto.setVisibility(View.VISIBLE);
                Picasso.with(getApplicationContext()) //передаем контекст приложения
                        .load(image)
                        .transform(new UnlockedTransformation())
                        .into(mUnlockedPhoto, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                //mProgressBar.setVisibility(View.INVISIBLE);
                                downloaded = true;
                            }

                            @Override
                            public void onError() {

                            }
                        });
                mTextViewThatsIt.setVisibility(View.VISIBLE);
                mTextViewExit.setVisibility(View.VISIBLE);


            }
            try{
                if(image!=null) {
                    Picasso.with(getApplicationContext()) //передаем контекст приложения
                            .load(image)
                            .into(mImageViewBackground);
                }else{
                    Picasso.with(getApplicationContext())
                            .load(R.drawable.pictureforfirstusers)
                            .into(mImageViewBackground);
                }
            }catch (Exception c){
                c.printStackTrace();
            }


    }

    //public void setDays(ArrayList<Day> days){
    //    mDays = days;
    //    mCalendarFragment.setDays(days);
    //}

    public void fetchEventsForYear(){
        mPresenter.fetchEventsForYear(mToken);
        //mProgressBar.setVisibility(View.VISIBLE);
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
        //mProgressBar.setVisibility(View.INVISIBLE);
      //  mTutorial.bringToFront();
    }

    public void updateHomeTab(){
        mHomeFragment.can = false;
        mHomeFragment.homeTabData = null;
    }

    public void setBackgroundInfo(BackgroundImageInfo info){
        if(info!=null) {
            mHomeFragment.setBackgroundImageInfo(info);
        }
    }

    public void setEditFragment(EditEventFragment editFragment){
        mEditFragment = editFragment;
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
