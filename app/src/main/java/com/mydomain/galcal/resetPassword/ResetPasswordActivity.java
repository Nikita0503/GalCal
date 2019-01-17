package com.mydomain.galcal.resetPassword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.R;

import org.w3c.dom.Text;

public class ResetPasswordActivity extends AppCompatActivity implements BaseContract.BaseView{

    private String mData;
    private ResetPasswordPresenter mPresenter;
    private EditText mEditTextNewPassword;
    private EditText mEditTextRepeatNewPassword;
    private Button mButtonResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mPresenter = new ResetPasswordPresenter(this);
        mPresenter.onStart();
        mEditTextNewPassword = (EditText) findViewById(R.id.new_password_reset);
        mEditTextRepeatNewPassword = (EditText) findViewById(R.id.password_reset_repeat);
        mButtonResetPassword = (Button) findViewById(R.id.reset_password_button);
        final Intent intent = getIntent();
        final String action = intent.getAction();
        final String data = intent.getDataString();

        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            mData = data.substring(data.lastIndexOf("=") + 1);
            Log.d("TAG", mData);
        }

        mButtonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = mEditTextNewPassword.getText().toString();
                String repeatNewPassword = mEditTextRepeatNewPassword.getText().toString();
                if(newPassword.equals("")) {
                    mEditTextNewPassword.setError(getResources().getString(R.string.required));
                    return;
                }
                if(repeatNewPassword.equals("")){
                    mEditTextRepeatNewPassword.setError(getResources().getString(R.string.required));
                    return;
                }
                if(!newPassword.equals(repeatNewPassword)){
                    mEditTextRepeatNewPassword.setError(getResources().getString(R.string.passwords_error));
                    return;
                }
                mPresenter.sendNewPassword(mData, newPassword);
            }
        });
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onStop();
    }
}
