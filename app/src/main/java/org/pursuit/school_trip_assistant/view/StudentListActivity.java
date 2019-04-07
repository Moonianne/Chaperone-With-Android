package org.pursuit.school_trip_assistant.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.model.Student;
import org.pursuit.school_trip_assistant.view.fragment.DisplayStudentFragment;
import org.pursuit.school_trip_assistant.view.fragment.InputStudentFragment;
import org.pursuit.school_trip_assistant.view.fragment.SplashFragment;
import org.pursuit.school_trip_assistant.view.fragment.input_trip_details.TripInputFragment;
import org.pursuit.school_trip_assistant.view.fragment.recyclerview.DataReceiveListener;
import org.pursuit.school_trip_assistant.view.fragment.recyclerview.StudentListFragment;
import org.pursuit.school_trip_assistant.viewmodel.StudentsViewModel;
import org.pursuit.school_trip_assistant.viewmodel.ViewModelFactory;

public final class StudentListActivity extends AppCompatActivity
  implements OnFragmentInteractionListener {

  private StudentsViewModel testViewModel;
  private DataReceiveListener dataReceiveListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student_list);
    inflateFragment(SplashFragment.newInstance());

    setSupportActionBar(findViewById(R.id.toolbar));

    testViewModel = ViewModelProviders.of(
      this, new ViewModelFactory(this)).get(StudentsViewModel.class);
  }

  @Override
  public void addStudentToDatabase(Student student, Fragment fragment) {
    testViewModel.addStudentToDatabase(student)
      .subscribe(() -> closeFragment(fragment),
        throwable -> {
        });
  }

  @Override
  public void finishSplashScreen(Fragment fragment) {
    closeFragment(fragment);
    inflateFragment(TripInputFragment.newInstance());
  }

  @Override
  public String getStudentFullName(int iD) {
    return testViewModel.getStudentLastNameFirstName(iD);
  }

  @Override
  public String getEmergencyContact(int iD) {
    return testViewModel.getEmergencyContact(iD);
  }

  @Override
  public void showStudentList() {
    StudentListFragment listFragment = StudentListFragment.newInstance();
    dataReceiveListener = (DataReceiveListener) listFragment;
    inflateFragment(listFragment);
    testViewModel.getStudentList().observe(
      this, students -> dataReceiveListener.onNewDataReceived(students));
  }

  @Override
  public void showStudentInformation(int iD) {
    inflateFragment(DisplayStudentFragment.newInstance(iD), true);
  }

  @Override
  public void closeFragment(Fragment fragment) {
    getSupportFragmentManager()
      .beginTransaction()
      .remove(fragment)
      .commit();
  }

  public void showInputFragment() {
    inflateFragment(InputStudentFragment.newInstance(), true);
  }

  private void inflateFragment(Fragment fragment) {
    inflateFragment(fragment, false);
  }

  private void inflateFragment(Fragment fragment, boolean isBackStack) {
    FragmentTransaction fragmentTransaction = getSupportFragmentManager()
      .beginTransaction()
      .replace(R.id.fragment_container, fragment);
    if (isBackStack) fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }
}
