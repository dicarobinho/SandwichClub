package com.udacity.sandwichclub.data;

import com.udacity.sandwichclub.model.Sandwich;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SandwichDao {
    @Query("SELECT * FROM sandwiches")
    LiveData<List<Sandwich>> loadAllSandwiches();

    @Insert
    void insertSandwich(Sandwich sandwich);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSandwich(Sandwich sandwich);

    @Delete
    void deleteSandwich(Sandwich sandwich);

    @Query("SELECT * FROM sandwiches WHERE id = :id")
    LiveData<Sandwich> loadSandwichById(int id);
}
