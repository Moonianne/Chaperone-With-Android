package org.pursuit.school_trip_assistant.view.fragment.student_list;

import org.pursuit.school_trip_assistant.model.Student;

import java.util.List;

public interface DataReceiveListener {
  void onNewDataReceived(List<Student> students);
}
