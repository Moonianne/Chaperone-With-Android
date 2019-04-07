package org.pursuit.school_trip_assistant.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.model.Student;
import org.pursuit.school_trip_assistant.view.ItemClickListener;

import java.util.concurrent.TimeUnit;

final class StudentViewHolder extends RecyclerView.ViewHolder {
  private static final long DEBOUNCE_TIMEOUT = 300;

  StudentViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  void onBind(Student student, ItemClickListener itemClickListener) {
    String studentFullName = student.lastName + ", " + student.firstName;
    TextView textView = itemView.findViewById(R.id.rv_student_name);
    textView.setText(studentFullName);

    RxView.clicks(itemView)
      .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
      .subscribe(click -> itemClickListener.showStudentInformation(student.iD));
  }
}
