package com.mydomain.galcal.week;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mydomain.galcal.APIUtils.APIUtils;
import com.mydomain.galcal.R;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.data.DayOfWeekEventData;
import com.mydomain.galcal.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Nikita on 23.01.2019.
 */

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {



    private ArrayList<DayOfWeekEventData> mList;
    private ArrayList<DayEventData> mEvents;
    private WeekFragment mFragment;

    public WeekAdapter(WeekFragment fragment){


        mList = new ArrayList<DayOfWeekEventData>();
        mFragment = fragment;
        mEvents = new ArrayList<DayEventData>();
        //mFragment.fetchNewEvents();
       // mFragment.fetchNewEvents(mLastDate);
        //mList.add(new DayOfWeekEventData("123", new ArrayList<DayEventData>()));
        //mList.add(new DayOfWeekEventData("133", new ArrayList<DayEventData>()));
        //mList.add(new DayOfWeekEventData("143", new ArrayList<DayEventData>()));


    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_item_in_week_fragment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textViewDate.setText(mList.get(position).date);
        if(mList.get(position).events.size()==0){
            holder.textViewEvent1.setText("No events");
        }else {
            if (mList.get(position).events.size() >= 1) {
                holder.textViewEvent1.setText(mList.get(position).events.get(0).title);
                if (mList.get(position).events.size() >= 2) {
                    holder.textViewEvent2.setText(mList.get(position).events.get(1).title);
                    if (mList.get(position).events.size() >= 3) {
                        holder.textViewEvent3.setText(mList.get(position).events.get(2).title);
                        if (mList.get(position).events.size() > 3) {
                            holder.textViewShowAllEvents.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }



        Log.d("Queue", "= " + position);
        if(position==mList.size()-14) {
            mFragment.fetchNewEvents();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mFragment.getContext(), mList.get(position).date, Toast.LENGTH_SHORT).show();
                mFragment.openHomeTab(mList.get(position).date);

            }
        });
    }

    public void addEvents(ArrayList<DayOfWeekEventData> list, ArrayList<DayEventData> events){
        mList.addAll(list);
        mEvents.addAll(events);
        notifyDataSetChanged();
        //Log.d("TAG3", mToken);
        /*final ArrayList<String> dates = new ArrayList<String>();
        try {
                final SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
                //final SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                final SimpleDateFormat newFormant = new SimpleDateFormat("E'\n'd'\n'MMM", Locale.ENGLISH);
                int month = mLastDate.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int i = 0; i < month; i++) {
                    Date date = mLastDate.getTime();
                    dates.add(oldFormat.format(date));
                    //String day = newFormant.format(date);
                    //mList.add(new DayOfWeekEventData(day, new ArrayList<DayEventData>()));
                    date.setTime(date.getTime()+86400000);
                    mLastDate.setTime(date);
                }
                Log.d("TAG", month + "");
                /// /while (mLastDate.set;)
            for(int i = 0; i < dates.size(); i++){
                Log.d("TAG", dates.get(i));
            }
            Log.d("TAG2", dates.get(0)+"T00:00");
            Log.d("TAG2", dates.get(dates.size()-1)+"T23:59");
            APIUtils apiUtils = new APIUtils();
            String time = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            String startTime = time + "T00:00";
            String endTime = time + "T23:59";*/

            /*APIUtils apiUtils = new APIUtils();
            Disposable fetchEventList = apiUtils.getTodayEventList(mToken, "2019-01-22T00:00", "2019-01-22T23:59")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<ArrayList<DayEventData>>() {
                        @Override
                        public void onSuccess(ArrayList<DayEventData> data) {

                            for(int i = 0; i < data.size(); i++){
                                Log.d("TAG", data.get(i).title);
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (e instanceof HttpException) {
                                HttpException exception = (HttpException) e;
                                ResponseBody responseBody = exception.response().errorBody();
                                try {
                                    JSONObject responseError = new JSONObject(responseBody.string());
                                    JSONArray arrayError = responseError.getJSONArray("errors");
                                    JSONObject errorMessage = arrayError.getJSONObject(0);
                                    //mFragment.showMessage(errorMessage.getString("ERROR_MESSAGE"));
                                    Log.d("TAG", errorMessage.getString("ERROR_MESSAGE"));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
            mDisposables.add(fetchEventList);*/




            /*Disposable disposable = apiUtils.getEventList(mToken, "2019-01-22T00:00", "2019-02-22T23:59")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<ArrayList<DayEventData>>() {
                        @Override
                        public void onSuccess(ArrayList<DayEventData> data) {
                            SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                            try {
                                for(int i = 0; i < dates.size(); i++){
                                    ArrayList<DayEventData> list = new ArrayList<DayEventData>();
                                    for(int j = 0; j < data.size(); i++){
                                        Date date1 = apiFormat.parse(data.get(j).startTime);
                                        String dateStr = oldFormat.format(date1);
                                        if(dateStr.equals(dates.get(i))){
                                            list.add(data.get(j));
                                        }
                                    }
                                    Date date2 = oldFormat.parse(dates.get(i));
                                    mList.add(new DayOfWeekEventData(newFormant.format(date2), list));
                                }


                                for (int i = 0; i < data.size(); i++) {

                                    Date date1 = apiFormat.parse(data.get(i).startTime);
                                    String strDate = oldFormat.format(date1);

                                    for (int j = 0; j < dates.size(); j++) {
                                        if (strDate.equals(dates.get(j))) {
                                            list.add(data.get(i));
                                        }
                                    }
                                    mList.add(newFormant.);

                                }
                            }catch (Exception c){
                                c.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                    });
            mDisposables.add(disposable);*/


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewDate;
        TextView textViewEvent1;
        TextView textViewEvent2;
        TextView textViewEvent3;
        TextView textViewShowAllEvents;
        public ViewHolder(View itemView){
            super(itemView);
            textViewDate = (TextView) itemView.findViewById(R.id.date);
            textViewEvent1 = (TextView) itemView.findViewById(R.id.event1_title);
            textViewEvent2 = (TextView) itemView.findViewById(R.id.event2);
            textViewEvent3 = (TextView) itemView.findViewById(R.id.event3);
            textViewShowAllEvents = (TextView) itemView.findViewById(R.id.see_all_events);
        }


    }
}
