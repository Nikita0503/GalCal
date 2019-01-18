package com.mydomain.galcal.home;

import com.mydomain.galcal.APIUtils.APIUtils;
import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.settings.SettingsFragment;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Nikita on 18.01.2019.
 */

public class HomePresenter implements BaseContract.BasePresenter {

    private HomeFragment mFragment;
    private CompositeDisposable mDisposables;
    private APIUtils mApiUtils;

    public HomePresenter(HomeFragment fragment) {
        mFragment = fragment;
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
