package org.pursuit.school_trip_assistant.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import org.pursuit.school_trip_assistant.db.StudentDatabase;
import org.pursuit.school_trip_assistant.model.Student;

import java.io.File;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;

public final class StudentsViewModel extends ViewModel {
  private final StudentDatabase studentDatabase;

  private MutableLiveData<List<Student>> studentList = new MutableLiveData<>();

  public StudentsViewModel(Context context) {
    this.studentDatabase = StudentDatabase.getInstance(context);
    getStudentsFromDatabase();
  }

  public Completable addStudentToDatabase(Student student) {
    return Completable.fromAction(() -> {
      studentDatabase.addStudent(student);
      getStudentsFromDatabase();
    });
  }

  public boolean deleteStudentFromDatabase(String iD) {
    return studentDatabase.deleteStudent(iD);
  }

  public Student getStudent(int iD) {
    return studentDatabase.getStudent(iD);
  }

  public String getStudentLastNameFirstName(int iD) {
    Student student = getStudent(iD);
    return student.lastName + ", " + student.firstName;
  }

  public String getEmergencyContact(int iD) {
    return getStudent(iD).ePhoneNumber;
  }

  public File getStudentImage(int iD) {
    return getStudent(iD).image;
  }

  public void getStudentsFromDatabase() {
    List<Student> students = studentDatabase.getStudentList();
    Collections.sort(students, Student.studentComparator);
    studentList.postValue(students);
  }

  public LiveData<List<Student>> getStudentList() {
    return studentList;
  }
}
