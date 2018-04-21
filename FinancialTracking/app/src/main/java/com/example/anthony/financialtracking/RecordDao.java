package com.example.anthony.financialtracking;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Keegan on 4/20/2018.
 *
 * This interface is Data Access Object for the table Record.
 *
 * Don't actually use these methods, use the methods in RecordRepository, which contains
 * wrappers for these methods and handles all the asynctask/threading issues
 *
 *
 * Contains simple CRUD methods
 *
 * The methods should all be self explanatory
 *
 * GetAllRecords returns LiveData, which will auto update as the data changes
 *
 * **** IMPORTANT ****
 *
 * Queries must be done in an async task/seperate thread and not on the main thread
 *
 * ******************
 */

@Dao
public interface RecordDao {

    @Insert
    void insert(Record record);

    @Delete
    void delete(Record record);

    @Update
    void update(Record record);

    @Query("DELETE FROM Record")
    void deleteAll();

    @Query("SELECT * from Record")
    LiveData<List<Record>> getAllRecords();
}
