package org.pursuit.school_trip_assistant.view.fragment.InputTripDetails;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;

import org.jetbrains.annotations.NotNull;
import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.view.OnFragmentInteractionListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public final class TripInputFragment extends Fragment
        implements TimePickerFragment.OnTimePickListener {
    private static final String SHARED_PREFS = "ASSISTANT";
    private static final String START_PREFS = "START_TIME";
    private static final String END_PREFS = "END_TIME";
    private static final String TAG = "TripInputFrag.TAG";

    private OnFragmentInteractionListener onFragmentInteractionListener;
    private SharedPreferences sharedPreferences;
    private TextView startTime;
    private TextView endTime;

    public static TripInputFragment newInstance() {
        return new TripInputFragment();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null)
            sharedPreferences = getActivity().getSharedPreferences(
                    SHARED_PREFS, Context.MODE_PRIVATE);
        startTime = view.findViewById(R.id.clickable_start_time);
        endTime = view.findViewById(R.id.clickable_end_time);
        Disposable disposableStart = getSubscribe(startTime);
        Disposable disposableEnd = getSubscribe(endTime);
    }

    @Override
    public void setTimeView() {
        startTime.setText(sharedPreferences.getString(START_PREFS, "12:00 AM"));
        endTime.setText(sharedPreferences.getString(END_PREFS, "12:00 AM"));
    }

    @NotNull
    private Disposable getSubscribe(TextView textView) {
        return RxView.clicks(textView)
                .observeOn(Schedulers.io())
                .debounce(300L, TimeUnit.MILLISECONDS)
                .map(click -> textView.getId())
                .map(this::getTimePicker)
                .doOnNext(timePickerFragment -> timePickerFragment.setOnTimePickListener(this))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(timePickerFragment -> {
                    if (getFragmentManager() != null) {
                        timePickerFragment.show(getFragmentManager(), TAG);
                    }
                });
    }

    private TimePickerFragment getTimePicker(Integer iD) {
        switch (iD) {
            case R.id.clickable_start_time:
                return new TimePickerFragment.StartTimePicker();
            case R.id.clickable_end_time:
                return new TimePickerFragment.EndTimePicker();
            default:
                throw new RuntimeException("View ID not found.");
        }
    }
}
