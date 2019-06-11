package com.mydomain.galcal.APIUtils;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mydomain.galcal.data.AddEventData;
import com.mydomain.galcal.data.BackgroundImageInfo;
import com.mydomain.galcal.data.ChangeEmailData;
import com.mydomain.galcal.data.ChangePasswordData;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.data.SendRestoreLinkData;
import com.mydomain.galcal.data.SetNewPasswordData;
import com.mydomain.galcal.data.UserData;
import com.mydomain.galcal.data.AuthorizationResponse;
import com.mydomain.galcal.data.new_data.Day;
import com.mydomain.galcal.data.new_data.Event;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nikita on 16.01.2019.
 */

public class APIUtils {
    public static final String BASE_URL = "https://galcal.sooprit.com/api/v1/";
    //public static final String EXP_URL = "http://192.168.1.182:8080/api/v1/";

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


    public Single<Map<String, ArrayList<DayEventData>>> getAllEvents(String token, String startTime, String endTime){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getAllEvents("Bearer " + token, startTime, endTime);
    }


    public Completable createNewEvent(String token, AddEventData data){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.createNewEvent("Bearer " + token, data);
    }

    public Completable deleteEvent(String token, String id){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.deleteEvent("Bearer " + token, id);
    }

    public Completable editEvent(String token, String id, AddEventData data){ //не пашет null в remind_time
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.editEvent("Bearer " + token, id, data);
    }

    public Single<ArrayList<BackgroundImageInfo>> getBackgroundImageInfo(String token, String startTime){
        Retrofit retrofit = getClient(BASE_URL);
        Log.d("BEARER", startTime);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getBackgroundImageInfo("Bearer " + token, startTime);
    }

    public Completable sendConfirm(String key){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.sendConfirm(key);
    }

    public Single<ResponseBody> getNewbieImage(String token){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getNewbieImage("Bearer " + token);
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
