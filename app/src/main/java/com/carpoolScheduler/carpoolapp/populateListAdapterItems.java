package com.carpoolScheduler.carpoolapp;

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
    public static String[] populateMainTitle(ArrayList<Object> userList)
    {
        //skipp first item because its the carpool id
        int numOfUsers = userList.size();

        String[] maintitle= new String[numOfUsers-1];
        for (int index = 1;index<numOfUsers;index++)
        {
            maintitle[index-1] =((User) userList.get(index)).firstName;
        }
        return maintitle;
    }

    public static String[] populateSubTitle(ArrayList<Object> userList) {
        int numOfUsers = userList.size();

        String[] subtitle= new String[numOfUsers-1];
        for (int index = 1;index<numOfUsers;index++)
        {
            subtitle[index-1] = String.valueOf(((User)userList.get(index)).driveCount);
        }
        return subtitle;
    }
    public static String[] populateUserID(ArrayList<Object> userList) {
        int numOfUsers = userList.size();

        String[] UserId= new String[numOfUsers-1];
        for (int index = 1;index<numOfUsers;index++)
        {
            UserId[index-1] = String.valueOf(((User)userList.get(index)).id);
        }
        return UserId;
    }

//    public static String[] populateSubTitle(ArrayList<User> userList) {
//        int numOfUsers = userList.size();
//
//        String[] subtitle= new String[numOfUsers-1];
//        for (int index = 1;index<numOfUsers;index++)
//        {
//            subtitle[index-1] = userList.get(index).id;
//        }
//        return subtitle;
//    }
}
