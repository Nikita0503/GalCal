package com.mydomain.galcal.week;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mydomain.galcal.APIUtils.APIUtils;
import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.data.DayOfWeekEventData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Nikita on 23.01.2019.
 */

public class WeekPresenter implements BaseContract.BasePresenter {

    private String mToken;
    private WeekFragment mFragment;
    private CompositeDisposable mDisposables;
    private APIUtils mApiUtils;
    private Calendar mLastDate;

    public WeekPresenter(WeekFragment mFragment) {
        this.mFragment = mFragment;
        mLastDate = Calendar.getInstance();
        mLastDate.set(mLastDate.get(Calendar.YEAR), mLastDate.get(Calendar.MONTH), mLastDate.get(Calendar.DAY_OF_MONTH), 0, 0 ,0);
    }

    @Override
    public void onStart() {
        mDisposables = new CompositeDisposable();
        mApiUtils = new APIUtils();
        //fetchEventsByDates();
    }

    public void setToken(String token){
        mToken = token;
    }

    public void fetchEventsByDates(){
        final ArrayList<String> dates = new ArrayList<String>();
        final SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        //final SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        final SimpleDateFormat newFormant = new SimpleDateFormat("E'\n'd'\n'MMM", Locale.ENGLISH);
        int month = mLastDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < month; i++) {
            Date date = mLastDate.getTime();
            dates.add(oldFormat.format(date));
            //String day = newFormant.format(date);
            //mList.add(new DayOfWeekEventData(day, new ArrayList<DayEventData>()));
            date.setTime(date.getTime() + 86400000);
            mLastDate.setTime(date);
        }

        Disposable fetchEventList = mApiUtils.getTodayEventList(mToken, dates.get(0)+"T00:00", dates.get(dates.size()-1)+"T23:59")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<DayEventData>>() {
                    @Override
                    public void onSuccess(ArrayList<DayEventData> data) {
                        ArrayList<DayOfWeekEventData> list = new ArrayList<DayOfWeekEventData>();
                        try {
                            for(int i = 0; i < dates.size(); i++){
                                ArrayList<DayEventData> events = new ArrayList<DayEventData>();
                                for(int j = 0; j < data.size(); j++){
                                    Date date1 = oldFormat.parse(dates.get(i));
                                    Date date2 = oldFormat.parse(data.get(j).startTime);
                                    if(oldFormat.format(date1).equals(oldFormat.format(date2))){
                                        events.add(data.get(j));
                                    }
                                }
                                Date date1 = oldFormat.parse(dates.get(i));
                                list.add(new DayOfWeekEventData(newFormant.format(date1),events));
                                Log.d("TAG",newFormant.format(date1));

                            }
                            mFragment.addNewEvents(list, data);
                            /*SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                            ArrayList<DayOfWeekEventData> list = new ArrayList<DayOfWeekEventData>();
                            for (int i = 0; i < data.size(); i++) {
                                Date date = apiFormat.parse(data.get(i).startTime);
                                list.add(new DayOfWeekEventData(newFormant.format(date), data));
                            }*/



                            //mFragment.addNewEvents(list);
                            for (int i = 0; i < data.size(); i++) {
                                Log.d("TAG", data.get(i).title);
                            }
                        }catch (Exception c){
                            c.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException) e;
                            ResponseBody responseBody = exception.response().errorBody();
                            try {
                                JSONObject responseError = new JSONObject(responseBody.string());
                                JSONArray arrayError = responseError.getJSONArray("errors");
                                JSONObject errorMessage = arrayError.getJSONObject(0);
                                //mFragment.showMessage(errorMessage.getString("ERROR_MESSAGE"));
                                Log.d("TAG", errorMessage.getString("ERROR_MESSAGE"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
        mDisposables.add(fetchEventList);
    }

    @Override
    public void onStop() {
        mDisposables.clear();
    }
}
