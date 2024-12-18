package com.example.garagemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.garagemanagement.Objects.CarBrand;
import com.example.garagemanagement.R;

import java.util.List;

public class CarBrandSpinnerAdapter extends ArrayAdapter<CarBrand> {
    public CarBrandSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<CarBrand> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_brand_selected, parent, false);
        TextView tvSelected = convertView.findViewById(R.id.tvSelected);
        if (this.getItem(position) != null) {
            tvSelected.setText(this.getItem(position).getCarBrandText());
            tvSelected.setTag(this.getItem(position));
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_brand, parent, false);
        TextView tvCarBrand = convertView.findViewById(R.id.tvCarBrandText);
        if (this.getItem(position) != null) {
            tvCarBrand.setText(this.getItem(position).getCarBrandText());
            tvCarBrand.setTag(this.getItem(position));
        }
        return convertView;
    }
}
