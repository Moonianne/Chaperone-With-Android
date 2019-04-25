package org.pursuit.school_trip_assistant.viewmodel;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

public final class ViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public ViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        /**
         * check if the viewmodel class being passed in is correct
         */
        if(modelClass.isAssignableFrom(StudentsViewModel.class)){
            return (T) new StudentsViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
