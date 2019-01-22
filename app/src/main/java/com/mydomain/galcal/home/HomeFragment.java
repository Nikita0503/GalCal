package com.mydomain.galcal.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.R;
import com.mydomain.galcal.data.BackgroundImageInfo;
import com.mydomain.galcal.settings.SettingsPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nikita on 17.01.2019.
 */

public class HomeFragment extends Fragment implements BaseContract.BaseView{

    private String mToken;
    private String mUserName;
    private HomePresenter mPresenter;

    private TextView mTextViewUserName;
    private TextView mTextViewEventsCount;
    private TextView mTextViewDate;
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
        mTextViewUserName.setText(getResources().getString(R.string.hello) + " " + mUserName);
        mTextViewEventsCount = (TextView) view.findViewById(R.id.events_count);
        mTextViewDate = (TextView) view.findViewById(R.id.date);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mPresenter.fetchTodayEventList(mToken);
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


    public void setEventsCountTodayToTextView(SpannableString text){
        mTextViewEventsCount.setVisibility(View.VISIBLE);
        mTextViewEventsCount.setText(text);
    }

    public void setDateToTextView(String date){
        mTextViewDate.setText(date);
    }

    public void setAdapter(HomeEventsListAdapter adapter){
        mRecyclerView.setAdapter(adapter);
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
