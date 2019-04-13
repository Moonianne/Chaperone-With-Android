package org.pursuit.school_trip_assistant.viewmodel;

import android.arch.lifecycle.ViewModel;

import org.pursuit.school_trip_assistant.model.Trip;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class TripViewModel extends ViewModel {

  private final Trip trip = Trip.getInstance();
  private final Calendar calendar = Calendar.getInstance();

  public void setDate(int year, int month, int day) {
    trip.setYear(year);
    trip.setMonth(month);
    trip.setDayOfMonth(day);
  }

  public void setStartTime(int hourOfDay, int minute) {
    trip.setStartHourOfDay(hourOfDay);
    trip.setStartMinute(minute);
  }

  public void setEndTime(int hourOfDay, int minute) {
    trip.setEndHourOfDay(hourOfDay);
    trip.setEndMinute(minute);
  }

  public long getTripStartTimeInMillis() {
    setDateToModel();
    calendar.set(Calendar.HOUR_OF_DAY, trip.getStartHourOfDay());
    calendar.set(Calendar.MINUTE, trip.getStartMinute());
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTimeInMillis();
  }

  public long getTripEndTimeInMillis() {
    setDateToModel();
    calendar.set(Calendar.HOUR_OF_DAY, trip.getEndHourOfDay());
    calendar.set(Calendar.MINUTE, trip.getEndMinute());
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    return calendar.getTimeInMillis();
  }

  public String getDatePresentable() {
    setDateToModel();
    return new
      SimpleDateFormat("EEE, MMM d, yyyy", Locale.US)
      .format(new Date(calendar.getTimeInMillis()));
  }

  public String getEndTimePresentable() {
    return (((trip.getEndHourOfDay() % 12) == 0) ? 12 : (trip.getEndHourOfDay() % 12)) + ":"
      + ((trip.getEndMinute() < 10) ? "0" + trip.getEndMinute() : trip.getEndMinute())
      + ((trip.getEndHourOfDay() < 12) ? " AM" : " PM");
  }

  public String getStartTimePresentable() {
    return (((trip.getStartHourOfDay() % 12) == 0) ? 12 : (trip.getStartHourOfDay() % 12)) + ":"
      + ((trip.getStartMinute() < 10) ? "0" + trip.getStartMinute() : trip.getStartMinute())
      + ((trip.getStartHourOfDay() < 12) ? " AM" : " PM");
  }

  private void setDateToModel() {
    if (trip.getYear() != 0) {
      calendar.set(Calendar.YEAR, trip.getYear());
      calendar.set(Calendar.MONTH, trip.getMonth());
      calendar.set(Calendar.DAY_OF_MONTH, trip.getDayOfMonth());
    }
  }
}
