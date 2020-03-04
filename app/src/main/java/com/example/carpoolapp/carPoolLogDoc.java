package com.example.carpoolapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class carPoolLogDoc {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "CarPool Log")
    private String carPoolLogDoc;

    public carPoolLogDoc(String carPoolLogDocs) {
        this.carPoolLogDoc = carPoolLogDocs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarPoolLogDoc() {
        return carPoolLogDoc;
    }

    public void setCarPoolLogDoc(String carPoolLogDoc) {
        this.carPoolLogDoc = carPoolLogDoc;
    }
}
