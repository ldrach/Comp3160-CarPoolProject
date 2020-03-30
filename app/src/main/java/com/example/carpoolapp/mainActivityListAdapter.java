package com.example.carpoolapp;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class mainActivityListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private ArrayList<mainActivityUserentry> UserList = new ArrayList<>();
    private ArrayList<mainActivityUserentry> totalList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public mainActivityListAdapter(Activity context, String[] maintitle, String[] driveCount, Integer[] imgid, String[] weekDaysArray) {
        super(context, R.layout.list_item, weekDaysArray);
        //used for sorting users by drivecount. add to object
        for (int i = 0; i < maintitle.length; i++) {
            UserList.add(new mainActivityUserentry(maintitle[i], driveCount[i], imgid[i]));

        }
        //sort object
        sortBasedOnDriveCount(UserList);
        //

        // populate up to 2 weeks
        //--
        ArrayList<mainActivityUserentry> copy = new ArrayList<>();//create a copy
        for (mainActivityUserentry entry : UserList) {
            copy.add(new mainActivityUserentry(entry));
        }
        populateTotalList(weekDaysArray, copy);
        //--

        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        TextView dateTextView = (TextView) rowView.findViewById(R.id.dateTextView);

        titleText.setText(totalList.get(position).maintitle);
        imageView.setImageResource(totalList.get(position).imgid);
        subtitleText.setText(String.valueOf(totalList.get(position).drivecount));
        dateTextView.setText(totalList.get(position).dayOfWeek);

        return rowView;

    }

    //used for creating totalList and adding day of week
    //---
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populateTotalList(String[] weekDaysArray, ArrayList<mainActivityUserentry> copyUserList) {
        Date dt = new Date();
        Calendar calender = Calendar.getInstance();
        calender.setTime(dt);

        //TODO this will break if two users have the same name
        //fix only one user issue
        for (int i = 0; i < weekDaysArray.length; i++) {



            dt = calender.getTime();
                mainActivityUserentry newUser = new mainActivityUserentry(copyUserList.get(0));
                newUser.dayOfWeek= calender.getTime().toString().split("(?<=(([a-z|A-Z]{3} ){2}\\d{2})) ")[0];//Sat Mar 14 15:31:20 PDT 2020
                totalList.add(newUser );
                copyUserList.get(0).drivecount++;


            sortBasedOnDriveCount(copyUserList);
            calender.add(Calendar.DATE, 1);

        }


    }

    private void sortBasedOnDriveCount(ArrayList<mainActivityUserentry> list) {
        Collections.sort(list, new Comparator<mainActivityUserentry>() {
            @Override
            public int compare(mainActivityUserentry mainActivityUserentry, mainActivityUserentry t1) {
                if (mainActivityUserentry.drivecount == t1.drivecount) return mainActivityUserentry.maintitle.compareTo(t1.maintitle);
                return Integer.compare(mainActivityUserentry.drivecount, t1.drivecount);
            }
        });
    }
    //---

    //class to orginise user entrys
    private class mainActivityUserentry {
        String maintitle;
        int drivecount;
        int imgid;
        String dayOfWeek;

        mainActivityUserentry(String maintitle, String drivecount, int imgid) {
            this.maintitle = maintitle;
            this.drivecount = Integer.parseInt(drivecount);
            this.imgid = imgid;
        }

        public mainActivityUserentry(mainActivityUserentry x) {
            // Copy all the fields of mainActivityUserentry.
            this.maintitle = x.maintitle;
            this.drivecount = x.drivecount;
            this.imgid = x.imgid;
        }

        @NonNull
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return this.clone();
        }
    }
}

//    List<mainActivityUserentry> user = UserList.stream()
//            .filter(x -> Objects.equals(x.maintitle, copyUserList.get(1).maintitle))
//            .collect(Collectors.toList());