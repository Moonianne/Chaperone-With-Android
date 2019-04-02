package org.pursuit.school_trip_assistant.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.view.OnFragmentInteractionListener;

public class TripInputFragment extends Fragment {
    private OnFragmentInteractionListener onFragmentInteractionListener;

    public static TripInputFragment newInstance() {
        return new TripInputFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_input, container, false);
    }

}
