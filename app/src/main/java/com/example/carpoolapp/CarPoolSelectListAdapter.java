package com.example.carpoolapp;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CarPoolSelectListAdapter  extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] buttonTextArray;

    FireStoreDatbase fsd = new FireStoreDatbase();

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

        //textView
        TextView tv = (TextView) rowView.findViewById(R.id.textView2);
        tv.setText(buttonTextArray[position]);

        //final positon for button
        final int accesablePosition = position;

        if(position%2 == 0)
        {
            tv.setBackgroundColor(Color.parseColor("#F0A22D"));
        }
        else
        {
            tv.setBackgroundColor(Color.parseColor("#2196F3"));
        }

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext() , "AddUser",Toast.LENGTH_LONG).show();
                User testUser = new User("111","first Name", "Last Name");
                fsd.addUserToCarpool(testUser,buttonTextArray[accesablePosition]);
                int stop = 1;
            }
        });



        return rowView;




    };

}
