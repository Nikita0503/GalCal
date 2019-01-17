package com.mydomain.galcal.week;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mydomain.galcal.R;

/**
 * Created by Nikita on 17.01.2019.
 */

public class WeekFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.week_fragment, container, false);
    }
}
