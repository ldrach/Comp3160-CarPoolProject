package com.example.carpoolapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class mainActivityListAdapter extends ArrayAdapter<String> {


    private final Activity context;
    private final String[] maintitle;
    private final String[] subtitle;
    private final Integer[] imgid;

    public mainActivityListAdapter(Activity context, String[] maintitle,String[] driveCount, Integer[] imgid) {
        super(context, R.layout.list_item, maintitle);

        ArrayList<String[]> array = new ArrayList<>();
       //TODO sort items
        // array.add(driveCount);

        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=driveCount;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(maintitle[position]);
        imageView.setImageResource(imgid[position]);
        subtitleText.setText(subtitle[position]);

        return rowView;

    };
//    public static void sortbyColumn(int[][] arr, int col)
//    {
//        // Using built-in sort function Arrays.sort
//        Arrays.sort(arr, new Comparator<int[]>() {
//
//            @Override
//            // Compare values according to columns
//            public int compare(final int[] entry1,
//                               final int[] entry2) {
//
//                // To sort in descending order revert
//                // the '>' Operator
//                if (entry1[col] > entry2[col])
//                    return 1;
//                else
//                    return -1;
//            }
//        });  // End of function call sort().
//    }
}
