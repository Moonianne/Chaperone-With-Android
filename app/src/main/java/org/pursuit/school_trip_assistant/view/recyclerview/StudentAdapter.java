package org.pursuit.school_trip_assistant.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.model.Student;
import org.pursuit.school_trip_assistant.view.ItemClickListener;

import java.util.List;

public final class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder> {
    private final ItemClickListener itemClickListener;
    private List<Student> students;

    public StudentAdapter(ItemClickListener itemClickListener, List<Student> students) {
        this.itemClickListener = itemClickListener;
        this.students = students;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StudentViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.student_item_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder studentViewHolder, int i) {
        studentViewHolder.onBind(students.get(i), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public void setData(List<Student> newStudentList) {
        this.students = newStudentList;
        notifyDataSetChanged();
    }
}
