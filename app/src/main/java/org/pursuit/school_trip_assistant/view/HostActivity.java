package org.pursuit.school_trip_assistant.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.constants.TripPreference;
import org.pursuit.school_trip_assistant.model.Student;
import org.pursuit.school_trip_assistant.view.fragment.DisplayStudentFragment;
import org.pursuit.school_trip_assistant.view.fragment.input_student.CameraFragment;
import org.pursuit.school_trip_assistant.view.fragment.input_student.InputStudentFragment;
import org.pursuit.school_trip_assistant.view.fragment.SplashFragment;
import org.pursuit.school_trip_assistant.view.fragment.input_student.OnPictureTakenListener;
import org.pursuit.school_trip_assistant.view.fragment.input_trip_details.TripInputFragment;
import org.pursuit.school_trip_assistant.view.fragment.recyclerview.DataReceiveListener;
import org.pursuit.school_trip_assistant.view.fragment.recyclerview.StudentListFragment;
import org.pursuit.school_trip_assistant.viewmodel.StudentsViewModel;
import org.pursuit.school_trip_assistant.viewmodel.ViewModelFactory;
import org.pursuit.school_trip_assistant.workers.DeletionWorker;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import io.reactivex.disposables.Disposable;

public final class HostActivity extends AppCompatActivity
  implements OnFragmentInteractionListener, OnPictureTakenListener {

  private static final String TAG = "HostActivity.TAG";

  private File latestImage;
  private StudentsViewModel testViewModel;
  private DataReceiveListener dataReceiveListener;
  private Disposable disposable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_host);

    inflateFragment(SplashFragment.newInstance());
    setSupportActionBar(findViewById(R.id.toolbar));
    testViewModel = ViewModelProviders.of(
      this, new ViewModelFactory(this)).get(StudentsViewModel.class);
  }

  @Override
  protected void onDestroy() {
    if (disposable != null)
      disposable.dispose();
    super.onDestroy();
  }

  @Override
  public void addStudentToDatabase(Student student, Fragment fragment) {
    disposable = testViewModel.addStudentToDatabase(student)
      .subscribe(this::showStudentList,
        throwable -> Log.e(TAG, "addStudentToDatabase: ", throwable));
  }

  @Override
  public void finishSplashScreen(Fragment fragment) {
    closeFragment(fragment);
    if (!getSharedPreferences(
      TripPreference.SHARED_PREFS, MODE_PRIVATE).contains(TripPreference.DEST_PREFS)) {
      inflateFragment(TripInputFragment.newInstance());
    } else {
      showStudentList();
    }
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
  public File getStudentImage(int iD) {
    return testViewModel.getStudentImage(iD);
  }

  @Override
  public void showStudentList() {
    StudentListFragment listFragment = StudentListFragment.newInstance();
    dataReceiveListener = (DataReceiveListener) listFragment;
    inflateFragment(listFragment);
    testViewModel.getStudentList().observe(
      this, students -> dataReceiveListener.onNewDataReceived(students));
    scheduleDataDeletion();
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

  @Override
  public void showCameraFragment() {
    CameraFragment cameraFragment = new CameraFragment();
    cameraFragment.setOnPictureTakenListener(this);
    inflateFragment(cameraFragment, true);
  }

  @Override
  public void onPictureTaken(File file) {
    latestImage = file;
  }

  @Override
  public File getCameraFile() {
    return latestImage;
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

  private void scheduleDataDeletion() {
    SharedPreferences sharedPreferences = getSharedPreferences(TripPreference
      .SHARED_PREFS, MODE_PRIVATE);
    long currentTimeMillis = System.currentTimeMillis();
    long tripEndTimeInMillis = sharedPreferences
      .getLong(TripPreference.END_PREFS_MILLIS, 0);
    Log.d(TAG, "scheduleDataDeletion: " + (tripEndTimeInMillis - currentTimeMillis));
    WorkManager.getInstance().enqueue(new OneTimeWorkRequest.Builder(DeletionWorker.class)
      .setInitialDelay(tripEndTimeInMillis - currentTimeMillis, TimeUnit.MILLISECONDS)
      .build());
  }
}
