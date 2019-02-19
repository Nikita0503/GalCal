package com.mydomain.galcal.calendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.R;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.editEvent.EditEventPresenter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nikita on 17.01.2019.
 */

public class CalendarFragment extends Fragment implements BaseContract.BaseView {

    private float ALPHA = 0.3f;
    //private String mToken;
    //private CalendarPresenter mPresenter;
    private ArrayList<DayEventData> mList;
    private CalendarAdapter mAdapter;
    private TextView mTextView;
    private TextView mTextViewStep1;
    private TextView mTextViewWelcome;
    private MaterialCalendarView mCalendarView;
    private RecyclerView mRecyclerView;
    private ImageView mImageViewArrow;
    private ImageView mImageViewGirl;
    private ConstraintLayout mLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPresenter = new CalendarPresenter(this);
        //mPresenter.onStart();
    }

    private void showStep1(){
        mLayout.setBackgroundColor(getResources().getColor(R.color.tutorialColor));
        mTextView.setAlpha(ALPHA);
        mCalendarView.setAlpha(ALPHA);
        mRecyclerView.setAlpha(ALPHA);
        mTextViewWelcome.setVisibility(View.VISIBLE);
        mTextViewStep1.setVisibility(View.VISIBLE);
        mImageViewGirl.setVisibility(View.VISIBLE);
        mImageViewArrow.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.arrowtrans);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(100);
        mImageViewArrow.startAnimation(animation);
    }

    public void hideStep1(){ //вернуть цвет
        mTextView.setAlpha(1);
        mCalendarView.setAlpha(1);
        mRecyclerView.setAlpha(1);
        mTextViewWelcome.setVisibility(View.GONE);
        mTextViewStep1.setVisibility(View.GONE);
        mImageViewGirl.setVisibility(View.GONE);
        mImageViewArrow.setVisibility(View.GONE);
        mImageViewArrow.clearAnimation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);
        mTextViewStep1 = (TextView) view.findViewById(R.id.textViewStep1);
        mTextViewWelcome = (TextView) view.findViewById(R.id.textViewWelcomeStep1);
        mImageViewArrow = (ImageView) view.findViewById(R.id.imageViewStep1);
        mImageViewGirl = (ImageView) view.findViewById(R.id.imageViewGirlStep1);
        mLayout = (ConstraintLayout) view.findViewById(R.id.layout);
        mTextView = (TextView) view.findViewById(R.id.textView3);
        mCalendarView = (MaterialCalendarView) view.findViewById(R.id.materialCalendarView);
        mCalendarView.setWeekDayLabels(new String[]{"M", "T", "W", "T", "F", "S", "S"});
        mCalendarView.setHeaderTextAppearance(R.style.TitleTextAppearance);
        mCalendarView.setWeekDayTextAppearance(R.style.WeekAppearance);
        mCalendarView.setDateTextAppearance(R.style.DayAppearance);
        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                try {
                    ArrayList<DayEventData> list = new ArrayList<DayEventData>();
                    String currentDate = calendarDay.getYear() + "-" + calendarDay.getMonth() + "-" + calendarDay.getDay();
                    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
                    Date dateStart;
                    Date dateEnd;
                    String dateStrStart;
                    String dateStrEnd;
                    for (int i = 0; i < mList.size(); i++) {
                        if(mList.get(i).type.equals("holidays")) {
                            dateStart = oldFormat.parse(mList.get(i).startTime);
                            dateStrStart = dateFormat.format(dateStart);
                            if (dateStrStart.equals(currentDate)) {
                                DayEventData dayEventData = mList.get(i);
                                //dayEventData.todayData = dayEventData.startTime;
                                list.add(dayEventData);
                            }
                        }else{
                            dateStart = oldFormat.parse(mList.get(i).startTime);
                            dateStrStart = dateFormat.format(dateStart);
                            dateEnd = oldFormat.parse(mList.get(i).endTime);
                            dateStrEnd = dateFormat.format(dateEnd);
                            while (!dateStrStart.equals(dateStrEnd)){
                                if (dateStrStart.equals(currentDate)) {
                                    DayEventData dayEventData = mList.get(i);
                                    //dayEventData.todayData = dayEventData.startTime;
                                    list.add(dayEventData);
                                }
                                dateStart.setTime(dateStart.getTime()+86400000);
                                dateStrStart = dateFormat.format(dateStart);
                            }
                            if (dateStrStart.equals(currentDate)) {
                                DayEventData dayEventData = mList.get(i);
                                dayEventData.todayData = dayEventData.startTime;
                                list.add(dayEventData);
                            }
                        }
                    }
                    if(list.size()==0){
                        mTextView.setVisibility(View.VISIBLE);
                        mTextView.setText("No events this day");
                    }else{
                        mTextView.setVisibility(View.INVISIBLE);
                    }
                    mAdapter.setEvents(list);
                }catch (Exception c){
                    c.printStackTrace();
                }
            }
        });
        EventDayDecorator dayDecorator = new EventDayDecorator(getContext(), mList);
        HolidayDecorator holidayDecorator = new HolidayDecorator(getContext(), mList);
        mCalendarView.addDecorator(dayDecorator);
        mCalendarView.addDecorator(holidayDecorator);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewCalendar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CalendarAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        //mPresenter.setToken(mToken);
        Log.d("TAG", "onCreateView");
        //mPresenter.fetchEvents();
        mRecyclerView.bringToFront();
        showStep1();
        return view;
    }


    public void setEvents(ArrayList<DayEventData> list){
        mList = list;

    }

    /*public void setToken(String token){
        mToken = token;
        Log.d("TAG", "setToken()");
        //Log.d("TAG3", mToken);
    }*/

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //mPresenter.onStop();
    }
}
