package com.example.myconsist;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * AppDatabase - The Room Database class
 *
 * This is the main database configuration class that:
 * 1. Lists all entities (tables) in the database
 * 2. Provides access to DAOs
 * 3. Implements singleton pattern to ensure only one instance exists
 */
@Database(
        entities = {
                Habit.class,
                HabitLog.class,
                Task.class,
                TaskGroup.class
        },
        version = 1,                    // Database version
        exportSchema = false            // Don't export schema to avoid warnings
)
public abstract class appDatabase extends RoomDatabase {

    // Singleton instance
    private static appDatabase instance;

    /**
     * Provide access to the DAO
     * Room will generate the implementation
     */
    public abstract appDao appDao();

    /**
     * Get database instance (Singleton pattern)
     * Only one instance of the database exists in the app
     */
    public static synchronized appDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            appDatabase.class,
                            "My_Consist_Database"  // Database name
                    )
                    .allowMainThreadQueries()      // FOR DEMO ONLY! Never do this in production
                    .build();                      // Use AsyncTask or coroutines in real apps
        }
        return instance;
    }

    /**
     * Close the database (optional, useful for testing)
     */
    public static void closeDatabase() {
        if (instance != null && instance.isOpen()) {
            instance.close();
            instance = null;
        }
    }
}

