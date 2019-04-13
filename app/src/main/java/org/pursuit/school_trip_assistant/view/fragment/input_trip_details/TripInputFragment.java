package org.pursuit.school_trip_assistant.view.fragment.input_trip_details;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.constants.TripPreference;
import org.pursuit.school_trip_assistant.view.OnFragmentInteractionListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public final class TripInputFragment extends Fragment
  implements TimePickerFragment.OnTimePickListener,
  DatePickerFragment.OnDatePickListener {

  private static final String TAG = "TripInputFrag.TAG";
  private static final long DEBOUNCE = 300;

  private OnFragmentInteractionListener onFragmentInteractionListener;
  private SharedPreferences sharedPreferences;
  private CompositeDisposable disposables = new CompositeDisposable();
  private String currentDate;
  private EditText editTrip;
  private TextView dateSelect;
  private TextView startTime;
  private TextView endTime;
  private MaterialButton doneButton;
  private ObservableTransformer<Integer, TimePickerFragment> timeViewIdToFragment =
    timeViewIds -> timeViewIds
      .observeOn(Schedulers.io())
      .debounce(DEBOUNCE, TimeUnit.MILLISECONDS)
      .map(this::getTimePicker)
      .doOnNext(timePickerFragment -> timePickerFragment.setOnTimePickListener(TripInputFragment.this));

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
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
    currentDate = simpleDateFormat.format(date);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_trip_input, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (getActivity() != null) {
      sharedPreferences = getActivity().getSharedPreferences(
        TripPreference.SHARED_PREFS, Context.MODE_PRIVATE);
    }
    editTrip = view.findViewById(R.id.edit_destination);
    dateSelect = view.findViewById(R.id.date_text);
    dateSelect.setText(currentDate);
    startTime = view.findViewById(R.id.clickable_start_time);
    endTime = view.findViewById(R.id.clickable_end_time);
    doneButton = view.findViewById(R.id.click_done);
    disposables = new CompositeDisposable();
    setupViewStreams();
  }

  @Override
  public void onDetach() {
    disposables.dispose();
    onFragmentInteractionListener = null;
    super.onDetach();
  }

  @Override
  public void onTimePick(String timePrefs) {
    String timeInput = onFragmentInteractionListener.getTime(timePrefs);
    if (timePrefs.equals(TripPreference.END_PREFS_TIME)) {
      endTime.setText(timeInput);
    } else {
      startTime.setText(timeInput);
    }
  }

  @Override
  public void onDatePick() {
    dateSelect.setText(onFragmentInteractionListener.getDate());
  }

  private void setupViewStreams() {
    disposables.add(
      RxView.clicks(dateSelect)
        .observeOn(Schedulers.io())
        .debounce(DEBOUNCE, TimeUnit.MILLISECONDS)
        .map(click -> new DatePickerFragment())
        .doOnNext(datePickerFragment2 -> datePickerFragment2.setOnDatePickListener(this))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::showDatePicker)
    );

    disposables.add(
      RxView.clicks(startTime)
        .map(click -> startTime.getId())
        .compose(timeViewIdToFragment)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::showTimePicker)
    );

    disposables.add(
      RxView.clicks(endTime)
        .map(click -> endTime.getId())
        .compose(timeViewIdToFragment)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::showTimePicker)
    );

    disposables.add(
      RxView.clicks(doneButton)
        .debounce(DEBOUNCE, TimeUnit.MILLISECONDS)
        .map(click -> editTrip.getText().toString())
        .subscribe(destination -> {
          sharedPreferences.edit()
            .putString(TripPreference.DEST_PREFS, destination)
            .apply();
          onFragmentInteractionListener.scheduleDataDeletion();
          onFragmentInteractionListener.showStudentList();
        })
    );
  }

  private void showTimePicker(TimePickerFragment timePickerFragment) {
    if (getFragmentManager() != null)
      timePickerFragment.show(getFragmentManager(), TAG);
  }

  private void showDatePicker(DatePickerFragment datePickerFragment) {
    if (getFragmentManager() != null)
      datePickerFragment.show(getFragmentManager(), TAG);
  }

  private TimePickerFragment getTimePicker(Integer iD) {
    String timePrefs;
    int hourOffSet;
    switch (iD) {
      case R.id.clickable_start_time:
        timePrefs = TripPreference.START_PREFS_TIME;
        hourOffSet = 0;
        break;
      case R.id.clickable_end_time:
        timePrefs = TripPreference.END_PREFS_TIME;
        hourOffSet = 4;
        break;
      default:
        throw new RuntimeException("View ID not found.");
    }
    return TimePickerFragment.newInstance(timePrefs, hourOffSet);
  }
}