package com.mydomain.galcal;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mydomain.galcal.APIUtils.APIUtils;
import com.mydomain.galcal.data.BackgroundImageInfo;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.data.new_data.Day;
import com.mydomain.galcal.data.new_data.Event;
import com.mydomain.galcal.data.new_data.Response;
import com.mydomain.galcal.settings.SettingsFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Nikita on 21.01.2019.
 */

public class MainPresenter implements BaseContract.BasePresenter {

    private String date;
    private MainActivity mActivity;
    private CompositeDisposable mDisposables;
    private APIUtils mApiUtils;

    public MainPresenter(MainActivity activity){
        mActivity = activity;

    }

    public void fetchNewbieBackgroundImageInfo(String token){
        Disposable newbieImage = mApiUtils.getNewbieImage(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(String image) {
                        mActivity.setBackgroundInfo(null);
                        mActivity.setBackgroundImage(image);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposables.add(newbieImage);
    }

    public void fetchBackgroundImageInfo(String token){
        //String date = LocalDate.now().toString();
       // DateFormat df = DateFormat.getTimeInstance();
       // df.setTimeZone(TimeZone.getTimeZone("gmt"));
       // String gmtTime = df.format(new Date());
       // Log.d("TIME", gmtTime);
       // TimeZone timeZone = TimeZone.getDefault();
       // String difference = String.valueOf(timeZone.getRawOffset()/3600000);
       // String s = CalendarDay.today().toString();
       // Log.d("TIME", s+gmtTime+difference);

        final Date currentTime = new Date();
        TimeZone timeZone = TimeZone.getDefault();

        final SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        sdf.setTimeZone(TimeZone.getDefault());
        int timeZoneInt = timeZone.getRawOffset()/3600000;
        String timeZoneStr = "";
        if(timeZoneInt > 0) {
            if (timeZoneInt < 10) {
                timeZoneStr = "+0" + String.valueOf(timeZoneInt);
            } else {
                timeZoneStr = "+" + String.valueOf(timeZoneInt);
            }
        }else{
            if(timeZoneInt>-10){
                timeZoneStr = "-0" + String.valueOf(Math.abs(timeZoneInt));
            }else{
                timeZoneStr = String.valueOf(timeZoneInt);
            }
        }

        Log.d("Жирный", timeZoneStr);
        date = sdf.format(currentTime)+timeZoneStr+":00";
        //String date = LocalDate.now().toString();
        Log.d("Жирный", date);
       // Log.d("LOCALDATE", "now " + date);
       // Log.d("TAG", date);
        Disposable backgroundImageInfo = mApiUtils.getBackgroundImageInfo(token, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<BackgroundImageInfo>>() {
                    @Override
                    public void onSuccess(ArrayList<BackgroundImageInfo> info) {
                        if(info.size()!=0) {
                            mActivity.setBackgroundInfo(info.get(0));
                            mActivity.setBackgroundImage(info.get(0).image);
                            Log.d("BEARER", info.get(0).image);
                        }else{
                            mActivity.setBackgroundInfo(null);
                            mActivity.setBackgroundImage(null);
                        }
                        //Toast.makeText(mActivity.getApplicationContext(), date, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //Log.d("BEARER", "ERROR");
                        //Toast.makeText(mActivity.getApplicationContext(), "The device has an incorrect date", Toast.LENGTH_LONG).show();
                        //e.printStackTrace();
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException) e;
                            ResponseBody responseBody = exception.response().errorBody();
                            try {
                                JSONObject responseError = new JSONObject(responseBody.string());
                                JSONArray arrayError = responseError.getJSONArray("errors");
                                JSONObject errorMessage = arrayError.getJSONObject(0);
                                final SimpleDateFormat errorFormat =
                                        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                                final SimpleDateFormat newFormat = new SimpleDateFormat("yyyy MM dd, HH:mm");
                                Date date = errorFormat.parse(errorMessage.getString("server_time"));
                                String serverTime = newFormat.format(date);
                                String yourTime = newFormat.format(Calendar.getInstance().getTime());

                                Dialog dialog = getTimeDialog(serverTime, yourTime);
                                dialog.show();
                                Toast.makeText(mActivity.getApplicationContext(), "You have time difference with Greenwich time more than 12 hours\nGreenwich time: "
                                        + serverTime
                                        + "\nYour device time:" + yourTime,
                                        Toast.LENGTH_LONG).show();
                                Log.d("TAG", errorMessage.getString("server_time"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });


        mDisposables.add(backgroundImageInfo);
    }

    private Dialog getTimeDialog(String timeGreenwich, String yourTime){
        final Dialog dialog = new Dialog(mActivity, R.style.DialogTheme);
        dialog.setContentView(R.layout.error_dialog);

        dialog.setTitle("");
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
        final TextView textViewGreenwichTime = (TextView) dialog.findViewById(R.id.textViewGreenwichTime);
        final TextView textViewYourTime = (TextView) dialog.findViewById(R.id.textViewYourTime);
        textViewGreenwichTime.setText(timeGreenwich);
        textViewYourTime.setText(yourTime);
        Button buttonOk = (Button) dialog.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    //public void fetchEventsForYearNew(String token){
    //    Calendar calendar1 = Calendar.getInstance();
    //    calendar1.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 0);
    //    Calendar calendar2 = Calendar.getInstance();
    //    calendar2.set(calendar1.get(Calendar.YEAR)+1, calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
    //    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
    //    String startTime = dateFormat.format(calendar1.getTime());
    //    String endTime = dateFormat.format(calendar2.getTime());
    //    Disposable eventsForYear = mApiUtils.getAllEvents(token, startTime, endTime)
    //            .subscribeOn(Schedulers.io())
    //            .observeOn(AndroidSchedulers.mainThread())
    //            .subscribeWith(new DisposableSingleObserver<Map<String, ArrayList<Event>>>() {
    //                @Override
    //                public void onSuccess(Map<String, ArrayList<Event>> response) {
    //                    Set<String> keys = response.keySet();
    //                    ArrayList<String> dates = new ArrayList<String>();
    //                    dates.addAll(keys);
    //                    ArrayList<Day> days = new ArrayList<Day>();
    //                    for(int i = 0; i < dates.size(); i++){
    //                        //Log.v("NEW_DATE", events.get(i).date);
    //                        //Toast.makeText(mActivity.getApplicationContext(), "date = " + dates.get(i), Toast.LENGTH_SHORT).show();
    //                        days.add(new Day(dates.get(i), response.get(dates.get(i))));
    //                    }
    //                    mActivity.setDays(days);
    //                }
//
    //                @Override
    //                public void onError(Throwable e) {
    //                    e.printStackTrace();
    //                }
    //            });
    //    mDisposables.add(eventsForYear);
    //}


    public void fetchEventsForYear(String token){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(calendar1.get(Calendar.YEAR)+1, calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        String startTime = dateFormat.format(calendar1.getTime());
        String endTime = dateFormat.format(calendar2.getTime());
        Disposable eventsForYear = mApiUtils.getTodayEventList(token, startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<DayEventData>>() {
                    @Override
                    public void onSuccess(ArrayList<DayEventData> events) {
                        mActivity.setEventsForYear(events);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
        mDisposables.add(eventsForYear);
    }

    @Override
    public void onStart() {
        mDisposables = new CompositeDisposable();
        mApiUtils = new APIUtils();
    }

    @Override
    public void onStop() {
        mDisposables.clear();
    }


}
