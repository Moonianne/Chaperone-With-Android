package org.pursuit.school_trip_assistant.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.model.Student;
import org.pursuit.school_trip_assistant.view.fragment.InputStudentFragment;
import org.pursuit.school_trip_assistant.view.fragment.SplashFragment;
import org.pursuit.school_trip_assistant.view.recyclerview.StudentAdapter;
import org.pursuit.school_trip_assistant.viewmodel.StudentsViewModel;

import java.util.LinkedList;

public final class StudentListActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    private final StudentAdapter studentAdapter = new StudentAdapter(new LinkedList<>());

    private StudentsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        inflateFragment(SplashFragment.newInstance());
        setSupportActionBar(findViewById(R.id.toolbar));
        setFabListener(findViewById(R.id.fab));

        viewModel = new StudentsViewModel(this);
        RecyclerView recyclerView = findViewById(R.id.recycyler_student_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(studentAdapter);
        studentAdapter.setData(viewModel.getStudentsFromDatabase());
    }

    @Override
    public void addStudentToDatabase(Student student, Fragment fragment) {
        viewModel.addStudentToDatabase(student);
        closeFragment(fragment);
        studentAdapter.setData(viewModel.getStudentsFromDatabase());
    }

    @Override
    public void finishSplashScreen(Fragment fragment) {
        closeFragment(fragment);
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

    private void closeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }
}
