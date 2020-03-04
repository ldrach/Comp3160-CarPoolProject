package com.example.carpoolapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {carPoolLogDoc.class},version = 1, exportSchema = false)
public abstract class carPoolLogDatabase extends RoomDatabase {
    public abstract carPoolLogDocDao getCarPoolLogDao();
}
