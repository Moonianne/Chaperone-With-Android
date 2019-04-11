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
import org.pursuit.school_trip_assistant.constants.TripPreference;

import java.util.Calendar;

public final class TimePickerFragment extends DialogFragment
  implements TimePickerDialog.OnTimeSetListener {

  private static final String TIME_KEY = "TIME_KEY";
  private static final String OFFSET_KEY = "OFFSET_KEY";

  private OnTimePickListener listener;
  private String timePrefs;
  private int hourOffSet;

  public static TimePickerFragment newInstance(String timePref, int hourOffSet) {
    TimePickerFragment fragment = new TimePickerFragment();
    Bundle args = new Bundle();
    args.putString(TIME_KEY, timePref);
    args.putInt(OFFSET_KEY, hourOffSet);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    return getTimePickerDialog(hour + hourOffSet, minute);
  }

  @Override
  public void onDetach() {
    listener = null;
    super.onDetach();
  }

  @NotNull
  public Dialog getTimePickerDialog(int hour, int minute) {
    return new TimePickerDialog(getActivity(), this, hour, minute,
      DateFormat.is24HourFormat(getActivity()));
  }

  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    int hourOfDayTwelveHour = (hourOfDay % 12) == 0 ? 12 : (hourOfDay % 12);
    String time = hourOfDayTwelveHour + ":" +
      ((minute < 10) ? "0" + minute : minute) +
      ((hourOfDay >= 12) ? " PM" : " AM");
    setTime(time);
    listener.onTimePick();
  }

  public void setTime(String time) {
    if (getActivity() == null)
      throw new NullPointerException("Invoked Method on Null Activity.");
    getActivity().getSharedPreferences(TripPreference.SHARED_PREFS,
      Context.MODE_PRIVATE)
      .edit()
      .putString(timePrefs, time)
      .apply();
  }

  public void setOnTimePickListener(OnTimePickListener onTimePickerListener) {
    this.listener = onTimePickerListener;
  }

  interface OnTimePickListener {
    void onTimePick();
  }
}
