package com.mydomain.galcal;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mydomain.galcal.APIUtils.APIUtils;
import com.mydomain.galcal.data.BackgroundImageInfo;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.settings.SettingsFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
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

    public void fetchBackgroundImageInfo(String token){
        String date = LocalDate.now().toString();
        Log.d("LOCALDATE", "now " + date);
        Log.d("TAG", date);
        Disposable backgroundImageInfo = mApiUtils.getBackgroundImageInfo(token, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<BackgroundImageInfo>>() {
                    @Override
                    public void onSuccess(ArrayList<BackgroundImageInfo> info) {
                        mActivity.setBackgroundInfo(info.get(0));
                        mActivity.setBackgroundImage(info.get(0).image);
                        Log.d("TAG_DELAY", info.get(0).image);
                        //Toast.makeText(mActivity.getApplicationContext(), date, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });


        mDisposables.add(backgroundImageInfo);
    }

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
