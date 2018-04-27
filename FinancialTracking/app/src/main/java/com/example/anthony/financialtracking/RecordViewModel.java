package com.example.anthony.financialtracking;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Created by Keegan on 4/26/2018.
 */

public class RecordViewModel extends AndroidViewModel {
    private RecordRepository mRepository;
    private LiveData<List<Record>> mAllRecords;
    private String[] types;


    public RecordViewModel(Application application) {
        super(application);
        mRepository = new RecordRepository(application);
        mAllRecords = mRepository.getAllRecords();
    }

    LiveData<List<Record>> getAllRecords() {
        return mAllRecords;
    }

    public void insert(Record record) {
        mRepository.insert(record);
    }

    public void clearDatabase(){
        mRepository.clearDatabase();
    }

    public void populateDatabase(){
        mRepository.populateDatabase(types);
    }

    public void setTypes(String[] foo){
        types=foo;
    }
}
