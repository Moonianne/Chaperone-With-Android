package org.pursuit.school_trip_assistant.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import org.pursuit.school_trip_assistant.db.StudentDatabase;
import org.pursuit.school_trip_assistant.model.Student;

import java.util.Collections;
import java.util.List;

public final class StudentsViewModel extends ViewModel {
    private final StudentDatabase studentDatabase;

    public StudentsViewModel(Context context) {
        this.studentDatabase = StudentDatabase.getInstance(context);
    }

    public void addStudentToDatabase(Student student) {
        studentDatabase.addStudent(student);
    }

    public List<Student> getStudentsFromDatabase() {
        List<Student> students = studentDatabase.getStudentList();
        Collections.sort(students, Student.studentComparator);
        return students;
    }
}
