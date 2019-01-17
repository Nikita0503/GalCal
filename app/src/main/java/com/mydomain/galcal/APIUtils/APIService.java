package com.mydomain.galcal.APIUtils;

import com.mydomain.galcal.data.SendRestoreLinkData;
import com.mydomain.galcal.data.SetNewPasswordData;
import com.mydomain.galcal.data.UserData;
import com.mydomain.galcal.data.AuthorizationResponse;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
    Completable sendRequestToResetPassword(@Body SetNewPasswordData data);
}
