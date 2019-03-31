package org.pursuit.school_trip_assistant.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import org.pursuit.school_trip_assistant.db.StudentDatabase;
import org.pursuit.school_trip_assistant.model.Student;

import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public final class StudentsViewModel extends ViewModel {
    private final StudentDatabase studentDatabase;

    MutableLiveData<List<Student>> studentList = new MutableLiveData<>();

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

    public void getStudentsFromDatabase() {
        List<Student> students = studentDatabase.getStudentList();
        Collections.sort(students, Student.studentComparator);
        studentList.postValue(students);
    }

//    public Single<List<String>> getListStudentNames() {
//        return Observable.fromIterable(getStudentsFromDatabase())
//                .subscribeOn(Schedulers.computation())
//                .map(student -> student.lastName + ", " + student.firstName)
//                .toList();
//    }

    public LiveData<List<Student>> getStudentList() {
        return studentList;
    }
}
