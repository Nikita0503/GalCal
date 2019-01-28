package com.mydomain.galcal.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.R;
import com.mydomain.galcal.authorization.AuthorizationActivity;

/**
 * Created by Nikita on 17.01.2019.
 */

public class SettingsFragment extends Fragment implements BaseContract.BaseView{

    private String mToken;
    private SettingsPresenter mPresenter;
    private TextView mTextViewPassword;
    private TextView mTextViewEmail;
    private TextView mTextViewPrivacyPolicy;
    private TextView mTextViewSupport;
    private TextView mTextViewLogout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SettingsPresenter(this);
        mPresenter.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        mTextViewPassword = (TextView) view.findViewById(R.id.password_tv);
        mTextViewEmail = (TextView) view.findViewById(R.id.email_tv);
        mTextViewPrivacyPolicy = (TextView) view.findViewById(R.id.privacy_tv);
        mTextViewSupport = (TextView) view.findViewById(R.id.support_tv);
        mTextViewLogout = (TextView) view.findViewById(R.id.logout);
        mTextViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity().getApplicationContext(), "Password", Toast.LENGTH_SHORT).show();
                Dialog dialog = createChangePasswordDialog();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
                dialog.show();
            }
        });
        mTextViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity().getApplicationContext(), "Email", Toast.LENGTH_SHORT).show();
                Dialog dialog = createChangeEmailDialog();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
                dialog.show();
            }
        });
        mTextViewPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity().getApplicationContext(), "Privacy Policy", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/10-9rasBlul8Zb27tRtFyvzXkkq4R9ZcGDRCAA975GWo/edit#heading=h.pbrwii6q72cs"));
                startActivity(intent);
            }
        });
        mTextViewSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity().getApplicationContext(), "Support", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"galcalapp@gmail.com"});
                try {
                    startActivity(Intent.createChooser(intent, getResources().getString(R.string.send_email)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), getResources().getString(R.string.dont_have_apps_for_email), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mTextViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = SettingsFragment.this.getActivity().getSharedPreferences("GalCal", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("token", "");
                editor.commit();
                String savedText = pref.getString("token", "");
                Log.d("TAG", savedText);
                getActivity().finish();
                Intent intent = new Intent(getContext(), AuthorizationActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void setToken(String token){
        mToken = token;
    }

    private Dialog createChangeEmailDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.change_email_dialog);
        dialog.setTitle(getResources().getString(R.string.change_email_dialog));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
        final EditText editTextEmail = (EditText) dialog.findViewById(R.id.newEmail);
        Button buttonOk = (Button) dialog.findViewById(R.id.buttonOk);
        Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newEmail = editTextEmail.getText().toString();

                if(newEmail.equals("")) {
                    editTextEmail.setError(getResources().getString(R.string.required));
                    return;
                }
                mPresenter.sendRequestForChangeEmail(mToken, newEmail);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    private Dialog createChangePasswordDialog(){

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.change_password_dialog);
        dialog.setTitle(getResources().getString(R.string.change_password_dialog));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
        final EditText editTextOldPassword = (EditText) dialog.findViewById(R.id.oldPassword);
        final EditText editTextNewPassword = (EditText) dialog.findViewById(R.id.newPassword);
        final EditText editTextNewPasswordRepeat = (EditText) dialog.findViewById(R.id.newPasswordRepeat);
        Button buttonOk = (Button) dialog.findViewById(R.id.buttonOk);
        Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = editTextOldPassword.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();
                String newPasswordRepeat = editTextNewPasswordRepeat.getText().toString();
                if(oldPassword.equals("")) {
                    editTextOldPassword.setError(getResources().getString(R.string.required));
                    return;
                }
                if(newPassword.equals("")) {
                    editTextNewPassword.setError(getResources().getString(R.string.required));
                    return;
                }
                if(newPasswordRepeat.equals("")) {
                    editTextNewPasswordRepeat.setError(getResources().getString(R.string.required));
                    return;
                }
                if(!newPassword.equals(newPasswordRepeat)){
                    editTextNewPasswordRepeat.setError(getResources().getString(R.string.passwords_error));
                    return;
                }
                mPresenter.sendRequestForChangePassword(mToken, newPassword);
                dialog.dismiss();
            }
        });
        return dialog;
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }
}
