package com.mydomain.galcal.calendar;

import com.mydomain.galcal.APIUtils.APIUtils;
import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.data.DayOfWeekEventData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 24.01.2019.
 */

public class CalendarPresenter implements BaseContract.BasePresenter {

    private String mToken;
    private CalendarFragment mFragment;
    private CompositeDisposable mDisposables;
    private APIUtils mApiUtils;

    public CalendarPresenter(CalendarFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public void onStart() {
        mDisposables = new CompositeDisposable();
        mApiUtils = new APIUtils();
    }

    public void fetchEvents(){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(calendar1.get(Calendar.YEAR)+1, calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        String startTime = oldDateFormat.format(calendar1.getTime());
        String endTime = oldDateFormat.format(calendar2.getTime());
        Disposable yearEvents = mApiUtils.getTodayEventList(mToken, startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<DayEventData>>() {
                    @Override
                    public void onSuccess(ArrayList<DayEventData> data) {
                        mFragment.setEvents(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposables.add(yearEvents);
    }

    public void setToken(String token){
        mToken = token;
    }

    @Override
    public void onStop() {
        mDisposables.clear();
    }
}
