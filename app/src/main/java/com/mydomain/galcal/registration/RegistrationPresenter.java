package com.mydomain.galcal.registration;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mydomain.galcal.APIUtils.APIUtils;
import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.R;
import com.mydomain.galcal.data.AuthorizationResponse;
import com.mydomain.galcal.data.ErrorData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Nikita on 16.01.2019.
 */

public class RegistrationPresenter implements BaseContract.BasePresenter {

    private RegistrationActivity mActivity;
    private CompositeDisposable mDisposables;
    private APIUtils mApiUtils;

    public RegistrationPresenter(RegistrationActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onStart() {
        mDisposables = new CompositeDisposable();
        mApiUtils = new APIUtils();
    }

    public void fetchUserRegistrationResponse(String login, String password){
        Disposable userRegistrationResponse = mApiUtils.getUserRegistrationResponse(login, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mActivity.showMessage(mActivity.getResources().getString(R.string.successful_registration));
                        authorization(login, password);
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
                                mActivity.showMessage(errorMessage.getString("ERROR_MESSAGE"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
        mDisposables.add(userRegistrationResponse);
    }

    private void authorization(String login, String password){
        Disposable loginResponse = mApiUtils.getAuthorizationToken(login, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AuthorizationResponse>() {
                    @Override
                    public void onSuccess(AuthorizationResponse response) {
                        Log.d("TAG", response.token);
                        mActivity.showMessage(mActivity.getResources().getString(R.string.welcome));
                        Log.d("123123123", response.first_login_time);
                        String firstLoginTime = response.first_login_time.substring(0, 19);
                        mActivity.openMainActivity(response.token, firstLoginTime);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
        mDisposables.add(loginResponse);
    }


    @Override
    public void onStop() {
        mDisposables.clear();
    }
}
