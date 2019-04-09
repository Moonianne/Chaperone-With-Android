package org.pursuit.school_trip_assistant.view.fragment.input_student;

import java.io.File;

public interface OnPictureTakenListener {
  void onPictureTaken(File file);

  File getCameraFile();
}
