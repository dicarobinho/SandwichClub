package com.udacity.sandwichclub;

import com.udacity.sandwichclub.data.AppDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mTaskId;

    DetailActivityViewModelFactory(AppDatabase database, int sandwichId) {
        mDb = database;
        mTaskId = sandwichId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailActivityViewModel(mDb, mTaskId);
    }
}
