package org.pursuit.school_trip_assistant.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.squareup.picasso.Picasso;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.view.OnFragmentInteractionListener;

import io.reactivex.disposables.Disposable;

public final class DisplayStudentFragment extends Fragment {
    private static final String ID_KEY = "Display.ID";

    private OnFragmentInteractionListener onFragmentInteractionListener;
    private Disposable disposable;

    public static DisplayStudentFragment newInstance(int iD) {
        DisplayStudentFragment displayStudentFragment = new DisplayStudentFragment();
        Bundle args = new Bundle();
        args.putInt(ID_KEY, iD);
        displayStudentFragment.setArguments(args);
        return displayStudentFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Must Implement OnFragmentInteractionListener in Host Activity.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int iD = getArguments().getInt(ID_KEY);
        MaterialButton materialButton = view.findViewById(R.id.edit_button);
        disposable = RxView.clicks(materialButton)
          .subscribe(click -> onFragmentInteractionListener.editStudent(iD));
        view.<TextView>findViewById(R.id.display_text_name)
          .setText(getString(R.string.student_name,
            onFragmentInteractionListener.getStudentFullName(iD)));
        view.<TextView>findViewById(R.id.display_e_phone)
          .setText(getString(R.string.emergency_contact,
            onFragmentInteractionListener.getEmergencyContact(iD)));
        Picasso.get()
          .load(onFragmentInteractionListener.getStudentImage(iD))
          .into(view.<ImageView>findViewById(R.id.display_image));
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}