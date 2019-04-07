package org.pursuit.school_trip_assistant.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.model.Student;
import org.pursuit.school_trip_assistant.view.fragment.DisplayStudentFragment;
import org.pursuit.school_trip_assistant.view.fragment.InputStudentFragment;
import org.pursuit.school_trip_assistant.view.fragment.SplashFragment;
import org.pursuit.school_trip_assistant.view.fragment.input_trip_details.TripInputFragment;
import org.pursuit.school_trip_assistant.view.recyclerview.StudentAdapter;
import org.pursuit.school_trip_assistant.viewmodel.StudentsViewModel;
import org.pursuit.school_trip_assistant.viewmodel.ViewModelFactory;

import java.util.Collections;

public final class StudentListActivity extends AppCompatActivity
  implements OnFragmentInteractionListener, ItemClickListener {

  private final StudentAdapter studentAdapter =
    new StudentAdapter(this, Collections.EMPTY_LIST);

  //    private StudentsViewModel viewModel;
  private StudentsViewModel testViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student_list);
    inflateFragment(SplashFragment.newInstance());

    setSupportActionBar(findViewById(R.id.toolbar));
    setFabListener(findViewById(R.id.fab));

//        viewModel = new StudentsViewModel(this);
    RecyclerView recyclerView = findViewById(R.id.recycyler_student_list);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(studentAdapter);
    testViewModel = ViewModelProviders.of(this, new ViewModelFactory(this)).get(StudentsViewModel.class);
    testViewModel.getStudentList().observe(this, students -> studentAdapter.setData(students));
//        studentAdapter.setData(viewModel.getStudentsFromDatabase());
  }

  @Override
  public void addStudentToDatabase(Student student, Fragment fragment) {
    testViewModel.addStudentToDatabase(student)
      .subscribe(() -> closeFragment(fragment),
        throwable -> {
        });
//        studentAdapter.setData(testViewModel.getStudentsFromDatabase());
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

  private void setFabListener(FloatingActionButton fab) {
    fab.setOnClickListener(view -> showInputFragment());
  }

  private void showInputFragment() {
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
