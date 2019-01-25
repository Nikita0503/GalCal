package com.mydomain.galcal.resetPassword;

import android.content.Intent;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mydomain.galcal.APIUtils.APIUtils;
import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.R;
import com.mydomain.galcal.authorization.AuthorizationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Nikita on 17.01.2019.
 */

public class ResetPasswordPresenter implements BaseContract.BasePresenter {

    private ResetPasswordActivity mActivity;
    private CompositeDisposable mDisposables;
    private APIUtils mApiUtils;

    public ResetPasswordPresenter(ResetPasswordActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onStart() {
        mDisposables = new CompositeDisposable();
        mApiUtils = new APIUtils();
    }

    public void sendNewPassword(String data, String password){
        Disposable newPassword = mApiUtils.sendRequestForRestorePassword(data, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mActivity.showMessage(mActivity.getResources().getString(R.string.new_password_successful));
                        Intent intent = new Intent(mActivity.getApplicationContext(), AuthorizationActivity.class);
                        mActivity.startActivity(intent);
                        mActivity.finish();
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
        mDisposables.add(newPassword);
    }

    @Override
    public void onStop() {
        mDisposables.clear();
    }
}
