package com.udacity.sandwichclub;

import com.udacity.sandwichclub.data.AppDatabase;
import com.udacity.sandwichclub.model.Sandwich;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class DetailActivityViewModel extends ViewModel {

    private LiveData<Sandwich> sandwich;

    DetailActivityViewModel(AppDatabase database, int sandwichId) {
        sandwich = database.sandwichDao().loadSandwichById(sandwichId);
    }

    public LiveData<Sandwich> getSandwich() {
        return sandwich;
    }
}

