package com.example.krishna.letsshield;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by krishna on 12/9/17.
 */

public class ThirdFragment extends Fragment{
    View view= null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view ==null)
        view = inflater.inflate(R.layout.activity_location_history,container,false);
        return view;
    }

    public static ThirdFragment newInstance()
    {
        ThirdFragment f = new ThirdFragment();
        return f;
    }
}
