package org.pursuit.school_trip_assistant.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.pursuit.school_trip_assistant.R;

public final class InputStudentFragment extends Fragment {

    public InputStudentFragment() {
    }

    public static InputStudentFragment newInstance() {
        return new InputStudentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.photo_button);
        button.setOnClickListener(v -> takePhoto());
    }

    private void takePhoto() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, CameraFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}