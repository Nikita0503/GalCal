package com.mydomain.galcal.confirm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mydomain.galcal.APIUtils.APIUtils;
import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.R;

public class ConfirmActivity extends AppCompatActivity implements BaseContract.BaseView{


    private ConfirmPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        mPresenter = new ConfirmPresenter(this);
        mPresenter.onStart();
        final Intent intent = getIntent();
        final String action = intent.getAction();
        final String data = intent.getDataString();
        Log.d("TAG", data);
        String key = "";
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            key = data.split("https://galcal.sooprit.com/confirm-email/")[1];
            Log.d("TAG8", key);
        }
        mPresenter.sendConfirm(key);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }
}
