package com.example.carpoolapp;

import java.util.ArrayList;

public class PopulateCarPoolSelectListAdapterItems {
    public static String[] populateCarpools(ArrayList<ArrayList<Object>> carPools)
    {
        int carPoolSize = carPools.size();

        String[] carpoolIDs= new String[carPoolSize];
        for (int index = 0;index<carPoolSize;index++)
        {
            carpoolIDs[index] = (String) carPools.get(index).get(0);
        }
        return carpoolIDs;
    }


}
