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
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.RequiresApi;

public class mainActivityListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private ArrayList<mainActivityUserentry> totalList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public mainActivityListAdapter(Activity context, String[] maintitle, String[] driveCount, Integer[] imgid) {
        super(context, R.layout.list_item, maintitle);
        //used for sorting users by drivecount. add to object
        for (int i = 0; i < maintitle.length; i++) {
            totalList.add(new mainActivityUserentry(maintitle[i], driveCount[i], imgid[i]));
        }
        //sort object
        Collections.sort(totalList, new Comparator<mainActivityUserentry>() {
            @Override
            public int compare(mainActivityUserentry mainActivityUserentry, mainActivityUserentry t1) {
                return Integer.compare(mainActivityUserentry.drivecount, t1.drivecount);
            }
        });

        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(totalList.get(position).maintitle);
        imageView.setImageResource(totalList.get(position).imgid);
        subtitleText.setText(String.valueOf(totalList.get(position).drivecount));

        return rowView;

    }
    //class to orginise user entrys
    private class mainActivityUserentry {
        String maintitle;
        int drivecount;
        int imgid;

        mainActivityUserentry(String maintitle, String drivecount, int imgid) {
            this.maintitle = maintitle;
            this.drivecount = Integer.parseInt(drivecount);
            this.imgid = imgid;
        }
    }
}

