package org.pursuit.school_trip_assistant.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.model.Student;

final class StudentViewHolder extends RecyclerView.ViewHolder {

    StudentViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void onBind(Student student) {
        String studentFullName = student.lastName + ", " + student.firstName;
        TextView textView = itemView.findViewById(R.id.rv_student_name);
        textView.setText(studentFullName);
    }
}
