package org.pursuit.school_trip_assistant.view.fragment.input_student;

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
import com.squareup.picasso.Picasso;

import org.pursuit.school_trip_assistant.R;
import org.pursuit.school_trip_assistant.constants.Debounce;
import org.pursuit.school_trip_assistant.model.Student;
import org.pursuit.school_trip_assistant.view.OnFragmentInteractionListener;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

public final class InputStudentFragment extends Fragment {
  private static final String IMAGE_KEY = "IMAGE";
  private static final String FIRST_NAME_KEY = "FIRST NAME";
  private static final String LAST_NAME_KEY = "LAST NAME";
  private static final String CONTACT_KEY = "CONTACT";

  private static final String TAG = "InputStudentFragmen.TAG";

  private OnFragmentInteractionListener onFragmentInteractionListener;
  private OnPictureTakenListener onPictureTakenListener;
  private ImageView imageView;
  private EditText editLastName;
  private EditText editFirstName;
  private EditText editContact;
  private Disposable disposable;
  private File image;

  public static InputStudentFragment newInstance(@Nullable Student student) {
    InputStudentFragment inputStudentFragment = new InputStudentFragment();
    if (student != null) {
      Bundle args = new Bundle();
      args.putString(IMAGE_KEY, student.image.getAbsolutePath());
      args.putString(FIRST_NAME_KEY, student.firstName);
      args.putString(LAST_NAME_KEY, student.lastName);
      args.putString(CONTACT_KEY, student.ePhoneNumber);
      inputStudentFragment.setArguments(args);
    }
    return inputStudentFragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    onFragmentInteractionListener = (OnFragmentInteractionListener) context;
    onPictureTakenListener = (OnPictureTakenListener) context;
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

    imageView = view.findViewById(R.id.image_student);
    imageView.setOnClickListener(v -> takePhoto());
    editLastName = view.findViewById(R.id.edit_last_name);
    editFirstName = view.findViewById(R.id.edit_first_name);
    editContact = view.findViewById(R.id.edit_phone);

    if (getArguments() != null) {
      Bundle received = getArguments();
      File file = new File(received.getString(IMAGE_KEY));
      image = file;
      Picasso.get().load(file).into(imageView);
      editFirstName.setText(received.getString(FIRST_NAME_KEY));
      editLastName.setText(received.getString(LAST_NAME_KEY));
      editContact.setText(received.getString(CONTACT_KEY));
    }

    setupSubmitButton(view);
  }

  @Override
  public void onResume() {
    super.onResume();
    File file = onPictureTakenListener.getCameraFile();
    if (file != null) {
      Log.d(InputStudentFragment.class.getName(), "onStart: " + file.getName());
      Picasso.get().load(file).into(imageView);
    }
    image = file;
  }

  @Override
  public void onDetach() {
    disposable.dispose();
    onFragmentInteractionListener = null;
    super.onDetach();
  }

  private void setupSubmitButton(@NonNull View view) {
    disposable = RxView.clicks(view.<Button>findViewById(R.id.button_submit))
      .debounce(Debounce.TIME, TimeUnit.MILLISECONDS)
      .map(click -> getInputStudent())
      .subscribe(this::addStudentToDatabase,
        throwable -> Log.d(TAG, "onFailure: " + throwable));
  }

  private Student getInputStudent() {
    return new Student(editFirstName.getText().toString(),
      editLastName.getText().toString(),
      editContact.getText().toString(), image);
  }

  private void addStudentToDatabase(Student student) {
    onFragmentInteractionListener.addStudentToDatabase(student, this);
  }

  private void takePhoto() {
    onFragmentInteractionListener.showCameraFragment();
  }
}
