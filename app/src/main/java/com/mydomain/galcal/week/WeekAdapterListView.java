package com.mydomain.galcal.week;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mydomain.galcal.R;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.data.DayOfWeekEventData;

import java.util.ArrayList;

/**
 * Created by Nikita on 23.01.2019.
 */

public class WeekAdapterListView extends BaseAdapter{

    //private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<DayOfWeekEventData> mList;
    private WeekFragment mFragment;
    public WeekAdapterListView(WeekFragment fragment){


        mList = new ArrayList<DayOfWeekEventData>();
        mFragment = fragment;

        //mFragment.fetchNewEvents();
        // mFragment.fetchNewEvents(mLastDate);
        //mList.add(new DayOfWeekEventData("123", new ArrayList<DayEventData>()));
        //mList.add(new DayOfWeekEventData("133", new ArrayList<DayEventData>()));
        //mList.add(new DayOfWeekEventData("143", new ArrayList<DayEventData>()));


    }

    public void addEvents(ArrayList<DayOfWeekEventData> list) {
        mList.addAll(list);
        //mEvents.addAll(events);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View v, ViewGroup viewGroup) {
        View view = v;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.event_item_in_week_fragment, viewGroup, false);
        }

        TextView textViewDate;
        TextView textViewEvent1;
        TextView textViewEvent2;
        TextView textViewEvent3;
        TextView textViewShowAllEvents;

        textViewDate = (TextView) view.findViewById(R.id.date);
        textViewEvent1 = (TextView) view.findViewById(R.id.event1_title);
        textViewEvent2 = (TextView) view.findViewById(R.id.event2);
        textViewEvent3 = (TextView) view.findViewById(R.id.event3);
        textViewShowAllEvents = (TextView) view.findViewById(R.id.see_all_events);

        textViewDate.setText(mList.get(i).date);
        Log.d("TAG5", "Pos: "+i+", "+mList.get(i).events.size());

        if (mList.get(i).events.size() >= 1) {
            textViewEvent1.setText(mList.get(i).events.get(0).title);

        }
        if (mList.get(i).events.size() >= 2) {
            textViewEvent2.setText(mList.get(i).events.get(1).title);

        }
        if (mList.get(i).events.size() >= 3) {
            textViewEvent3.setText(mList.get(i).events.get(2).title);

        }
        if (mList.get(i).events.size() > 3) {
            textViewShowAllEvents.setVisibility(View.VISIBLE);
        }




        //Log.d("Queue", "= " + position);
        /*if(position==mList.size()-14) {
            mFragment.fetchNewEvents();
        }*/
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mFragment.getContext(), mList.get(position).date, Toast.LENGTH_SHORT).show();
                mFragment.openHomeTab(mList.get(i));

            }
        });

        return view;
    }
}
