package org.pursuit.school_trip_assistant.view.fragment.student_list;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.model.Student;
import org.pursuit.school_trip_assistant.view.OnFragmentInteractionListener;

import java.util.Collections;
import java.util.List;

public final class StudentListFragment extends Fragment
  implements ItemClickListener, DataReceiveListener {
  private final StudentAdapter studentAdapter;

  private OnFragmentInteractionListener listener;

  public StudentListFragment() {
    studentAdapter = new StudentAdapter(this, Collections.emptyList());
  }

  public static StudentListFragment newInstance() {
    return new StudentListFragment();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      listener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException("Activity Must Implement OnFragmentInteractionListener.");
    }
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_student_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setFabListener(view.findViewById(R.id.fab));
    RecyclerView recyclerView = view.findViewById(R.id.recycler_student_list);
    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    recyclerView.setAdapter(studentAdapter);
  }

  @Override
  public void onNewDataReceived(List<Student> students) {
    studentAdapter.setData(students);
  }

  @Override
  public void onItemClick(int iD) {
    listener.showStudentInformation(iD);
  }

  private void setFabListener(FloatingActionButton fab) {
    fab.setOnClickListener(view -> listener.showStudentInputFragment());
  }
}
