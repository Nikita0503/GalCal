package com.mydomain.galcal.addEvent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mydomain.galcal.APIUtils.APIUtils;
import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.MainActivity;
import com.mydomain.galcal.TimeNotification;
import com.mydomain.galcal.data.AddEventData;
import com.mydomain.galcal.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Nikita on 22.01.2019.
 */

public class AddEventPresenter implements BaseContract.BasePresenter {

    private AddEventFragment mFragment;
    private CompositeDisposable mDisposables;
    private APIUtils mApiUtils;

    public AddEventPresenter(AddEventFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public void onStart() {
        mDisposables = new CompositeDisposable();
        mApiUtils = new APIUtils();
    }

    public void sendNewEventData(String token, final AddEventData data, final String reminder){
        Disposable newEvent = mApiUtils.createNewEvent(token, data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                        mFragment.showMessage("Event created successfully");
                        MainActivity activity = (MainActivity) mFragment.getActivity();
                        activity.fetchEventsForYear();
                        try {
                            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
                            Date date = Calendar.getInstance().getTime();
                            Date date1 = oldFormat.parse(data.startTime);
                            if(newFormat.format(date).equals(newFormat.format(date1))) {
                                activity.updateHomeTab();
                                //activity.firstCreating = true;
                                activity.fetchEventsForYear();
                                if(reminder != null) {
                                    SharedPreferences pref = mFragment.getActivity().getSharedPreferences("GalCal", mFragment.getActivity().MODE_PRIVATE);
                                    String userName = pref.getString("userName", "").split("@")[0];
                                    AlarmManager am = (AlarmManager) mFragment.getActivity().getSystemService(Context.ALARM_SERVICE);
                                    Intent intent = new Intent(mFragment.getContext(), TimeNotification.class);
                                    intent.putExtra("userName", userName);
                                    intent.putExtra("event", data.title);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mFragment.getContext(), 0,
                                            intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                    am.cancel(pendingIntent);
                                    Log.d("FINAL", "тюнсь");
                                    Log.d("FINAL", data.startTime);
                                    SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                                    Date dateRemind = timeStamp.parse(data.startTime);
                                    dateRemind.setTime(dateRemind.getTime() - 1800000);
                                    Log.d("FINAL", timeStamp.format(dateRemind));
                                    am.set(AlarmManager.RTC_WAKEUP, dateRemind.getTime(), pendingIntent);
                                }else{
                                    Log.d("FINAL", "не тюнсь");
                                }
                            }
                        }catch (Exception c){
                            c.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException) e;
                            ResponseBody responseBody = exception.response().errorBody();
                            try {
                                JSONObject responseError = new JSONObject(responseBody.string());
                                JSONArray arrayError = responseError.getJSONArray("errors");
                                JSONObject errorMessage = arrayError.getJSONObject(0);
                                mFragment.showMessage(errorMessage.getString("ERROR_MESSAGE"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
        mDisposables.add(newEvent);
    }

    @Override
    public void onStop() {
        mDisposables.clear();
    }
}
