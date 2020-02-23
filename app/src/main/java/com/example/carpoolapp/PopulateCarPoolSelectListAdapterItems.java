package com.example.carpoolapp;

import java.util.List;

public class PopulateCarPoolSelectListAdapterItems {
    public static String[] populateCarpools(List<String> carPools)
    {
        int carPoolSize = carPools.size();

        String[] carpoolIDs= new String[carPoolSize];
        for (int index = 0;index<carPoolSize;index++)
        {
            carpoolIDs[index] = carPools.get(index);
        }
        return carpoolIDs;
    }


}
