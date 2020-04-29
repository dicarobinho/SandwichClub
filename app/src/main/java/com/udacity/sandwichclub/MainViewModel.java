package com.udacity.sandwichclub;

import android.app.Application;
import android.util.Log;

import com.udacity.sandwichclub.data.AppDatabase;
import com.udacity.sandwichclub.model.Sandwich;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Sandwich>> sandwiches;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        sandwiches = database.sandwichDao().loadAllSandwiches();
    }

    LiveData<List<Sandwich>> getSandwiches() {
        return sandwiches;
    }
}
