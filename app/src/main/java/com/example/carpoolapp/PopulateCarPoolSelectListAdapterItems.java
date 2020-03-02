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
    public static ArrayList<ArrayList<String>> populateUsers(ArrayList<ArrayList<Object>> carPools)
    {
        int carPoolSize = carPools.size();

        ArrayList<ArrayList<String>> totalUsers= new ArrayList();
        ArrayList<String> userArray ;

        //iterate through carpools
        for (int index = 0;index<carPoolSize;index++)
        {
            int length = carPools.get(index).size();
            userArray = new ArrayList<>();
            //start at one because the first item is not a user
            //iterate through users in carpools
            for(int x =1;x<length;x++)
            {
                //get user from carpool
                User tempUser=(User) carPools.get(index).get(x);
                userArray.add( tempUser.firstName +" "+ tempUser.lastName);
            }
            totalUsers.add(userArray);

        }
        return totalUsers;
    }



}
