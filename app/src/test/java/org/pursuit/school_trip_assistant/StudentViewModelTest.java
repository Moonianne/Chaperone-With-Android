package org.pursuit.school_trip_assistant;

import org.junit.Before;
import org.junit.Test;
import org.pursuit.school_trip_assistant.model.Student;
import org.pursuit.school_trip_assistant.viewmodel.StudentsViewModel;

import static org.junit.Assert.*;

public class StudentViewModelTest {
    Student student;
    @Before
    public void setup() {
        student = new Student("Geo", "Jimenez", "9174468023", null);
    }
    @Test
    public void nameIsCorrect() {
        assertEquals(4, 2 + 2);
    }
}