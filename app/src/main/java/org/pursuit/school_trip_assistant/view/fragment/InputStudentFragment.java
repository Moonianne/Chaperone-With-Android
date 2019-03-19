package org.pursuit.school_trip_assistant.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pursuit.school_trip_assistant.R;

public final class InputStudentFragment extends Fragment {

    public InputStudentFragment() {
    }

    public static InputStudentFragment newInstance() {
        return new InputStudentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_student, container, false);
    }
}