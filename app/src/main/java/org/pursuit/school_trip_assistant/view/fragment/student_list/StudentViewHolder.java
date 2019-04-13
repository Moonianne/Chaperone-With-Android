package org.pursuit.school_trip_assistant.view.fragment.student_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.constants.Debounce;
import org.pursuit.school_trip_assistant.model.Student;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

final class StudentViewHolder extends RecyclerView.ViewHolder {
  private Disposable disposable;

  StudentViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  void onBind(Student student, ItemClickListener itemClickListener) {
    String studentFullName = student.lastName + ", " + student.firstName;
    TextView textView = itemView.findViewById(R.id.rv_student_name);
    textView.setText(studentFullName);

    disposable = RxView.clicks(itemView)
      .debounce(Debounce.TIME, TimeUnit.MILLISECONDS)
      .subscribe(click -> onClick(itemClickListener, student.iD));
  }

  private void onClick(ItemClickListener itemClickListener, int iD) {
    itemClickListener.onItemClick(iD);
    disposable.dispose();
  }
}
