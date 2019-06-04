package com.mydomain.galcal.registration;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.MainActivity;
import com.mydomain.galcal.R;

/**
 * Created by Nikita on 16.01.2019.
 */

public class RegistrationActivity extends AppCompatActivity implements BaseContract.BaseView{

    private RegistrationPresenter mPresenter;
    private EditText mEditTextEmail;
    private EditText mEditTextRepeatEmail;
    private EditText mEditTextPassword;
    private EditText mEditTextRepeatPassword;
    private Button mSingUpButton;
    private Button mLoginButton;
    private CheckBox mCheckBox;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        mPref = getSharedPreferences("GalCal", MODE_PRIVATE);
        mPresenter = new RegistrationPresenter(this);
        mPresenter.onStart();
        mEditTextEmail = (EditText) findViewById(R.id.login_registration);
        mEditTextRepeatEmail = (EditText) findViewById(R.id.login_registration_repeat);
        mEditTextPassword = (EditText) findViewById(R.id.registration_password);
        mEditTextRepeatPassword = (EditText) findViewById(R.id.password_registration_repeat);
        mSingUpButton = (Button) findViewById(R.id.sign_up_button_registration);
        mSingUpButton.setClickable(false);
        mSingUpButton.setEnabled(false);
        mLoginButton = (Button) findViewById(R.id.login_button_registration);
        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mSingUpButton.setClickable(true);
                    mSingUpButton.setEnabled(true);
                }else{
                    mSingUpButton.setClickable(false);
                    mSingUpButton.setEnabled(false);
                }
            }
        });
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog policyDialog = getPolicyDialog();
                    policyDialog.setCancelable(false);
                    policyDialog.show();
            }
        });
        mSingUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = mEditTextEmail.getText().toString();
                String loginRepeat = mEditTextRepeatEmail.getText().toString();
                String password = mEditTextPassword.getText().toString();
                String repeatPassword = mEditTextRepeatPassword.getText().toString();
                //Toast.makeText(getApplicationContext(), "Sing Up", Toast.LENGTH_SHORT).show();
                if(login.equals("")) {
                    mEditTextEmail.setError(getResources().getString(R.string.required));
                    return;
                }
                if(loginRepeat.equals("")){
                    mEditTextRepeatEmail.setError(getResources().getString(R.string.required));
                    return;
                }
                if(password.equals("")) {
                    mEditTextPassword.setError(getResources().getString(R.string.required));
                    return;
                }
                if(repeatPassword.equals("")) {
                    mEditTextRepeatPassword.setError(getResources().getString(R.string.required));
                    return;
                }
                if(!login.equals(loginRepeat)){
                    mEditTextRepeatEmail.setError(getResources().getString(R.string.emails_error));
                    return;
                }
                if(!password.equals(repeatPassword)){
                    mEditTextRepeatPassword.setError(getResources().getString(R.string.passwords_error));
                    return;
                }
                mPresenter.fetchUserRegistrationResponse(login, password);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void openMainActivity(String token, String first_login_time){
        String userName = mEditTextEmail.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, userName);
        //mFBanalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("token", token);
        editor.putString("userName", userName);
        editor.putString("firstLoginTime", first_login_time);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("userName", userName);
        //intent.putExtra("firstLoginTime", first_login_time);
        startActivity(intent);
        finish();
    }

    private Dialog getPolicyDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.policy_dialog);
        //dialog.setTitle(getResources().getString(R.string.change_email_dialog));
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
        Button buttonOk = (Button) dialog.findViewById(R.id.buttonOk);


        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        return dialog;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
