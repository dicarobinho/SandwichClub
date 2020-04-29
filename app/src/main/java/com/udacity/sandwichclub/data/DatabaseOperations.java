package com.udacity.sandwichclub.data;

import android.content.Context;

import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.Random;

public class DatabaseOperations {

    public static void saveSandwichToDataBase(final Sandwich sandwich, Context context) {
        final AppDatabase mDb = AppDatabase.getInstance(context);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (sandwich.getId() > 0) {
                    mDb.sandwichDao().updateSandwich(sandwich);
                } else {
                    mDb.sandwichDao().insertSandwich(sandwich);
                }
            }
        });
    }

    public static void parsareRandomJsonLocal(Context context) {
        String[] currentSandwiches = context.getResources().getStringArray(R.array.sandwich_details);
        final int randomSandwich = new Random().nextInt(9);
        String json = currentSandwiches[randomSandwich];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        saveSandwichToDataBase(sandwich, context);
    }

    public static void deleteSandwich(final Sandwich sandwich, Context context) {
        final AppDatabase mDb = AppDatabase.getInstance(context);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.sandwichDao().deleteSandwich(sandwich);
            }
        });
    }
}
