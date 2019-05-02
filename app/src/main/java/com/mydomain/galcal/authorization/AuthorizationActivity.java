package com.mydomain.galcal.authorization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.MainActivity;
import com.mydomain.galcal.R;
import com.mydomain.galcal.registration.RegistrationActivity;

public class AuthorizationActivity extends AppCompatActivity implements BaseContract.BaseView {

    private AuthorizationPresenter mPresenter;
    private TextView mTextViewForgotPassword;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mLoginButton;
    private Button mSingUpButton;
    private SharedPreferences mPref;
    private FirebaseAnalytics mFBanalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFBanalytics = FirebaseAnalytics.getInstance(this);

        mPref = getSharedPreferences("GalCal", MODE_PRIVATE);
        String token = mPref.getString("token", "");
        Log.d("TOKEN", token);
        String userName = mPref.getString("userName", "");
        if(!token.equals("")){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("userName", userName);
            startActivity(intent);
            finish();
        }
        Log.d("TAG", token);
        setContentView(R.layout.authorization_activity);
        mPresenter = new AuthorizationPresenter(this);
        mPresenter.onStart();
        mTextViewForgotPassword = (TextView) findViewById(R.id.forgot_password);
        mEditTextEmail = (EditText) findViewById(R.id.login);
        mEditTextPassword = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mSingUpButton = (Button) findViewById(R.id.sign_up_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = mEditTextEmail.getText().toString();
                String password = mEditTextPassword.getText().toString();
                //Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT).show();
                if(login.equals("")) {
                    mEditTextEmail.setError(getResources().getString(R.string.required));
                    return;
                }
                if(password.equals("")){
                    mEditTextPassword.setError(getResources().getString(R.string.required));
                    return;
                }
                mPresenter.fetchAuthorizationToken(login, password);
            }
        });

        mSingUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        mTextViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = mEditTextEmail.getText().toString();
                Toast.makeText(getApplicationContext(), "Forgot Password", Toast.LENGTH_SHORT).show();
                if(login.equals("")) {
                    mEditTextEmail.setError(getResources().getString(R.string.required));
                    return;
                }
                mPresenter.sendRequestForRestoreLink(login);
            }
        });
    }

    public void openMainActivity(String token){
        String userName = mEditTextEmail.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, userName);
        mFBanalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("token", token);
        editor.putString("userName", userName);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("userName", userName);
        startActivity(intent);
        finish();
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
