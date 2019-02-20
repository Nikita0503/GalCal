package com.mydomain.galcal.editEvent;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.mydomain.galcal.data.DayEventData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nikita on 22.01.2019.
 */

public class EditEventFragment extends Fragment implements BaseContract.BaseView {

    private String mToken;
    private DayEventData mData;
    private EditEventPresenter mPresenter;
    private TextView mTextViewDelete;
    private TextView mTextViewEdit;
    private TextView mTextViewStartTime;
    private TextView mTextViewEndTime;
    private TextView mTextViewStartDate;
    private TextView mTextViewEndDate;
    private TextView mTextViewLocation;
    private EditText mEditTextTitleEvent;
    private EditText mEditTextLocation;
    private EditText mEditTextNotes;
    private Switch mSwitchAllDay;
    private Switch mSwitchReminder;
    private MaterialCalendarView mCalendarViewFrom;
    private MaterialCalendarView mCalendarViewTo;
    private TimePicker mTimePickerFrom;
    private TimePicker mTimePickerTo;
    private ProgressBar mProgressBar;
    private ConstraintLayout mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new EditEventPresenter(this);
        mPresenter.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_event_fragment, container, false);
        mLayout = (ConstraintLayout) view.findViewById(R.id.layout);
        mProgressBar = (ProgressBar)view.findViewById(R.id.spin_kit);
        MainActivity activity = (MainActivity) getActivity();
        if(activity.isTutirial){
            mLayout.setBackgroundColor(getResources().getColor(R.color.tutorialColor));
            activity.setEditFragment(this);
        }

        Sprite doubleBounce = new Circle();
        mProgressBar.setIndeterminateDrawable(doubleBounce);
        mTextViewLocation = (TextView) view.findViewById(R.id.location_tv);
        mTextViewDelete = (TextView) view.findViewById(R.id.delete);
        mTextViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.delete_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
                Button buttonOk = (Button) dialog.findViewById(R.id.buttonDelete);
                Button buttonCancel = (Button) dialog.findViewById(R.id.buttonOk);
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.deleteEvent(mToken, mData);
                        dialog.dismiss();
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                });
                dialog.show();
            }
        });
        mTextViewEdit = (TextView) view.findViewById(R.id.edit);
        mTextViewStartTime = (TextView) view.findViewById(R.id.time_from);
        mTextViewEndTime = (TextView) view.findViewById(R.id.time_to);
        mTextViewStartDate = (TextView) view.findViewById(R.id.date_from);
        mTextViewEndDate = (TextView) view.findViewById(R.id.date_to);
        mEditTextTitleEvent = (EditText) view.findViewById(R.id.event1);
        mEditTextLocation = (EditText) view.findViewById(R.id.location);
        mEditTextNotes = (EditText) view.findViewById(R.id.notes_ed);
        mSwitchAllDay = (Switch) view.findViewById(R.id.all_day_switch);
        mSwitchReminder = (Switch) view.findViewById(R.id.reminder_switch);


        mTimePickerFrom = (TimePicker) view.findViewById(R.id.timePickerFrom);
        mTimePickerFrom.setVisibility(View.GONE);

        mTimePickerTo = (TimePicker) view.findViewById(R.id.timePickerTo);
        mTimePickerTo.setVisibility(View.GONE);
        Calendar calendar = Calendar.getInstance();

        mCalendarViewFrom = (MaterialCalendarView) view.findViewById(R.id.calendarViewFrom);
        mCalendarViewTo = (MaterialCalendarView) view.findViewById(R.id.calendarViewTo);
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


        mEditTextTitleEvent.setText(mData.title);
        mSwitchAllDay.setChecked(mData.isAllDay);
        if(mData.isAllDay){
            try {
                mTextViewStartTime.setVisibility(View.INVISIBLE);
                mTextViewEndTime.setVisibility(View.INVISIBLE);
                String dateStart;
                String dateEnd;
                SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                Date start = oldDateFormat.parse(mData.startTime);
                Date end = oldDateFormat.parse(mData.endTime);
                dateStart = dateFormat.format(start);
                dateEnd = dateFormat.format(end);
                mTextViewStartDate.setText(dateStart);
                mTextViewEndDate.setText(dateEnd);
            }catch (Exception c){
                c.printStackTrace();
            }
        }else{
            try {
                String dateStart;
                String dateEnd;
                String timeStart;
                String timeEnd;
                SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm", Locale.ENGLISH);
                Date start = oldDateFormat.parse(mData.startTime);
                Date end = oldDateFormat.parse(mData.endTime);
                dateStart = dateFormat.format(start);
                dateEnd = dateFormat.format(end);
                timeStart = timeFormat.format(start);
                timeEnd = timeFormat.format(end);
                mTextViewStartDate.setText(dateStart);
                mTextViewEndDate.setText(dateEnd);
                mTextViewStartTime.setText(timeStart);
                mTextViewEndTime.setText(timeEnd);
            } catch (Exception c){
                c.printStackTrace();
            }
        }
        //mTextViewStartDate.setText(mData.s);
        mEditTextLocation.setText(mData.location);
        if(mData.remindTime!=null){
            mSwitchReminder.setChecked(true);
        }
        //mSwitchReminder.setChecked(mData.re);
        mEditTextNotes.setText(mData.notes);
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
                    mTimePickerFrom.setVisibility(View.VISIBLE);
                    Calendar calendar = Calendar.getInstance();
                    mTextViewStartTime.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
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
                    mTextViewStartTime.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
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
                    mTextViewEndTime.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
                    mTimePickerTo.setVisibility(View.VISIBLE);
                    mTimePickerFrom.setVisibility(View.GONE);
                    mCalendarViewFrom.setVisibility(View.GONE);
                    mCalendarViewTo.setVisibility(View.GONE);
                    ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) mTextViewLocation.getLayoutParams();
                    layoutParams1.topToBottom = R.id.timePickerTo;
                    mTextViewLocation.setLayoutParams(layoutParams1);
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
        mTextViewEdit.setOnClickListener(new View.OnClickListener() {
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
                        Log.d("TAG", "TRUE");
                        Date reminderDate = new Date(startD.getTime()- 1800000);
                        remindTime = newDateFormat.format(reminderDate);
                    }else{
                        Log.d("TAG", "FALSE");
                        remindTime = null;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.d("TAG", finalDateStart+" S");
                Log.d("TAG", finalDateEnd+" F");
                if(mSwitchReminder.isChecked()) {
                    Log.d("TAG", remindTime+" R");
                }
                notes = mEditTextNotes.getText().toString();
                location = mEditTextLocation.getText().toString();

                AddEventData data = new AddEventData(title, "personal", allDay, finalDateStart, finalDateEnd, location, notes, remindTime);
                mProgressBar.setVisibility(View.VISIBLE);
                mPresenter.editEvent(mToken, String.valueOf(mData.id), data);
                //Toast.makeText(getContext(), "Go", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void setEventData(DayEventData data){
        mData = data;
    }

    public void setToken(String token){
        mToken = token;
    }

    public void setBackground(){
        mLayout.setBackground(getResources().getDrawable(R.drawable.home_tab_background));
    }

    @Override
    public void showMessage(String message) {
        mProgressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getView() == null){
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }
}
