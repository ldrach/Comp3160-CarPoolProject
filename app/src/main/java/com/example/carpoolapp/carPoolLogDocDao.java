package com.example.carpoolapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao // Database access object
public interface carPoolLogDocDao {
    @Insert
    void insertLogDoc(carPoolLogDoc... car_poolLogDocs);

    @Update
    void updataLogDoc(carPoolLogDoc... car_poolLogDocs);

    @Delete
    void deleteLogDoc(carPoolLogDoc... car_poolLogDocs);

    @Query("SELECT * FROM carPoolLogDoc ORDER BY ID ASC")
    //List<carPoolLogDoc> getAllcarPoolLogDoc();
    LiveData<List<carPoolLogDoc>> getAllcarPoolLogDoc();
}
