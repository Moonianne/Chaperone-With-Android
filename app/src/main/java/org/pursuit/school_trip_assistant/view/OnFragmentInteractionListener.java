package org.pursuit.school_trip_assistant.view;

import android.support.v4.app.Fragment;

import org.pursuit.school_trip_assistant.model.Student;

import java.io.File;

public interface OnFragmentInteractionListener {
  void addStudentToDatabase(Student student, Fragment fragment);

  void finishSplashScreen(Fragment fragment);

  void showStudentInputFragment();

  void showStudentInputFragment(Student student);

  void showCameraFragment();

  void closeFragment(Fragment fragment);

  String getStudentFullName(int iD);

  String getEmergencyContact(int iD);

  File getStudentImage(int iD);

  void showStudentList();

  void showStudentInformation(int iD);

  void setTime(String prefs, int hourOfDay, int minute);

  void setDate(int year, int month, int day);

  String getDate();

  String getTime(String timePrefs);

  void editStudent(int iD);

  void scheduleDataDeletion();
}