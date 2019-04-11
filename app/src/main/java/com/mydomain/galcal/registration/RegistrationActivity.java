package com.mydomain.galcal.registration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mydomain.galcal.BaseContract;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        mPresenter = new RegistrationPresenter(this);
        mPresenter.onStart();
        mEditTextEmail = (EditText) findViewById(R.id.login_registration);
        mEditTextRepeatEmail = (EditText) findViewById(R.id.login_registration_repeat);
        mEditTextPassword = (EditText) findViewById(R.id.registration_password);
        mEditTextRepeatPassword = (EditText) findViewById(R.id.password_registration_repeat);
        mSingUpButton = (Button) findViewById(R.id.sign_up_button_registration);
        mLoginButton = (Button) findViewById(R.id.login_button_registration);

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

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
