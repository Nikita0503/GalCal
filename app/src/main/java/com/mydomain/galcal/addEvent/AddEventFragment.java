package com.mydomain.galcal.addEvent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.R;
import com.mydomain.galcal.data.AddEventData;
import com.mydomain.galcal.home.HomePresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nikita on 17.01.2019.
 */

public class AddEventFragment extends Fragment implements BaseContract.BaseView{

    private String mToken;
    private AddEventPresenter mPresenter;
    private TextView mTextViewSave;
    private TextView mTextViewStartTime;
    private TextView mTextViewEndTime;
    private TextView mTextViewStartDate;
    private TextView mTextViewEndDate;
    private EditText mEditTextTitleEvent;
    private EditText mEditTextLocation;
    private EditText mEditTextNotes;
    private Switch mSwitchAllDay;
    private Switch mSwitchReminder;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddEventPresenter(this);
        mPresenter.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_event_fragment, container, false);
        mTextViewSave = (TextView) view.findViewById(R.id.save);
        mTextViewStartTime = (TextView) view.findViewById(R.id.time_from);
        mTextViewEndTime = (TextView) view.findViewById(R.id.time_to);
        mTextViewStartDate = (TextView) view.findViewById(R.id.date_from);
        mTextViewEndDate = (TextView) view.findViewById(R.id.date_to);
        mEditTextTitleEvent = (EditText) view.findViewById(R.id.event1);
        mEditTextLocation = (EditText) view.findViewById(R.id.location);
        mEditTextNotes = (EditText) view.findViewById(R.id.notes_ed);
        mSwitchAllDay = (Switch) view.findViewById(R.id.all_day_switch);
        mSwitchReminder = (Switch) view.findViewById(R.id.reminder_switch);
        mProgressBar = (ProgressBar)view.findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Circle();
        mProgressBar.setIndeterminateDrawable(doubleBounce);
        mTextViewStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewStartTime.setError(null);
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //Toast.makeText(getContext(), selectedHour+":"+selectedMinute, Toast.LENGTH_SHORT).show();
                        mTextViewStartTime.setText(selectedHour+":"+selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        mTextViewEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewEndTime.setError(null);
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //Toast.makeText(getContext(), selectedHour+":"+selectedMinute, Toast.LENGTH_SHORT).show();
                        mTextViewEndTime.setText(selectedHour+":"+selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                timePicker.setTitle("Select Time");
                timePicker.show();
            }
        });
        mTextViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewStartDate.setError(null);
                Calendar currentTime = Calendar.getInstance();
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
                datePicker.show();
            }
        });
        mTextViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewEndDate.setError(null);
                Calendar currentTime = Calendar.getInstance();
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
                datePicker.show();
            }
        });

        mSwitchAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy H:m", Locale.ENGLISH);


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
            }
        });
        return view;
    }

    public void setToken(String token){
        mToken = token;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }
}
