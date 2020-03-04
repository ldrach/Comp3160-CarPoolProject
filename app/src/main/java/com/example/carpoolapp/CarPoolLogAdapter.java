package com.example.carpoolapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CarPoolLogAdapter extends RecyclerView.Adapter<CarPoolLogAdapter.CarPoolViewHolder> {
    List<carPoolLogDoc> allLogDoc = new ArrayList<>();

    public void setAllLogDoc(List<carPoolLogDoc> allLogDoc) {
        this.allLogDoc = allLogDoc;
    }

    @NonNull
    @Override
    public CarPoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.activity_car_pool_log_cell, parent, false);
        return new CarPoolViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarPoolViewHolder holder, int position) {
        carPoolLogDoc carPoolLogDoc = allLogDoc.get(position);
        holder.textViewLogDocs.setText(carPoolLogDoc.getCarPoolLogDoc());
    }

    @Override
    public int getItemCount() {
        return allLogDoc.size();
    }

    static class CarPoolViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLogDocs;
        public CarPoolViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLogDocs = itemView.findViewById(R.id.log_cell_doc);
        }
    }
}
