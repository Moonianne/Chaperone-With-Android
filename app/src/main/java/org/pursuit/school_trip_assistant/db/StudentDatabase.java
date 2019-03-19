package org.pursuit.school_trip_assistant.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.pursuit.school_trip_assistant.model.Student;

public final class StudentDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "STUDENT_DATABASE.db";
    private static final String TABLE_NAME = "Students";
    private static final int SCHEMA_VERSION = 1;
    private static StudentDatabase instance;

    private StudentDatabase(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    public static StudentDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new StudentDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME +
                        " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "last_name TEXT, first_name TEXT, emergency_number TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //No-Op
    }

    public boolean addStudent(Student student) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE last_name = '" + student.lastName + "' AND first_name = '" + student.firstName + "';", null);
        if (cursor.getCount() == 0) {
            getWritableDatabase().execSQL("INSERT INTO " + TABLE_NAME + "(last_name, first_name, emergency_number) VALUES('" +
                    student.lastName + "', '" +
                    student.firstName + "', '" +
                    student.ePhoneNumber + "');");
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
}
