package org.pursuit.school_trip_assistant.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static android.support.constraint.Constraints.TAG;

public final class DeletionWorker extends Worker {
  private final Context context;

  public DeletionWorker(@NonNull Context context,
                        @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
    this.context = context;
  }

  @NonNull
  @Override
  public Result doWork() {
    if (trimCache() && deleteDatabase() && deleteSettings()) {
      return Result.success();
    }
    return Result.failure();
  }

  private boolean trimCache() {
    try {
      File dir = context.getCacheDir();
      if (dir != null && dir.isDirectory()) {
        return deleteDir(dir);
      }
    } catch (Exception e) {
      Log.d(TAG, "trimCache: " + e);
    }
    return false;
  }

  private boolean deleteDir(File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }
    return dir.delete();
  }

  private boolean deleteDatabase() {
    return StudentDatabase.getInstance(context).deleteAll(context);
  }

  private boolean deleteSettings() {
    return context.getSharedPreferences("ASSISTANT", Context.MODE_PRIVATE)
      .edit()
      .clear()
      .commit();
  }
}
