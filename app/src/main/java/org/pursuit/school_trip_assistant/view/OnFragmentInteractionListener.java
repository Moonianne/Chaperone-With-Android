package org.pursuit.school_trip_assistant.view;

import android.support.v4.app.Fragment;

import org.pursuit.school_trip_assistant.model.Student;

public interface OnFragmentInteractionListener {
    void addStudentToDatabase(Student student, Fragment fragment);

    void finishSplashScreen(Fragment fragment);
}
