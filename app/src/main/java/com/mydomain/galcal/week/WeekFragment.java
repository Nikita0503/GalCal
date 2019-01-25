package com.mydomain.galcal.week;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mydomain.galcal.BaseContract;
import com.mydomain.galcal.MainActivity;
import com.mydomain.galcal.R;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.data.DayOfWeekEventData;
import com.mydomain.galcal.editEvent.EditEventPresenter;
import com.mydomain.galcal.home.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nikita on 17.01.2019.
 */

public class WeekFragment extends Fragment implements BaseContract.BaseView{

    private String mToken;
    private ArrayList<DayEventData> mEvents;
    private WeekPresenter mPresenter;
    private RecyclerView mRecyclerView;
    private WeekAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new WeekPresenter(this);
        mPresenter.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.week_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new WeekAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        //mPresenter.setToken(mToken);
        Log.d("TAG", "onCreateView");
        fetchNewEvents();
        //Log.d("TAG3", mToken);
        return view;
    }

    public void setToken(String token){
        mToken = token;
        Log.d("TAG", "setToken()");
        //Log.d("TAG3", mToken);
    }

    public void setEvents(ArrayList<DayEventData> events){
        mEvents = events;
    }

    public void fetchNewEvents(){
        mPresenter.fetchEventsByDates(mEvents);
    }

    public void addNewEvents(ArrayList<DayOfWeekEventData> events){
        mAdapter.addEvents(events);

    }

    public void openHomeTab(String dateStr){
        try {

            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.openHomeTab();
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setToken(mToken);
            SimpleDateFormat oldFormat = new SimpleDateFormat("E d MMM", Locale.ENGLISH);
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = oldFormat.parse(dateStr.replaceAll("\n", " "));
            Calendar calendar2 = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(calendar2.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            Log.d("TAG123", newFormat.format(calendar.getTime()));
            homeFragment.setDate(newFormat.format(calendar.getTime()));
            //homeFragment.setEventData(mList.get(position));
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_container, homeFragment);
            //0transaction.addToBackStack(null);
            transaction.commit();
        }catch (Exception c){
            c.printStackTrace();
        }
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
