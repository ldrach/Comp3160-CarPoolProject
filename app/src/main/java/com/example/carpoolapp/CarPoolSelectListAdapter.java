package com.example.carpoolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class CarPoolSelectListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] buttonTextArray;
    private final ArrayList<ArrayList<String>> usersArray;
    private final User appUser;
    private final AppCompatActivity activity;

    FireStoreDatbase fsd = new FireStoreDatbase();

    public CarPoolSelectListAdapter(Activity context, String[] buttonTextArray, ArrayList<ArrayList<String>> usersArray, User appUser, AppCompatActivity activity) {
        super(context, R.layout.car_pool_select_list_item, buttonTextArray);

        this.context = context;
        this.buttonTextArray = buttonTextArray;
        this.usersArray = usersArray;
        this.appUser = appUser;
        this.activity = activity;


    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.car_pool_select_list_item, null, true);


        //buttons
        final Button addUserButton = (Button) rowView.findViewById(R.id.addUserButton);
        final Button delUserButton = (Button) rowView.findViewById(R.id.delUserButton);
        //addUserButton.setVisibility(View.INVISIBLE);
        //textViews
        TextView tv = (TextView) rowView.findViewById(R.id.textView2);
        //tv.setText("Carpool " + (position + 1));
        tv.setText("Carpool " + (position + 1));

        TextView user1Textview = (TextView) rowView.findViewById(R.id.user1TextView);
        TextView user2Textview = (TextView) rowView.findViewById(R.id.user2TextView);
        TextView user3Textview = (TextView) rowView.findViewById(R.id.user3TextView);
        TextView user4Textview = (TextView) rowView.findViewById(R.id.user4TextView);
        TextView user5Textview = (TextView) rowView.findViewById(R.id.user5TextView);

         user1Textview.setText("");
         user2Textview.setText("");
         user3Textview.setText("");
         user4Textview.setText("");
         user5Textview.setText("");

        TextView sideTextView = (TextView) rowView.findViewById(R.id.leftBarTextView);

        //load textviews
        try {
            user1Textview.setText(usersArray.get(position).get(0));
            user2Textview.setText(usersArray.get(position).get(1));
            user3Textview.setText(usersArray.get(position).get(2));
            user4Textview.setText(usersArray.get(position).get(3));
            user5Textview.setText(usersArray.get(position).get(4));


        } catch (Exception e) {

        }


        //final index for button
        final int accesablePosition = position;


        int color1 = ContextCompat.getColor(context,R.color.color1);
        int color2 = ContextCompat.getColor(context,R.color.color2);
        //colors
        if (position % 2 == 0) {
            tv.setBackgroundColor(color1);
            sideTextView.setBackgroundColor(changeColor(color1));

        } else {
            tv.setBackgroundColor(color2);
            sideTextView.setBackgroundColor(changeColor(color2));
        }

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "AddUser", Toast.LENGTH_LONG).show();
                User testUser = new User();
                fsd.addUserToCarpool(testUser, buttonTextArray[accesablePosition]);
                int stop = 1;
            }
        });
        delUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Do you want to delete this carpool?")
                        .setMessage("This action can't be undone")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(usersArray.get(position).size() !=1)
                                {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("You must be the only one in the car pool to delete it ")
                                            .setMessage("Go into the carpool and remove the users by Holding on there names.")
                                            .show();
                                    return;
                                }
                                //delete carpool
                                FireStoreDatbase fsd = new FireStoreDatbase();
                                fsd.deleteCarPoolFromUserCarPoolList(buttonTextArray[position],appUser.id);
                                fsd.deleteCarpool(buttonTextArray[position]);

                                fsd.showProgressDialog(activity);
                                Refresh r = new Refresh();
                                r.launchCarpoolSelect(appUser.id, appUser,activity);

                            }

                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        return rowView;
    }
    public int changeColor(int color)
    {
        float[] hsl = new float[3];
        Color.colorToHSV(color,hsl);
        hsl[1]*=2;

        // Set alpha based on your logic, here I'm making it 25% of it's initial value.


       return Color.HSVToColor( hsl);
    }



}
