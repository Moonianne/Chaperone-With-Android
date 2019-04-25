package org.pursuit.school_trip_assistant.view.fragment.input_trip_details;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import org.jetbrains.annotations.NotNull;
import org.pursuit.school_trip_assistant.view.OnFragmentInteractionListener;

import java.util.Calendar;

public final class TimePickerFragment extends DialogFragment
  implements TimePickerDialog.OnTimeSetListener {

  private static final String TIME_KEY = "TIME_KEY";
  private static final String OFFSET_KEY = "OFFSET_KEY";

  private OnFragmentInteractionListener onFragmentInteractionListener;
  private OnTimePickListener onTimePickListener;
  private String timePrefs;
  private int hourOffSet;

  public static TimePickerFragment newInstance(String timePref,
                                               int hourOffSet) {
    TimePickerFragment fragment = new TimePickerFragment();
    Bundle args = new Bundle();
    args.putString(TIME_KEY, timePref);
    args.putInt(OFFSET_KEY, hourOffSet);
    fragment.setArguments(args);
    return fragment;
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
    /**
     * this check should go first you cant get arguments if your activity is null
     */
    if (getActivity() == null) {
      throw new NullPointerException("Invoked Method on Null Activity.");
    }
    if (getArguments() != null) {
      Bundle receivedBundle = getArguments();
      timePrefs = receivedBundle.getString(TIME_KEY);
      hourOffSet = receivedBundle.getInt(OFFSET_KEY);
    } else {
      throw new RuntimeException("Did not pass data to new instance.");
    }

  }

  @NotNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    /**
     * If you make your variables final, you should do so throughout the whole app.
     */
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    return getTimePickerDialog(hour + hourOffSet, minute);
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    onFragmentInteractionListener.setTime(timePrefs, hourOfDay, minute);
    onTimePickListener.onTimePick(timePrefs);
  }

  @Override
  public void onDetach() {
    onTimePickListener = null;
    super.onDetach();
  }

  @NotNull
  public Dialog getTimePickerDialog(int hour, int minute) {
    return new TimePickerDialog(getActivity(), this, hour, minute,
      DateFormat.is24HourFormat(getActivity()));
  }

  public void setOnTimePickListener(OnTimePickListener onTimePickerListener) {
    this.onTimePickListener = onTimePickerListener;
  }

  interface OnTimePickListener {
    void onTimePick(String timePrefs);
  }
}
