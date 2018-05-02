package com.example.anthony.financialtracking;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by Keegan on 4/20/2018.
 *
 * * ***************** IMPORTANT **********************************
 *
 * !!! This is all you need to use the database. !!!
 *
 * DO NOT interact with the room database directly through the Dao or other methods.
 * If you need other functionality, provide it here or ask me
 *
 * ********************************************************************
 *
 * This class contains all the methods used to interact with the room database.
 * It contains wrappers for the Dao methods, and handles the threading issues.*
 *
 * Dont have to read/understand the other files unless you want to modify
 * the database, in which case you should probably ask me first
 * unless youre sure you know what youre doing
 *
 * TODO Add more functionality
 */

public class RecordRepository {

    //The dao containing the basic methods
    private RecordDao mRecordDao;
    //Live view of the data
    private LiveData<List<Record>> mAllRecords;


    /**
     * Constructor for the repository
     * Gets a handle to the database and inits vars
     * @param application
     */
    RecordRepository(Application application) {
        RecordRoomDatabase db = RecordRoomDatabase.getDatabase(application);
        mRecordDao = db.recordDao();
        mAllRecords = mRecordDao.getAllRecords();
    }

    /**
     * Wrapper method for getAllRecords
     * @return a LiveData containing a list of all the records
     */
    LiveData<List<Record>> getAllRecords() {
        return mAllRecords;
    }

    /**
     * Wrapper method for getAllRecords
     * @return a LiveData containing a list of all the records with time stamp>time
     */
    LiveData<List<Record>> getAllRecords(long time) {return mRecordDao.getAllRecords(time);
    }


    /**
     * This method inserts the record into the database
     * It takes care of all the threading and stuff
     *
     * @param record to be inserted
     */
    public void insert (Record record) {
        new insertAsyncTask(mRecordDao).execute(record);
    }


    /**
     * clears the database of all entries
     */
    public void clearDatabase(){ new clearDbAsyncTask(mRecordDao).execute();}

    public void delete(Record record) {new deleteAsyncTask(mRecordDao).execute(record);}

    public void update(Record record) {new updateAsyncTask(mRecordDao).execute(record);}

    /**
     * Adds 30 randomonly generated records to the database
     */
    public void populateDatabase(String[] foo){ new populateDbAsync(mRecordDao).execute(foo);}

    /**
     * Contains the code to create the asynctask to insert a new record into the database
     * Creates the task, and runs the insert in the background
     */
    private static class insertAsyncTask extends AsyncTask<Record, Void, Void> {

        private RecordDao mAsyncTaskDao;

        insertAsyncTask(RecordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Record... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class clearDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private RecordDao mAsyncTaskDao;

        clearDbAsyncTask(RecordDao dao) {mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Record, Void, Void> {
        private RecordDao mAsyncTaskDao;

        deleteAsyncTask(RecordDao dao) {mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Record... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Record, Void, Void> {
        private RecordDao mAsyncTaskDao;

        updateAsyncTask(RecordDao dao) {mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Record... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class populateDbAsync extends AsyncTask<String[], Void, Void> {
        private RecordDao mAsyncTaskDao;
        Random r = new Random();

        populateDbAsync(RecordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String[]... params) {
            String[] types = params[0];
            mAsyncTaskDao.deleteAll();
            for(int i=0; i<30; i++){
                String name = "RecordGen" + i;
                String type = types[r.nextInt(types.length)];
                double amount = ((double)r.nextInt(100000))/100;
                Record rec = new Record(name, amount, type);
                rec.setTimestamp(rec.getTimestamp()- ThreadLocalRandom.current().nextLong(TimeUnit.DAYS.toMillis(7)));
                mAsyncTaskDao.insert(rec);

            }
            return null;
        }
    }
}
