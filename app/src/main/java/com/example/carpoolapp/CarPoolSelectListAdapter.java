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

        final Button button = (Button) rowView.findViewById(R.id.carPoolButton);
        TextView tv = (TextView) rowView.findViewById(R.id.textView2);

        button.setText(buttonTextArray[position]);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext() , "This is my Toast message!"+ button.getText(),Toast.LENGTH_LONG).show();
                int stop = 1;
            }
        });



        return rowView;




    };

}
