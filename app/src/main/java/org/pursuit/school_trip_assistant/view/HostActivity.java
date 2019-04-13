package org.pursuit.school_trip_assistant.view;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
import org.pursuit.school_trip_assistant.viewmodel.TripViewModel;
import org.pursuit.school_trip_assistant.viewmodel.ViewModelFactory;
import org.pursuit.school_trip_assistant.workers.DeletionWorker;

import java.io.File;
import java.util.concurrent.TimeUnit;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import io.reactivex.disposables.Disposable;

public final class HostActivity extends AppCompatActivity
  implements OnFragmentInteractionListener, OnPictureTakenListener {

  private static final String TAG = "HostActivity.TAG";

  private SharedPreferences sharedPreferences;
  private File latestImage;
  private StudentsViewModel studentViewModel;
  private TripViewModel tripViewModel;
  private DataReceiveListener dataReceiveListener;
  private Disposable disposable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_host);

    sharedPreferences = getSharedPreferences(TripPreference.SHARED_PREFS, MODE_PRIVATE);
    inflateFragment(SplashFragment.newInstance());
    studentViewModel = ViewModelProviders.of(
      this, new ViewModelFactory(this)).get(StudentsViewModel.class);
    tripViewModel = ViewModelProviders.of(this).get(TripViewModel.class);

    Toolbar toolBar = findViewById(R.id.toolbar);
    setSupportActionBar(toolBar);
    ActionBar actionBar = getSupportActionBar();
  }

  @Override
  protected void onDestroy() {
    if (disposable != null)
      disposable.dispose();
    super.onDestroy();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.toolbar_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.tool_about:
        getAboutDialog().show();
        break;
    }
    return true;
  }

  @Override
  public void addStudentToDatabase(Student student, Fragment fragment) {
    disposable = studentViewModel.addStudentToDatabase(student)
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
    return studentViewModel.getStudentLastNameFirstName(iD);
  }

  @Override
  public String getEmergencyContact(int iD) {
    return studentViewModel.getEmergencyContact(iD);
  }

  @Override
  public File getStudentImage(int iD) {
    return studentViewModel.getStudentImage(iD);
  }

  @Override
  public void showStudentList() {
    StudentListFragment listFragment = StudentListFragment.newInstance();
    dataReceiveListener = (DataReceiveListener) listFragment;
    inflateFragment(listFragment);
    studentViewModel.getStudentList().observe(
      this, students -> dataReceiveListener.onNewDataReceived(students));
    scheduleDataDeletion();
  }

  @Override
  public void showStudentInformation(int iD) {
    inflateFragment(DisplayStudentFragment.newInstance(iD), true);
  }

  @Override
  public void setTime(String prefs, int hourOfDay, int minute) {
    switch (prefs) {
      case TripPreference.START_PREFS_TIME:
        tripViewModel.setStartTime(hourOfDay, minute);
        sharedPreferences.edit().putLong(TripPreference.START_PREFS_TIME,
          tripViewModel.getTripStartTimeInMillis()).apply();
        break;
      case TripPreference.END_PREFS_TIME:
        tripViewModel.setEndTime(hourOfDay, minute);
        sharedPreferences.edit().putLong(TripPreference.END_PREFS_TIME,
          tripViewModel.getTripEndTimeInMillis()).apply();
        break;
      default:
        throw new RuntimeException("No Match To Time Preference Key.");
    }
  }

  @Override
  public void setDate(int year, int month, int day) {
    tripViewModel.setDate(year, month, day);
  }

  @Override
  public String getDate() {
    return tripViewModel.getDatePresentable();
  }

  @Override
  public String getTime(String timePrefs) {
    if (timePrefs.equals(TripPreference.END_PREFS_TIME)) {
      return tripViewModel.getEndTimePresentable();
    }
    return tripViewModel.getStartTimePresentable();
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
    long currentTimeMillis = System.currentTimeMillis();
    Log.d(TAG, "scheduleDataDeletion: " + (tripViewModel.getTripEndTimeInMillis() - currentTimeMillis));
    WorkManager.getInstance().enqueue(new OneTimeWorkRequest.Builder(DeletionWorker.class)
      .setInitialDelay(
        tripViewModel.getTripEndTimeInMillis() - currentTimeMillis, TimeUnit.MILLISECONDS)
      .build());
  }

  public AlertDialog getAboutDialog() {
    AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);
    aboutDialog.setTitle(R.string.findoutmore)
      .setItems(R.array.links_array, (dialog, item) -> {
        switch (item) {
          case 0:
            startActivity(new Intent(Intent.ACTION_VIEW,
              Uri.parse(getString(R.string.repo))));
            break;
          case 1:
            startActivity(new Intent(Intent.ACTION_VIEW,
              Uri.parse(getString(R.string.linkedIn))));
            break;
          case 2:
            startActivity(new Intent(Intent.ACTION_VIEW,
              Uri.parse(getString(R.string.twitter_link))));
            break;
        }
      });
    return aboutDialog.create();
  }
}
