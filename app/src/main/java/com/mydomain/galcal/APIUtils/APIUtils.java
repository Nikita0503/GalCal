package com.mydomain.galcal.APIUtils;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mydomain.galcal.data.BackgroundImageInfo;
import com.mydomain.galcal.data.ChangeEmailData;
import com.mydomain.galcal.data.ChangePasswordData;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.data.SendRestoreLinkData;
import com.mydomain.galcal.data.SetNewPasswordData;
import com.mydomain.galcal.data.UserData;
import com.mydomain.galcal.data.AuthorizationResponse;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nikita on 16.01.2019.
 */

public class APIUtils {
    public static final String BASE_URL = "https://galcal.sooprit.com/api/v1/";

    public Single<AuthorizationResponse> getAuthorizationToken(String login, String password){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getAuthorizationToken(new UserData(login, password));
    }

    public Completable getUserRegistrationResponse(String login, String password){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getUserRegistrationResponse(new UserData(login, password));
    }

    public Completable sendRequestForRestoreLink(String login){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        Log.d("TAG", login);
        return apiService.sendRequestForRestoreLink(new SendRestoreLinkData(login));
    }

    public Completable sendRequestForRestorePassword(String data, String password){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.sendRequestForResetPassword(new SetNewPasswordData(data, password));
    }

    public Completable sendRequestForChangePassword(String token, String password){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.sendRequestForChangePassword("Bearer " + token, new ChangePasswordData(password));
    }

    public Completable sendRequestForChangeEmail(String token, String email){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.sendRequestForChangeEmail("Bearer " + token, new ChangeEmailData(email));
    }

    public Single<ArrayList<DayEventData>> getTodayEventList(String token, String startTime, String endTime){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getTodayEventList("Bearer " + token, startTime, endTime);
    }


    public static Retrofit getClient(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }
}
