package org.pursuit.school_trip_assistant.model;

public final class Trip {
  private static Trip instance;

  private int year;
  private int month;
  private int dayOfMonth;
  private int startHourOfDay;
  private int startMinute;
  private int endHourOfDay;
  private int endMinute;

  private Trip() {
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getDayOfMonth() {
    return dayOfMonth;
  }

  public void setDayOfMonth(int dayOfMonth) {
    this.dayOfMonth = dayOfMonth;
  }

  public int getStartHourOfDay() {
    return startHourOfDay;
  }

  public void setStartHourOfDay(int startHourOfDay) {
    this.startHourOfDay = startHourOfDay;
  }

  public int getStartMinute() {
    return startMinute;
  }

  public void setStartMinute(int startMinute) {
    this.startMinute = startMinute;
  }

  public int getEndHourOfDay() {
    return endHourOfDay;
  }

  public void setEndHourOfDay(int endHourOfDay) {
    this.endHourOfDay = endHourOfDay;
  }

  public int getEndMinute() {
    return endMinute;
  }

  public void setEndMinute(int endMinute) {
    this.endMinute = endMinute;
  }

  public static Trip getInstance() {
    if (instance == null) {
      instance = new Trip();
    }
    return instance;
  }
}
