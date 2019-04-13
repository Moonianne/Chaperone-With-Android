package org.pursuit.school_trip_assistant.view.fragment.input_trip_details;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import org.pursuit.school_trip_assistant.view.OnFragmentInteractionListener;

import java.util.Calendar;


public final class DatePickerFragment extends DialogFragment
  implements DatePickerDialog.OnDateSetListener {

  private final Calendar calendar;
  private OnFragmentInteractionListener onFragmentInteractionListener;
  private OnDatePickListener onDatePickListener;

  public DatePickerFragment() {
    calendar = Calendar.getInstance();
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
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    return new DatePickerDialog(getActivity(), this, year, month, day);
  }

  @Override
  public void onDateSet(DatePicker view, int year, int month, int day) {
    onFragmentInteractionListener.setDate(year, month, day);
    onDatePickListener.onDatePick();
  }

  public void setOnDatePickListener(OnDatePickListener listener) {
    this.onDatePickListener = listener;
  }

  interface OnDatePickListener {
    void onDatePick();
  }
}
