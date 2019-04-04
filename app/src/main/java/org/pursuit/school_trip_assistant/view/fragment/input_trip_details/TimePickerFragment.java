package org.pursuit.school_trip_assistant.view.fragment.input_trip_details;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public abstract class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private static final String SHARED_PREFS = "ASSISTANT";
    private static final String TIME_PREFS = "TIK_TOK";
    private static final String START_PREFS = "START_TIME";
    private static final String END_PREFS = "END_TIME";

    private OnTimePickListener listener;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int hourOfDayTwelveHour = (hourOfDay % 12) == 0 ? 12 : (hourOfDay % 12);
        String time = hourOfDayTwelveHour + ":" +
                ((minute < 10) ? "0" + minute : minute) +
                ((hourOfDay >= 12) ? " PM" : " AM");
        setTime(time, TIME_PREFS);
        listener.setTimeView();
    }

    public void setTime(String time, String timeKey) {
        if (getActivity() == null)
            throw new NullPointerException("Invoked Method on Null Activity.");
        getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(timeKey, time)
                .apply();
    }

    public void setOnTimePickListener(OnTimePickListener onTimePickerListener) {
        this.listener = onTimePickerListener;
    }

    public static final class StartTimePicker extends TimePickerFragment {

        @Override
        public void setTime(String time, String timeKey) {
            super.setTime(time, START_PREFS);
        }
    }

    public static final class EndTimePicker extends TimePickerFragment {

        @NotNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY) + 4;
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void setTime(String time, String timeKey) {
            super.setTime(time, END_PREFS);
        }
    }

    interface OnTimePickListener {
        void setTimeView();
    }
}
