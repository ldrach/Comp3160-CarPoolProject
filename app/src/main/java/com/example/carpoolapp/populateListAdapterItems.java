package com.example.carpoolapp;

import java.util.ArrayList;

public class populateListAdapterItems  {

    public static Integer[] populateIcon(int numberOfIcons)
    {
        Integer[] imgid= new Integer[numberOfIcons];
        for (int index = 0;index<numberOfIcons;index++)
        {
            imgid[index] = R.drawable.icon_1;
        }
        return imgid;
    }
    public static String[] populateMainTitle(ArrayList<User> userList)
    {
        int numOfUsers = userList.size();

        String[] maintitle= new String[numOfUsers];
        for (int index = 0;index<numOfUsers;index++)
        {
            maintitle[index] = userList.get(index).firstName;
        }
        return maintitle;
    }

    public static String[] populateSubTitle(ArrayList<User> userList) {
        int numOfUsers = userList.size();

        String[] subtitle= new String[numOfUsers];
        for (int index = 0;index<numOfUsers;index++)
        {
            subtitle[index] = userList.get(index).id;
        }
        return subtitle;
    }
}
