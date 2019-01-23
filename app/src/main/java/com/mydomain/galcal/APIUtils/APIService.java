package com.mydomain.galcal.APIUtils;

import com.mydomain.galcal.data.AddEventData;
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
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Nikita on 16.01.2019.
 */

public interface APIService {
    @POST("user/login/")
    Single<AuthorizationResponse> getAuthorizationToken(@Body UserData authorizationBody);

    @POST("user/register/")
    Completable getUserRegistrationResponse(@Body UserData authorizationBody);

    @POST("user/send-restore-link/")
    Completable sendRequestForRestoreLink(@Body SendRestoreLinkData email);

    @POST("user/set-new-password/")
    Completable sendRequestForResetPassword(@Body SetNewPasswordData data);

    @POST("user/change_password/")
    Completable sendRequestForChangePassword(@Header("Authorization") String header, @Body ChangePasswordData data);

    @POST("user/change_email/")
    Completable sendRequestForChangeEmail(@Header("Authorization") String header, @Body ChangeEmailData data);

    @GET("event/")
    Single<ArrayList<DayEventData>> getTodayEventList(@Header("Authorization") String header, @Query("start_time") String startTime, @Query("end_time") String endTime);

    @POST("event/")
    Completable createNewEvent(@Header("Authorization") String header, @Body AddEventData data);

    @DELETE("event/{id}/")
    Completable deleteEvent(@Header("Authorization") String header, @Path("id") String id);

    @PUT("event/{id}/")
    Completable editEvent(@Header("Authorization") String header, @Path("id") String id, @Body AddEventData data);

    @GET("background/")
    Single<ArrayList<BackgroundImageInfo>> getBackgroundImageInfo(@Header("Authorization") String header, @Query("start_time") String startTime);


}
