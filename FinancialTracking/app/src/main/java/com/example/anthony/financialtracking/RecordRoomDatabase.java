package com.example.anthony.financialtracking;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Keegan on 4/20/2018.
 *
 * This class contains the RecordRoomDatabase, which acts as a wrapper to the SQL database that
 * actually stores the information.
 *
 * TODO Add versioning and migration
 *
 */
@Database(entities = {Record.class}, version = 1)
public abstract class RecordRoomDatabase extends RoomDatabase {

    //Make the database a singleton
    private static RecordRoomDatabase INSTANCE;

    /**
     * This returns the single instance of the room database. If it has not
     * yet been created, it will be created then returned.
     *
     * @param context
     * @return the record room database, fully created and ready to go
     */
    public static RecordRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecordRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecordRoomDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Returns the Dao through which the database is interfaced
     * @return the Dao
     */
    public abstract RecordDao recordDao();
}
