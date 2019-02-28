package com.mydomain.galcal.editEvent;

import android.view.KeyEvent;
import android.view.View;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mydomain.galcal.APIUtils.APIUtils;
import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.MainActivity;
import com.mydomain.galcal.data.AddEventData;
import com.mydomain.galcal.data.DayEventData;

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

public class EditEventPresenter implements BaseContract.BasePresenter {

    private EditEventFragment mFragment;
    private CompositeDisposable mDisposables;
    private APIUtils mApiUtils;

    public EditEventPresenter(EditEventFragment fragment){
        mFragment = fragment;
    }

    @Override
    public void onStart() {
        mDisposables = new CompositeDisposable();
        mApiUtils = new APIUtils();
    }

    public void deleteEvent(String token, final DayEventData data){
        Disposable disposable = mApiUtils.deleteEvent(token, String.valueOf(data.id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mFragment.showMessage("Event deleted successfully");
                        mFragment.getFragmentManager().popBackStack();
                        MainActivity activity = (MainActivity) mFragment.getActivity();
                        activity.fetchEventsForYear();
                        activity.openWeekTab();
                        try {
                            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
                            Date date = Calendar.getInstance().getTime();
                            Date date1 = oldFormat.parse(data.startTime);
                            if(newFormat.format(date).equals(newFormat.format(date1))) {
                                activity.updateHomeTab();
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
        mDisposables.add(disposable);
    }

    public void editEvent(String token, String id, final AddEventData data){
        final Disposable disposable = mApiUtils.editEvent(token, id, data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mFragment.showMessage("Event edited successfully");
                        mFragment.getFragmentManager().popBackStack();
                        MainActivity mainActivity = (MainActivity) mFragment.getActivity();
                        mainActivity.fetchEventsForYear();
                        mainActivity.openWeekTab();
                        try {
                            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
                            Date date = Calendar.getInstance().getTime();
                            Date date1 = oldFormat.parse(data.startTime);
                            if(newFormat.format(date).equals(newFormat.format(date1))) {
                                mainActivity.updateHomeTab();
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
        mDisposables.add(disposable);
    }



    @Override
    public void onStop() {
        mDisposables.clear();
    }
}
