package com.mydomain.galcal.home;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mydomain.galcal.APIUtils.APIUtils;
import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.R;
import com.mydomain.galcal.data.DayEventData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Nikita on 18.01.2019.
 */

public class HomePresenter implements BaseContract.BasePresenter {

    private String mToken;
    private String mDate;
    private HomeFragment mFragment;
    private CompositeDisposable mDisposables;
    private APIUtils mApiUtils;


    public HomePresenter(HomeFragment fragment, String token) {
        mFragment = fragment;
        mToken = token;
    }

    @Override
    public void onStart() {
        mDisposables = new CompositeDisposable();
        mApiUtils = new APIUtils();
    }

    public void setDate(String date){
        mDate = date;
    }

    public void fetchTodayEventList(String token){
        //String time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(Calendar.getInstance().getTime());
        String time;
        if(mDate==null) {
            time = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        }else{
            time = mDate;
        }
        String startTime = time + "T00:00";
        String endTime = time + "T23:59";
        //Log.d("TAG", startTime);
        //Log.d("TAG", endTime);
        Disposable fetchTodayEventList = mApiUtils.getTodayEventList(token, startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<DayEventData>>() {
                    @Override
                    public void onSuccess(ArrayList<DayEventData> data) {
                        setEventsCount(data.size());
                        setDate();
                        createAdapter(data);
                        for(int i = 0; i < data.size(); i++){
                            Log.d("TAG", data.get(i).title);
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
                                mFragment.showMessage(errorMessage.getString("ERROR_MESSAGE"));
                                Log.d("TAG", errorMessage.getString("ERROR_MESSAGE"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
        mDisposables.add(fetchTodayEventList);
    }

    private void setEventsCount(int eventsCount){
        String str;
        SpannableString answer;
        if(eventsCount>0) {
            if(eventsCount==1) {
                str = "You have " + eventsCount + " event today";
                answer  = new SpannableString(str);
                answer.setSpan(new ForegroundColorSpan(mFragment.getResources().getColor(R.color.colorAccent)), 9, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else {
                str = "You have " + eventsCount + " events today";
                answer  = new SpannableString(str);
                answer.setSpan(new ForegroundColorSpan(mFragment.getResources().getColor(R.color.colorAccent)), 9, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }else{
            str = mFragment.getResources().getString(R.string.dont_have_events);
            answer  = new SpannableString(str);
        }
        mFragment.setEventsCountTodayToTextView(answer);
    }

    private void setDate(){
        String date = null;
        if(mDate==null){
            date = new SimpleDateFormat("E, MMM d", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        }else{
            try {

                date = new SimpleDateFormat("E, MMM d", Locale.ENGLISH).format(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(mDate));
            }catch (Exception c){

            }
        }
        mFragment.setDateToTextView(date);
    }

    private void createAdapter(ArrayList<DayEventData> data){
        HomeEventsListAdapter adapter = new HomeEventsListAdapter(mToken, data, mFragment);
        mFragment.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        mDisposables.clear();
    }
}
