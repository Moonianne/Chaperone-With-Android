package org.pursuit.school_trip_assistant.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding3.view.RxView;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.model.Student;
import org.pursuit.school_trip_assistant.view.OnFragmentInteractionListener;
import org.pursuit.school_trip_assistant.view.fragment.camera.CameraFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

public final class InputStudentFragment extends Fragment {
    private static final String TAG = "InputStudentFragmen.TAG";
    private static final long DEBOUNCE_TIMEOUT = 300;

    private OnFragmentInteractionListener onFragmentInteractionListener;
    private EditText editLastName;
    private EditText editFirstName;
    private EditText editContact;
    private Disposable disposable;

    public static InputStudentFragment newInstance() {
        return new InputStudentFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onFragmentInteractionListener = (OnFragmentInteractionListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.image_student);
        imageView.setOnClickListener(v -> takePhoto());

        editLastName = view.findViewById(R.id.edit_last_name);
        editFirstName = view.findViewById(R.id.edit_first_name);
        editContact = view.findViewById(R.id.edit_phone);
        setupSubmitButton(view);
    }

    @Override
    public void onDetach() {
        disposable.dispose();
        onFragmentInteractionListener = null;
        super.onDetach();
    }

    private void setupSubmitButton(@NonNull View view) {
        disposable = RxView.clicks(view.<Button>findViewById(R.id.button_submit))
                .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                .map(click -> getInputStudent())
                .subscribe(this::addStudentToDatabase,
                        throwable -> Log.d(TAG, "onFailure: " + throwable));
    }

    private Student getInputStudent() {
        return new Student(editFirstName.getText().toString(),
                editLastName.getText().toString(),
                editContact.getText().toString());
    }

    private void addStudentToDatabase(Student student) {
        onFragmentInteractionListener.addStudentToDatabase(student, this);
    }

    private void takePhoto() {
        if (getFragmentManager() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, CameraFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
    }
}