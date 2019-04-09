package org.pursuit.school_trip_assistant.view.fragment.camera;

import java.io.File;

public interface OnPictureTakenListener {
  void onPictureTaken(File file, CameraFragment cameraFragment);
}
