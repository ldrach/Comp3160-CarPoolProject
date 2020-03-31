package com.example.carpoolapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CarPoolSelectListAdapter  extends ArrayAdapter<String> {

import androidx.core.content.ContextCompat;

public class CarPoolSelectListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] buttonTextArray;



    public CarPoolSelectListAdapter(Activity context, String[] buttonTextArray) {
        super(context,R.layout.car_pool_select_list_item, buttonTextArray);

        this.context=context;
        this.buttonTextArray = buttonTextArray;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.car_pool_select_list_item, null,true);


        //button for testing
        final Button addUserButton = (Button) rowView.findViewById(R.id.addUserButton);
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
                Toast.makeText(getContext() , "This is my Toast message!"+ button.getText(),Toast.LENGTH_LONG).show();
                int stop = 1;
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
