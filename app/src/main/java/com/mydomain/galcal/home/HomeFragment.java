package com.mydomain.galcal.home;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.MainActivity;
import com.mydomain.galcal.R;
import com.mydomain.galcal.data.BackgroundImageInfo;
import com.mydomain.galcal.data.HomeTabData;
import com.mydomain.galcal.settings.SettingsPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nikita on 17.01.2019.
 */

public class HomeFragment extends Fragment implements BaseContract.BaseView{

    public HomeTabData homeTabData;
    private String mToken;
    private String mUserName;
    private String mDate;
    private HomePresenter mPresenter;
    private BackgroundImageInfo mInfo;
    private TextView mTextViewUserName;
    private TextView mTextViewEventsCount;
    private TextView mTextViewDate;
    private ImageView mImageView;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new HomePresenter(this, mToken);
        mPresenter.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mTextViewUserName = (TextView) view.findViewById(R.id.name);
        if(mUserName==null){
            SharedPreferences pref = getActivity().getSharedPreferences("GalCal", getActivity().MODE_PRIVATE);
            mUserName = pref.getString("userName", "").split("@")[0];
        }
        mTextViewUserName.setText(getResources().getString(R.string.hello) + " " + mUserName);
        mTextViewEventsCount = (TextView) view.findViewById(R.id.events_count);
        mTextViewDate = (TextView) view.findViewById(R.id.date);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mImageView = (ImageView) view.findViewById(R.id.imageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.info_dialog);
                TextView textViewPhotographer = (TextView) dialog.findViewById(R.id.photographer);
                TextView textViewModel = (TextView) dialog.findViewById(R.id.model);
                ImageView imageViewClose = (ImageView) dialog.findViewById(R.id.close);
                try {
                    textViewPhotographer.setText(mInfo.photographer);
                    textViewModel.setText(mInfo.model);
                }catch (Exception c){

                }
                imageViewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        if(mDate!=null){
            mPresenter.setDate(mDate);
            Log.d("TAGS", mDate);
        }
        if(homeTabData==null) {
            mPresenter.fetchTodayEventList(mToken);
            homeTabData = new HomeTabData();
        }else{
            mTextViewEventsCount.setVisibility(View.VISIBLE);
            mTextViewEventsCount.setText(homeTabData.answer);
            mTextViewDate.setText(homeTabData.date);
            mRecyclerView.setAdapter(homeTabData.adapter);
            Log.d("TAG2", homeTabData.answer.toString());
        }
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(Calendar.getInstance().getTime());
        Log.d("TAG", timeStamp.toString()); //работает
        return view;
    }

    public void setToken(String token){
        mToken = token;
    }

    public void setUserName(String userName){
        mUserName = userName.split("@")[0];
    }

    public void setDate(String date){
        mDate = date;
        Log.d("TAGS", date);
    }

    public void setEventsCountTodayToTextView(SpannableString text){
        mTextViewEventsCount.setVisibility(View.VISIBLE);
        mTextViewEventsCount.setText(text);
        homeTabData.answer = text;
        Log.d("TAG", homeTabData.answer.toString());
    }

    public void setDateToTextView(String date){
        mTextViewDate.setText(date);
        homeTabData.date = date;
        Log.d("TAG", homeTabData.date);
    }

    public void setAdapter(HomeEventsListAdapter adapter){
        mRecyclerView.setAdapter(adapter);
        homeTabData.adapter = adapter;
        Log.d("TAG", "adapter");

    }

    public void setBackgroundImageInfo(BackgroundImageInfo imageInfo){
        mInfo = imageInfo;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mDate!=null) {
            if (getView() == null) {
                return;
            }
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        getFragmentManager().popBackStack();
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.openWeekTab();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }
}
