package org.pursuit.school_trip_assistant.model;

import java.util.Comparator;

public final class Student {
    public static final Comparator<Student> studentComparator =
            Student::compareStudent;

    public final String firstName;
    public final String lastName;
    public final String ePhoneNumber;


    public Student(String firstName, String lastName, String ePhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ePhoneNumber = ePhoneNumber;
    }

    private static int compareStudent(Student s1, Student s2) {
        int lastNameCompare = s1.lastName.toUpperCase().compareTo(s2.lastName.toUpperCase());
        if (lastNameCompare != 0) {
            return lastNameCompare;
        }
        return s1.firstName.toUpperCase().compareTo(s2.firstName.toUpperCase());
    }
}
