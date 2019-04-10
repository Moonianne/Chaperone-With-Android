package org.pursuit.school_trip_assistant.view;

import android.support.v4.app.Fragment;

import org.pursuit.school_trip_assistant.model.Student;

import java.io.File;

public interface OnFragmentInteractionListener {
  void addStudentToDatabase(Student student, Fragment fragment);

  void finishSplashScreen(Fragment fragment);

  void showInputFragment();

  void showCameraFragment();

  void closeFragment(Fragment fragment);

  String getStudentFullName(int iD);

  String getEmergencyContact(int iD);

  File getStudentImage(int iD);

  void showStudentList();

  void showStudentInformation(int iD);
}
