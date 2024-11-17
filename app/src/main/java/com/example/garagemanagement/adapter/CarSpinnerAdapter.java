package com.example.garagemanagement.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.R;

import java.util.List;

public class CarSpinnerAdapter extends ArrayAdapter<Car> {
    public CarSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Car> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_spinner_selected, parent, false);
        TextView tvLicensePlateAndCarBrand = convertView.findViewById(R.id.tvLicensePlateAndCarBrand);
        TextView tvOwnerName = convertView.findViewById(R.id.tvOwnerNameText);
        TextView tvCarSate = convertView.findViewById(R.id.tvSpinnerCarState);
        if (this.getItem(position) != null) {
            tvLicensePlateAndCarBrand.setText(String.format("%s - %s", this.getItem(position).getLicensePlate(), this.getItem(position).getCarBrand()));
            tvOwnerName.setText(this.getItem(position).getOwnerName());
            int state = this.getItem(position).getState();
            String stateText = "";
            if (state == 0) {
                stateText = "Mới tiếp nhận";
                tvCarSate.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.red));
            }
            else if (state == 1) {
                stateText = "Đang sửa chữa";
                tvCarSate.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.yellow));
            }
            else if (state == 2) {
                stateText = "Mới hoàn thành";
                tvCarSate.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.green));
            }
            // Không có state 3
            else if (state == 3) {
                stateText = "Đã thanh toán";
            }
            tvCarSate.setText(stateText);
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_spinner, parent, false);
        TextView tvLicensePlateAndCarBrand = convertView.findViewById(R.id.tvLicensePlateAndCarBrand);
        TextView tvOwnerName = convertView.findViewById(R.id.tvOwnerNameText);
        TextView tvCarSate = convertView.findViewById(R.id.tvSpinnerCarState);
        if (this.getItem(position) != null) {
            tvLicensePlateAndCarBrand.setText(String.format("%s - %s", this.getItem(position).getLicensePlate(), this.getItem(position).getCarBrand()));
            tvOwnerName.setText(this.getItem(position).getOwnerName());
            int state = this.getItem(position).getState();
            String stateText = "";
            if (state == 0) {
                stateText = "Mới tiếp nhận";
                tvCarSate.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.red));
            }
            else if (state == 1) {
                stateText = "Đang sửa chữa";
                tvCarSate.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.yellow));
            }
            else if (state == 2) {
                stateText = "Mới hoàn thành";
                tvCarSate.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.green));
            }
            else if (state == 3) {
                stateText = "Đã thanh toán";
            }
            tvCarSate.setText(stateText);
        }
        return convertView;
    }
}
