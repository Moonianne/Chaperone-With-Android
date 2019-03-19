package org.pursuit.school_trip_assistant.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.view.fragment.InputStudentFragment;

public final class StudentListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        setSupportActionBar(findViewById(R.id.toolbar));
        setFabListener(findViewById(R.id.fab));
    }

    private void setFabListener(FloatingActionButton fab) {
        fab.setOnClickListener(view -> showInputFragment());
    }

    private void showInputFragment() {
        inflateFragment(InputStudentFragment.newInstance(), true);
    }

    private void inflateFragment(Fragment fragment, boolean isBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment);
        if (isBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
