package com.mydomain.galcal.addEvent;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.MainActivity;
import com.mydomain.galcal.R;
import com.mydomain.galcal.data.AddEventData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nikita on 17.01.2019.
 */

public class AddEventFragment extends Fragment implements BaseContract.BaseView{

    private float ALPHA = 0.3f;
    private String mToken;
    private AddEventPresenter mPresenter;
    private TextView mTextViewLocation;
    private TextView mTextViewSave;
    private TextView mTextViewStartTime;
    private TextView mTextViewEndTime;
    private TextView mTextViewStartDate;
    private TextView mTextViewEndDate;
    private TextView mTextViewTimeText;
    private TextView mTextViewLocationText;
    private TextView mTextViewReminderText2;
    private TextView mTextViewReminderText;
    private TextView mTextViewNotesText;
    private TextView mTextViewEnterTitleText;
    private TextView mTextViewChooseTime;
    private TextView mTextViewEnterLocationText;
    private TextView mTextViewReminderChoose;
    private TextView mTextViewEnterNotes;
    private TextView mTextViewLetsTo;
    private TextView mTextViewCongrats;
    private EditText mEditTextTitleEvent;
    private EditText mEditTextLocation;
    private EditText mEditTextNotes;
    private Switch mSwitchAllDay;
    private Switch mSwitchReminder;
    private ProgressBar mProgressBar;
    private MaterialCalendarView mCalendarViewFrom;
    private MaterialCalendarView mCalendarViewTo;
    private TimePicker mTimePickerFrom;
    private TimePicker mTimePickerTo;
    private ImageView mDivider;
    private ImageView mImageViewArrow;
    private ImageView mImageViewGirl2;
    private ImageView mImageVIewGirl3;
    private ImageView mImageViewPulsating;
    private ConstraintLayout mLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddEventPresenter(this);
        mPresenter.onStart();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_event_fragment, container, false);
        mLayout = (ConstraintLayout) view.findViewById(R.id.layout);
        mImageViewPulsating = (ImageView) view.findViewById(R.id.imageViewPulsating);
        mImageVIewGirl3 = (ImageView) view.findViewById(R.id.imageViewGirl3);
        mImageViewGirl2 = (ImageView) view.findViewById(R.id.imageViewGirl2);
        mTextViewCongrats = (TextView) view.findViewById(R.id.textViewCongrats);
        mImageViewArrow = (ImageView) view.findViewById(R.id.imageViewStep1);
        mTextViewLetsTo = (TextView) view.findViewById(R.id.textViewLetsTo);
        mTextViewEnterNotes = (TextView) view.findViewById(R.id.textViewEnterNotes);
        mTextViewReminderChoose = (TextView) view.findViewById(R.id.textViewReminderChoose);
        mTextViewEnterLocationText = (TextView) view.findViewById(R.id.textViewEnterLocationText);
        mTextViewChooseTime = (TextView) view.findViewById(R.id.textViewChooseTime);
        mTextViewEnterTitleText = (TextView) view.findViewById(R.id.textViewEnterTitleText);
        mTextViewLocation = (TextView) view.findViewById(R.id.location_tv);
        mTextViewSave = (TextView) view.findViewById(R.id.save);
        mTextViewStartTime = (TextView) view.findViewById(R.id.time_from);
        mTextViewEndTime = (TextView) view.findViewById(R.id.time_to);
        mTextViewStartDate = (TextView) view.findViewById(R.id.date_from);
        mTextViewEndDate = (TextView) view.findViewById(R.id.date_to);
        mTextViewReminderText = (TextView) view.findViewById(R.id.reminder_time_text);
        mTextViewTimeText = (TextView) view.findViewById(R.id.time);
        mTextViewLocationText = (TextView) view.findViewById(R.id.location_tv);
        mTextViewReminderText2 = (TextView) view.findViewById(R.id.reminder_tv);
        mTextViewNotesText = (TextView) view.findViewById(R.id. notes_tv);
        mEditTextTitleEvent = (EditText) view.findViewById(R.id.event1);
        mEditTextTitleEvent.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    MainActivity activity = (MainActivity) getActivity();
                    if(activity.isTutirial) {
                        hideStep2();
                    }

                    return true;
                }
                return false;
            }
        });
        mEditTextLocation = (EditText) view.findViewById(R.id.location);
        mEditTextLocation.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    MainActivity activity = (MainActivity) getActivity();
                    if(activity.isTutirial) {
                        hideStep4();
                    }


                    return true;
                }
                return false;
            }
        });
        mEditTextNotes = (EditText) view.findViewById(R.id.notes_ed);
        mSwitchAllDay = (Switch) view.findViewById(R.id.all_day_switch);
        mSwitchReminder = (Switch) view.findViewById(R.id.reminder_switch);
        mSwitchReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MainActivity activity = (MainActivity) getActivity();
                if(activity.isTutirial) {
                    hideStep5();
                }
            }
        });
        mProgressBar = (ProgressBar)view.findViewById(R.id.spin_kit);
        mDivider = (ImageView) view.findViewById(R.id.imageViewDivider);
        mTimePickerFrom = (TimePicker) view.findViewById(R.id.timePickerFrom);
        mTimePickerFrom.setVisibility(View.GONE);

        mTimePickerTo = (TimePicker) view.findViewById(R.id.timePickerTo);
        mTimePickerTo.setVisibility(View.GONE);

        mCalendarViewFrom = (MaterialCalendarView) view.findViewById(R.id.calendarViewFrom);
        mCalendarViewTo = (MaterialCalendarView) view.findViewById(R.id.calendarViewTo);
        Calendar calendar = Calendar.getInstance();
        mCalendarViewFrom.setDateSelected(CalendarDay.today(), true);
        mCalendarViewFrom.setWeekDayLabels(new String[]{"M", "T", "W", "T", "F", "S", "S"});
        mCalendarViewFrom.setHeaderTextAppearance(R.style.TitleTextAppearance);
        mCalendarViewFrom.setWeekDayTextAppearance(R.style.WeekAppearance);
        mCalendarViewFrom.setDateTextAppearance(R.style.DayAppearance);
        mCalendarViewFrom.setVisibility(View.GONE);
        mCalendarViewTo.setDateSelected(CalendarDay.today(), true);
        mCalendarViewTo.setWeekDayLabels(new String[]{"M", "T", "W", "T", "F", "S", "S"});
        mCalendarViewTo.setHeaderTextAppearance(R.style.TitleTextAppearance);
        mCalendarViewTo.setWeekDayTextAppearance(R.style.WeekAppearance);
        mCalendarViewTo.setDateTextAppearance(R.style.DayAppearance);
        mCalendarViewTo.setVisibility(View.GONE);
        Sprite doubleBounce = new Circle();
        mProgressBar.setIndeterminateDrawable(doubleBounce);
        mCalendarViewFrom.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {

                mCalendarViewFrom.setVisibility(View.GONE);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("d M yyyy", Locale.ENGLISH);
                    Date date = dateFormat.parse(calendarDay.getDay() + " " + calendarDay.getMonth() + " " + calendarDay.getYear());
                    SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                    //Toast.makeText(getContext(), date, Toast.LENGTH_SHORT).show();
                    mTextViewStartDate.setText(newFormat.format(date));
                }catch (Exception c){
                    c.printStackTrace();
                }
                if(mSwitchAllDay.isChecked()){
                    mCalendarViewTo.setVisibility(View.VISIBLE);
                    ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) mTextViewLocation.getLayoutParams();
                    layoutParams1.topToBottom = R.id.calendarViewTo;
                    mTextViewLocation.setLayoutParams(layoutParams1);
                }else {
                    Calendar calendar = Calendar.getInstance();
                    mTextViewStartTime.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
                    mTimePickerFrom.setVisibility(View.VISIBLE);
                    ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) mTextViewEndDate.getLayoutParams();
                    layoutParams1.topToBottom = R.id.timePickerFrom;
                    mTextViewEndDate.setLayoutParams(layoutParams1);


                    //
                }
            }
        });
        mCalendarViewTo.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {

                mCalendarViewTo.setVisibility(View.GONE);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("d M yyyy", Locale.ENGLISH);
                    Date date = dateFormat.parse(calendarDay.getDay() + " " + calendarDay.getMonth() + " " + calendarDay.getYear());
                    SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                    //Toast.makeText(getContext(), date, Toast.LENGTH_SHORT).show();
                    mTextViewEndDate.setText(newFormat.format(date));
                }catch (Exception c){
                    c.printStackTrace();
                }
                if(!mSwitchAllDay.isChecked()){
                    Calendar calendar = Calendar.getInstance();
                    mTextViewEndTime.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
                    mTimePickerTo.setVisibility(View.VISIBLE);
                    ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) mTextViewLocation.getLayoutParams();
                    layoutParams1.topToBottom = R.id.timePickerTo;
                    mTextViewLocation.setLayoutParams(layoutParams1);

                }else{
                    MainActivity activity = (MainActivity) getActivity();
                    if(activity.isTutirial) {
                        hideStep3();
                    }
                }


            }
        });

        mTimePickerFrom.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(selectedMinute<10) {
                    mTextViewStartTime.setText(selectedHour + ":0" + selectedMinute);
                }else{
                    mTextViewStartTime.setText(selectedHour + ":" + selectedMinute);
                }
            }
        });

        mTimePickerTo.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(selectedMinute<10) {
                    mTextViewEndTime.setText(selectedHour + ":0" + selectedMinute);
                }else{
                    mTextViewEndTime.setText(selectedHour + ":" + selectedMinute);
                }

            }
        });

        mTextViewStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewStartTime.setError(null);
                if(mTimePickerFrom.getVisibility()==View.VISIBLE){
                    mTimePickerFrom.setVisibility(View.GONE);
                    ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) mTextViewEndDate.getLayoutParams();
                    layoutParams1.topToBottom = R.id.date_from;
                    mTextViewEndDate.setLayoutParams(layoutParams1);
                }else{
                    Calendar calendar = Calendar.getInstance();
                    if(calendar.get(Calendar.MINUTE)<10){
                        mTextViewStartTime.setText(calendar.get(Calendar.HOUR_OF_DAY)+":0"+calendar.get(Calendar.MINUTE));
                    }else{
                        mTextViewStartTime.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
                    }

                    mTimePickerFrom.setVisibility(View.VISIBLE);
                    mTimePickerTo.setVisibility(View.GONE);
                    mCalendarViewFrom.setVisibility(View.GONE);
                    mCalendarViewTo.setVisibility(View.GONE);
                    ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) mTextViewEndDate.getLayoutParams();
                    layoutParams1.topToBottom = R.id.timePickerFrom;
                    mTextViewEndDate.setLayoutParams(layoutParams1);
                }
                /*Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog,  new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //Toast.makeText(getContext(), selectedHour+":"+selectedMinute, Toast.LENGTH_SHORT).show();
                        mTextViewStartTime.setText(selectedHour+":"+selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();*/
            }
        });
        mTextViewEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewEndTime.setError(null);
                if(mTimePickerTo.getVisibility()==View.VISIBLE){
                    mTimePickerTo.setVisibility(View.GONE);
                    ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) mTextViewLocation.getLayoutParams();
                    layoutParams1.topToBottom = R.id.date_to;
                    mTextViewLocation.setLayoutParams(layoutParams1);
                }else{
                    Calendar calendar = Calendar.getInstance();
                    if(calendar.get(Calendar.MINUTE)<10){
                        mTextViewEndTime.setText(calendar.get(Calendar.HOUR_OF_DAY)+":0"+calendar.get(Calendar.MINUTE));
                    }else{
                        mTextViewEndTime.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
                    }
                    mTimePickerTo.setVisibility(View.VISIBLE);
                    mTimePickerFrom.setVisibility(View.GONE);
                    mCalendarViewFrom.setVisibility(View.GONE);
                    mCalendarViewTo.setVisibility(View.GONE);
                    ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) mTextViewLocation.getLayoutParams();
                    layoutParams1.topToBottom = R.id.timePickerTo;
                    mTextViewLocation.setLayoutParams(layoutParams1);
                }
                if(!mTextViewStartTime.getText().toString().equals("Time")&&!mTextViewStartDate.getText().toString().equals("From")&&!mTextViewEndDate.getText().toString().equals("To")) {
                    MainActivity activity = (MainActivity) getActivity();
                    if(activity.isTutirial) {
                        hideStep3();
                    }
                }
                /*Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(getContext(),  android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //Toast.makeText(getContext(), selectedHour+":"+selectedMinute, Toast.LENGTH_SHORT).show();
                        mTextViewEndTime.setText(selectedHour+":"+selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                timePicker.setTitle("Select Time");
                timePicker.show();*/
            }
        });
        mTextViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewStartDate.bringToFront();
                mTextViewStartDate.setError(null);
                if(mCalendarViewFrom.getVisibility()==View.VISIBLE){
                    mCalendarViewFrom.setVisibility(View.GONE);
                    ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) mTextViewEndDate.getLayoutParams();
                    layoutParams1.topToBottom = R.id.date_from;
                    mTextViewEndDate.setLayoutParams(layoutParams1);
                }else{
                    mCalendarViewFrom.setVisibility(View.VISIBLE);
                    mCalendarViewTo.setVisibility(View.GONE);
                    mTimePickerFrom.setVisibility(View.GONE);
                    mTimePickerTo.setVisibility(View.GONE);
                    ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) mTextViewEndDate.getLayoutParams();
                    layoutParams1.topToBottom = R.id.calendarViewFrom;
                    mTextViewEndDate.setLayoutParams(layoutParams1);
                    //ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) mTextViewEndDate.getLayoutParams();
                    //ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) mTextViewEndTime.getLayoutParams();
                    //layoutParams1.topToBottom = R.id.calendarViewFrom;
                    //layoutParams2.topToBottom = R.id.date_from;
                    //Toast.makeText(getContext(), "VISIBLE", Toast.LENGTH_SHORT).show();
                }
                /*Calendar currentTime = Calendar.getInstance();
                int day = currentTime.get(Calendar.DAY_OF_MONTH);
                int month = currentTime.get(Calendar.MONTH);
                int year = currentTime.get(Calendar.YEAR);
                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                        String date = dateFormat.format(calendar.getTime());
                        //Toast.makeText(getContext(), date, Toast.LENGTH_SHORT).show();
                        mTextViewStartDate.setText(date);
                    }
                }, year, month, day);
                datePicker.setTitle("Select Date");
                datePicker.show();*/
            }
        });
        mTextViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewEndDate.setError(null);

                if(mCalendarViewTo.getVisibility()==View.VISIBLE){
                    mCalendarViewTo.setVisibility(View.GONE);
                    ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) mTextViewLocation.getLayoutParams();
                    layoutParams1.topToBottom = R.id.date_to;
                    mTextViewLocation.setLayoutParams(layoutParams1);
                }else{
                    mCalendarViewTo.setVisibility(View.VISIBLE);
                    mCalendarViewFrom.setVisibility(View.GONE);
                    mTimePickerTo.setVisibility(View.GONE);
                    mTimePickerFrom.setVisibility(View.GONE);
                    ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) mTextViewLocation.getLayoutParams();
                    layoutParams1.topToBottom = R.id.calendarViewTo;
                    mTextViewLocation.setLayoutParams(layoutParams1);
                }
                /*Calendar currentTime = Calendar.getInstance();
                int day = currentTime.get(Calendar.DAY_OF_MONTH);
                int month = currentTime.get(Calendar.MONTH);
                int year = currentTime.get(Calendar.YEAR);
                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                        String date = dateFormat.format(calendar.getTime());
                        //Toast.makeText(getContext(), date, Toast.LENGTH_SHORT).show();
                        mTextViewEndDate.setText(date);
                    }
                }, year, month, day);
                datePicker.setTitle("Select Date");
                datePicker.show();*/
            }
        });

        mSwitchAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mTimePickerFrom.setVisibility(View.GONE);
                    mTimePickerTo.setVisibility(View.GONE);
                    mTextViewStartTime.setVisibility(View.INVISIBLE);
                    mTextViewEndTime.setVisibility(View.INVISIBLE);
                    mTextViewStartTime.setText("Time");
                    mTextViewEndTime.setText("Time");
                }else{
                    mTextViewStartTime.setVisibility(View.VISIBLE);
                    mTextViewEndTime.setVisibility(View.VISIBLE);
                }
            }
        });

        mTextViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = "";
                String dateStart = "";
                String dateEnd = "";
                String timeStart = "00:00:00Z";
                String timeEnd = "23:59:59Z";
                boolean allDay = false;
                String location = "";
                boolean reminder = false;
                String remindTime = null;

                String notes = "";
                if(!mEditTextTitleEvent.getText().toString().equals("")){
                    title = mEditTextTitleEvent.getText().toString();
                }else{
                    mEditTextTitleEvent.setError(getResources().getString(R.string.required));
                    return;
                }
                if(!mTextViewStartDate.getText().toString().equals("From")){
                    dateStart = mTextViewStartDate.getText().toString();
                }else {
                    mTextViewStartDate.setError(getResources().getString(R.string.required));
                    return;}
                if(!mTextViewEndDate.getText().toString().equals("To")){
                    dateEnd = mTextViewEndDate.getText().toString();
                }else {
                    mTextViewEndDate.setError(getResources().getString(R.string.required));
                    return;}
                if(!mSwitchAllDay.isChecked()){
                    allDay = false;
                    if(!mTextViewStartTime.getText().toString().equals("Time")){
                        timeStart = mTextViewStartTime.getText().toString();
                    }else{
                        mTextViewStartTime.setError(getResources().getString(R.string.required));
                        return;
                    }
                    if(!mTextViewEndTime.getText().toString().equals("Time")){
                        timeEnd = mTextViewEndTime.getText().toString();
                    }else{
                        mTextViewEndTime.setError(getResources().getString(R.string.required));
                        return;
                    }
                }else{
                    allDay = true;
                }
                String finalDateStart = "";
                String finalDateEnd = "";
                try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy H:mm", Locale.ENGLISH);


                String allStart = dateStart + " " + timeStart;
                String allEnd = dateEnd + " " + timeEnd;

                Date startD = dateFormat.parse(allStart);
                Date endD = dateFormat.parse(allEnd);
                if(!startD.before(endD)){
                    Toast.makeText(getContext(), "Incorrect date", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                finalDateStart = newDateFormat.format(startD);
                finalDateEnd = newDateFormat.format(endD);

                    if(mSwitchReminder.isChecked()){
                        Date reminderDate = new Date(startD.getTime()- 1800000);
                        remindTime = newDateFormat.format(reminderDate);
                    }else{
                        remindTime = null;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.d("TAG", finalDateStart);
                Log.d("TAG", finalDateEnd);
                if(mSwitchReminder.isChecked()) {
                    Log.d("TAG", remindTime);
                }else{
                    remindTime = null;
                }
                notes = mEditTextNotes.getText().toString();
                location = mEditTextLocation.getText().toString();

                AddEventData data = new AddEventData(title, "personal", allDay, finalDateStart, finalDateEnd, location, notes, remindTime);
                mPresenter.sendNewEventData(mToken, data, remindTime);
                mProgressBar.setVisibility(View.VISIBLE);
                MainActivity activity = (MainActivity) getActivity();
                if(activity.isTutirial) {
                    hideStep6();
                }
            }
        });
        MainActivity activity = (MainActivity) getActivity();
        if(activity.isTutirial) {
            showStep2();
        }
        return view;
    }

    public void setToken(String token){
        mToken = token;
    }

    private void showStep2(){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.pulsating);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(100);
        mTextViewEnterTitleText.setVisibility(View.VISIBLE);
        mTextViewEnterTitleText.startAnimation(animation);
        mLayout.setBackgroundColor(getResources().getColor(R.color.tutorialColor));
        mTextViewLocation.setAlpha(ALPHA);
        mTextViewSave.setAlpha(ALPHA);
        mTextViewStartTime.setAlpha(ALPHA);
        mTextViewEndTime.setAlpha(ALPHA);
        mTextViewStartDate.setAlpha(ALPHA);
        mTextViewEndDate.setAlpha(ALPHA);
        mEditTextLocation.setAlpha(ALPHA);
        mEditTextNotes.setAlpha(ALPHA);
        mSwitchAllDay.setAlpha(ALPHA);
        mSwitchReminder.setAlpha(ALPHA);
        mProgressBar.setAlpha(ALPHA);
        mTimePickerFrom.setAlpha(ALPHA);
        mTimePickerTo.setAlpha(ALPHA);
        mCalendarViewFrom.setAlpha(ALPHA);
        mCalendarViewTo.setAlpha(ALPHA);
        mTextViewReminderText.setAlpha(ALPHA);
        mTextViewTimeText.setAlpha(ALPHA);
        mTextViewLocationText.setAlpha(ALPHA);
        mTextViewReminderText2.setAlpha(ALPHA);
        mTextViewNotesText.setAlpha(ALPHA);
        mImageViewGirl2.setVisibility(View.VISIBLE);
    }

    private void hideStep2(){
        InputMethodManager inputManager =
                (InputMethodManager) getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        mTextViewEnterTitleText.setVisibility(View.GONE);
        mTextViewEnterTitleText.clearAnimation();
        mLayout.setBackgroundColor(getResources().getColor(R.color.tutorialColor));
        mTextViewLocation.setAlpha(1);
        mTextViewSave.setAlpha(1);
        mTextViewStartTime.setAlpha(1);
        mTextViewEndTime.setAlpha(1);
        mTextViewStartDate.setAlpha(1);
        mTextViewEndDate.setAlpha(1);
        mEditTextLocation.setAlpha(1);
        mEditTextNotes.setAlpha(1);
        mSwitchAllDay.setAlpha(1);
        mSwitchReminder.setAlpha(1);
        mProgressBar.setAlpha(1);
        mTimePickerFrom.setAlpha(1);
        mTimePickerTo.setAlpha(1);
        mCalendarViewFrom.setAlpha(1);
        mCalendarViewTo.setAlpha(1);
        mTextViewReminderText.setAlpha(1);
        mTextViewTimeText.setAlpha(1);
        mTextViewLocationText.setAlpha(1);
        mTextViewReminderText2.setAlpha(1);
        mTextViewNotesText.setAlpha(1);
        mImageViewGirl2.setVisibility(View.GONE);
        showStep3();
    }

    private void showStep3(){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.pulsating);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(100);
        mTextViewChooseTime.setVisibility(View.VISIBLE);
        mTextViewChooseTime.startAnimation(animation);
        mTextViewLocation.setAlpha(ALPHA);
        mTextViewSave.setAlpha(ALPHA);
        mDivider.setAlpha(ALPHA);
        mEditTextTitleEvent.setAlpha(ALPHA);
        mTextViewStartTime.setAlpha(1);
        mTextViewEndTime.setAlpha(1);
        mTextViewStartDate.setAlpha(1);
        mTextViewEndDate.setAlpha(1);
        mEditTextLocation.setAlpha(ALPHA);
        mEditTextNotes.setAlpha(ALPHA);
        mSwitchAllDay.setAlpha(1);
        mSwitchReminder.setAlpha(ALPHA);
        mProgressBar.setAlpha(ALPHA);
        mTimePickerFrom.setAlpha(1);
        mTimePickerTo.setAlpha(1);
        mCalendarViewFrom.setAlpha(1);
        mCalendarViewTo.setAlpha(1);
        mTextViewReminderText.setAlpha(ALPHA);
        mTextViewTimeText.setAlpha(1);
        mTextViewLocationText.setAlpha(ALPHA);
        mTextViewReminderText2.setAlpha(ALPHA);
        mTextViewNotesText.setAlpha(ALPHA);
    }

    private void hideStep3(){
        mTextViewChooseTime.setVisibility(View.GONE);
        mTextViewChooseTime.clearAnimation();
        mTextViewLocation.setAlpha(ALPHA);
        mTextViewSave.setAlpha(ALPHA);
        mDivider.setAlpha(ALPHA);
        mEditTextTitleEvent.setAlpha(ALPHA);
        mTextViewStartTime.setAlpha(ALPHA);
        mTextViewEndTime.setAlpha(ALPHA);
        mTextViewStartDate.setAlpha(ALPHA);
        mTextViewEndDate.setAlpha(ALPHA);
        mEditTextLocation.setAlpha(ALPHA);
        mEditTextNotes.setAlpha(ALPHA);
        mSwitchAllDay.setAlpha(ALPHA);
        mSwitchReminder.setAlpha(ALPHA);
        mProgressBar.setAlpha(ALPHA);
        mTimePickerFrom.setAlpha(ALPHA);
        mTimePickerTo.setAlpha(ALPHA);
        mCalendarViewFrom.setAlpha(ALPHA);
        mCalendarViewTo.setAlpha(ALPHA);
        mTextViewReminderText.setAlpha(ALPHA);
        mTextViewTimeText.setAlpha(ALPHA);
        mTextViewLocationText.setAlpha(ALPHA);
        mTextViewReminderText2.setAlpha(ALPHA);
        mTextViewNotesText.setAlpha(ALPHA);
        showStep4();
    }

    private void showStep4(){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.pulsating);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(100);
        mTextViewEnterLocationText.startAnimation(animation);
        mTextViewEnterLocationText.setVisibility(View.VISIBLE);
        mTextViewEnterLocationText.setAlpha(1);
        mTextViewSave.setAlpha(ALPHA);
        mDivider.setAlpha(ALPHA);
        mEditTextTitleEvent.setAlpha(ALPHA);
        mTextViewStartTime.setAlpha(ALPHA);
        mTextViewEndTime.setAlpha(ALPHA);
        mTextViewStartDate.setAlpha(ALPHA);
        mTextViewEndDate.setAlpha(ALPHA);
        mEditTextLocation.setAlpha(1);
        mEditTextNotes.setAlpha(ALPHA);
        mSwitchAllDay.setAlpha(ALPHA);
        mSwitchReminder.setAlpha(ALPHA);
        mProgressBar.setAlpha(ALPHA);
        mTimePickerFrom.setAlpha(ALPHA);
        mTimePickerTo.setAlpha(ALPHA);
        mCalendarViewFrom.setAlpha(ALPHA);
        mCalendarViewTo.setAlpha(ALPHA);
        mTextViewReminderText.setAlpha(ALPHA);
        mTextViewTimeText.setAlpha(ALPHA);
        mTextViewLocationText.setAlpha(1);
        mTextViewReminderText2.setAlpha(ALPHA);
        mTextViewNotesText.setAlpha(ALPHA);
    }

    private void hideStep4(){
        InputMethodManager inputManager =
                (InputMethodManager) getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        mTextViewEnterLocationText.clearAnimation();
        mTextViewEnterLocationText.setVisibility(View.GONE);
        mTextViewEnterLocationText.setAlpha(ALPHA);
        mTextViewSave.setAlpha(ALPHA);
        mDivider.setAlpha(ALPHA);
        mEditTextTitleEvent.setAlpha(ALPHA);
        mTextViewStartTime.setAlpha(ALPHA);
        mTextViewEndTime.setAlpha(ALPHA);
        mTextViewStartDate.setAlpha(ALPHA);
        mTextViewEndDate.setAlpha(ALPHA);
        mEditTextLocation.setAlpha(ALPHA);
        mEditTextNotes.setAlpha(ALPHA);
        mSwitchAllDay.setAlpha(ALPHA);
        mSwitchReminder.setAlpha(ALPHA);
        mProgressBar.setAlpha(ALPHA);
        mTimePickerFrom.setAlpha(ALPHA);
        mTimePickerTo.setAlpha(ALPHA);
        mCalendarViewFrom.setAlpha(ALPHA);
        mCalendarViewTo.setAlpha(ALPHA);
        mTextViewReminderText.setAlpha(ALPHA);
        mTextViewTimeText.setAlpha(ALPHA);
        mTextViewLocationText.setAlpha(ALPHA);
        mTextViewReminderText2.setAlpha(ALPHA);
        mTextViewNotesText.setAlpha(ALPHA);
        showStep5();
    }

    private void showStep5(){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.pulsating);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(100);
        mTextViewReminderChoose.startAnimation(animation);
        mTextViewReminderChoose.setVisibility(View.VISIBLE);
        mTextViewReminderChoose.setAlpha(1);
        mTextViewSave.setAlpha(ALPHA);
        mDivider.setAlpha(ALPHA);
        mEditTextTitleEvent.setAlpha(ALPHA);
        mTextViewStartTime.setAlpha(ALPHA);
        mTextViewEndTime.setAlpha(ALPHA);
        mTextViewStartDate.setAlpha(ALPHA);
        mTextViewEndDate.setAlpha(ALPHA);
        mEditTextLocation.setAlpha(ALPHA);
        mEditTextNotes.setAlpha(ALPHA);
        mSwitchAllDay.setAlpha(ALPHA);
        mSwitchReminder.setAlpha(1);
        mProgressBar.setAlpha(ALPHA);
        mTimePickerFrom.setAlpha(ALPHA);
        mTimePickerTo.setAlpha(ALPHA);
        mCalendarViewFrom.setAlpha(ALPHA);
        mCalendarViewTo.setAlpha(ALPHA);
        mTextViewReminderText.setAlpha(1);
        mTextViewTimeText.setAlpha(ALPHA);
        mTextViewLocationText.setAlpha(ALPHA);
        mTextViewReminderText2.setAlpha(1);
        mTextViewNotesText.setAlpha(ALPHA);
    }

    private void hideStep5(){
        mTextViewReminderChoose.clearAnimation();
        mTextViewReminderChoose.setVisibility(View.GONE);
        mTextViewReminderChoose.setAlpha(ALPHA);
        mTextViewSave.setAlpha(ALPHA);
        mDivider.setAlpha(ALPHA);
        mEditTextTitleEvent.setAlpha(ALPHA);
        mTextViewStartTime.setAlpha(ALPHA);
        mTextViewEndTime.setAlpha(ALPHA);
        mTextViewStartDate.setAlpha(ALPHA);
        mTextViewEndDate.setAlpha(ALPHA);
        mEditTextLocation.setAlpha(ALPHA);
        mEditTextNotes.setAlpha(ALPHA);
        mSwitchAllDay.setAlpha(ALPHA);
        mSwitchReminder.setAlpha(ALPHA);
        mProgressBar.setAlpha(ALPHA);
        mTimePickerFrom.setAlpha(ALPHA);
        mTimePickerTo.setAlpha(ALPHA);
        mCalendarViewFrom.setAlpha(ALPHA);
        mCalendarViewTo.setAlpha(ALPHA);
        mTextViewReminderText.setAlpha(ALPHA);
        mTextViewTimeText.setAlpha(ALPHA);
        mTextViewLocationText.setAlpha(ALPHA);
        mTextViewReminderText2.setAlpha(ALPHA);
        mTextViewNotesText.setAlpha(ALPHA);
        showStep6();
    }

    private void showStep6(){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.pulsating);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(100);
        mTextViewEnterNotes.startAnimation(animation);
        mTextViewEnterNotes.setVisibility(View.VISIBLE);
        mTextViewSave.setAlpha(1);
        mDivider.setAlpha(ALPHA);
        mEditTextTitleEvent.setAlpha(ALPHA);
        mTextViewStartTime.setAlpha(ALPHA);
        mTextViewEndTime.setAlpha(ALPHA);
        mTextViewStartDate.setAlpha(ALPHA);
        mTextViewEndDate.setAlpha(ALPHA);
        mEditTextLocation.setAlpha(ALPHA);
        mEditTextNotes.setAlpha(1);
        mSwitchAllDay.setAlpha(ALPHA);
        mSwitchReminder.setAlpha(ALPHA);
        mProgressBar.setAlpha(ALPHA);
        mTimePickerFrom.setAlpha(ALPHA);
        mTimePickerTo.setAlpha(ALPHA);
        mCalendarViewFrom.setAlpha(ALPHA);
        mCalendarViewTo.setAlpha(ALPHA);
        mTextViewReminderText.setAlpha(ALPHA);
        mTextViewTimeText.setAlpha(ALPHA);
        mTextViewLocationText.setAlpha(ALPHA);
        mTextViewReminderText2.setAlpha(ALPHA);
        mTextViewNotesText.setAlpha(1);
    }

    private void hideStep6(){
        mTextViewEnterNotes.clearAnimation();
        mTextViewEnterNotes.setVisibility(View.GONE);
        mTextViewSave.setAlpha(ALPHA);
        mDivider.setAlpha(ALPHA);
        mEditTextTitleEvent.setAlpha(ALPHA);
        mTextViewStartTime.setAlpha(ALPHA);
        mTextViewEndTime.setAlpha(ALPHA);
        mTextViewStartDate.setAlpha(ALPHA);
        mTextViewEndDate.setAlpha(ALPHA);
        mEditTextLocation.setAlpha(ALPHA);
        mEditTextNotes.setAlpha(ALPHA);
        mSwitchAllDay.setAlpha(ALPHA);
        mSwitchReminder.setAlpha(ALPHA);
        mProgressBar.setAlpha(ALPHA);
        mTimePickerFrom.setAlpha(ALPHA);
        mTimePickerTo.setAlpha(ALPHA);
        mCalendarViewFrom.setAlpha(ALPHA);
        mCalendarViewTo.setAlpha(ALPHA);
        mTextViewReminderText.setAlpha(ALPHA);
        mTextViewTimeText.setAlpha(ALPHA);
        mTextViewLocationText.setAlpha(ALPHA);
        mTextViewReminderText2.setAlpha(ALPHA);
        mTextViewNotesText.setAlpha(ALPHA);
        showStep7();
    }

    private void showStep7(){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.pulsating_circle);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mImageViewPulsating.startAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImageViewPulsating.setVisibility(View.VISIBLE);
        mImageViewPulsating.startAnimation(animation);
        mImageViewArrow.setVisibility(View.VISIBLE);
        mTextViewLetsTo.setVisibility(View.VISIBLE);
        mTextViewCongrats.setVisibility(View.VISIBLE);
        mTextViewSave.setAlpha(ALPHA);
        mDivider.setAlpha(ALPHA);
        mEditTextTitleEvent.setAlpha(ALPHA);
        mTextViewStartTime.setAlpha(ALPHA);
        mTextViewEndTime.setAlpha(ALPHA);
        mTextViewStartDate.setAlpha(ALPHA);
        mTextViewEndDate.setAlpha(ALPHA);
        mEditTextLocation.setAlpha(ALPHA);
        mEditTextNotes.setAlpha(ALPHA);
        mSwitchAllDay.setAlpha(ALPHA);
        mSwitchReminder.setAlpha(ALPHA);
        mProgressBar.setAlpha(ALPHA);
        mTimePickerFrom.setAlpha(ALPHA);
        mTimePickerTo.setAlpha(ALPHA);
        mCalendarViewFrom.setAlpha(ALPHA);
        mCalendarViewTo.setAlpha(ALPHA);
        mTextViewReminderText.setAlpha(ALPHA);
        mTextViewTimeText.setAlpha(ALPHA);
        mTextViewLocationText.setAlpha(ALPHA);
        mTextViewReminderText2.setAlpha(ALPHA);
        mTextViewNotesText.setAlpha(ALPHA);
        mImageVIewGirl3.setVisibility(View.VISIBLE);
        MainActivity activity = (MainActivity) getActivity();
        activity.showSteps = true;
    }

    public void hideStep7(){
        mImageViewArrow.setVisibility(View.GONE);
        mImageViewArrow.clearAnimation();
        mTextViewLetsTo.setVisibility(View.VISIBLE);
        mTextViewCongrats.setVisibility(View.VISIBLE);
        mTextViewSave.setAlpha(ALPHA);
        mDivider.setAlpha(ALPHA);
        mEditTextTitleEvent.setAlpha(ALPHA);
        mTextViewStartTime.setAlpha(ALPHA);
        mTextViewEndTime.setAlpha(ALPHA);
        mTextViewStartDate.setAlpha(ALPHA);
        mTextViewEndDate.setAlpha(ALPHA);
        mEditTextLocation.setAlpha(ALPHA);
        mEditTextNotes.setAlpha(ALPHA);
        mSwitchAllDay.setAlpha(ALPHA);
        mSwitchReminder.setAlpha(ALPHA);
        mProgressBar.setAlpha(ALPHA);
        mTimePickerFrom.setAlpha(ALPHA);
        mTimePickerTo.setAlpha(ALPHA);
        mCalendarViewFrom.setAlpha(ALPHA);
        mCalendarViewTo.setAlpha(ALPHA);
        mTextViewReminderText.setAlpha(ALPHA);
        mTextViewTimeText.setAlpha(ALPHA);
        mTextViewLocationText.setAlpha(ALPHA);
        mTextViewReminderText2.setAlpha(ALPHA);
        mTextViewNotesText.setAlpha(ALPHA);
        mImageVIewGirl3.setVisibility(View.INVISIBLE);
    }

    public void showAll(){
        mTextViewSave.setAlpha(1);
        mDivider.setAlpha(1);
        mEditTextTitleEvent.setAlpha(1);
        mTextViewStartTime.setAlpha(1);
        mTextViewEndTime.setAlpha(1);
        mTextViewStartDate.setAlpha(1);
        mTextViewEndDate.setAlpha(1);
        mEditTextLocation.setAlpha(1);
        mEditTextNotes.setAlpha(1);
        mSwitchAllDay.setAlpha(1);
        mSwitchReminder.setAlpha(1);
        mProgressBar.setAlpha(1);
        mTimePickerFrom.setAlpha(1);
        mTimePickerTo.setAlpha(1);
        mCalendarViewFrom.setAlpha(1);
        mCalendarViewTo.setAlpha(1);
        mTextViewReminderText.setAlpha(1);
        mTextViewTimeText.setAlpha(1);
        mTextViewLocationText.setAlpha(1);
        mTextViewReminderText2.setAlpha(1);
        mTextViewNotesText.setAlpha(1);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.INVISIBLE);
       //mEditTextTitleEvent.setHint("Title of event");
       //mTextViewStartDate.setHint("From");
       //mTextViewStartTime.setHint("Time");
       //mTextViewEndDate.setHint("To");
       //mTextViewEndTime.setHint("Time");
       //mEditTextLocation.setHint("Enter location here");
       //mEditTextNotes.setHint("Enter notes here");
       //mSwitchReminder.setChecked(false);
       //mSwitchAllDay.setChecked(false);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }
}
