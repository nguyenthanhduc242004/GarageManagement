package com.example.garagemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.garagemanagement.R;

import java.util.List;

public class CarBrandAdapter extends ArrayAdapter<String> {
    public CarBrandAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_brand_selected, parent, false);
        TextView tvSelected = convertView.findViewById(R.id.tvSelected);
        if (this.getItem(position) != null) {
            tvSelected.setText(this.getItem(position));
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_brand, parent, false);
        TextView tvCarBrand = convertView.findViewById(R.id.tvCarBrand);
        if (this.getItem(position) != null) {
            tvCarBrand.setText(this.getItem(position));
        }
        return convertView;
    }
}
