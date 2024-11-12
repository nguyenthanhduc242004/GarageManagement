package com.example.garagemanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class CarDetailAdapter extends RecyclerView.Adapter<CarDetailAdapter.CarViewHolder> {
    private List<Car> cars;

    public void setData(List<Car> cars) {
        this.cars = cars;
        notifyDataSetChanged();
    }

    public void fakeNotifyDataSetChanged() {
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_detail, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = cars.get(position);
        if (car == null) {
            return;
        }
        holder.tvOwnerName.setText(car.getOwnerName());
        holder.tvLicensePlate.setText(car.getLicensePlate());
        holder.tvCarBrand.setText(car.getCarBrand());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        holder.tvReceiveDate.setText(formatter.format(car.getReceiveDate()));
    }

    @Override
    public int getItemCount() {
        if (cars != null) {
            return cars.size();
        }
        return 0;
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOwnerName;
        private TextView tvLicensePlate;
        private TextView tvCarBrand;
        private TextView tvReceiveDate;

        public CarViewHolder(View itemView) {
            super(itemView);
            tvOwnerName = itemView.findViewById(R.id.tvOwnerName);
            tvLicensePlate = itemView.findViewById(R.id.tvLicensePlate);
            tvCarBrand = itemView.findViewById(R.id.tvCarBrand);
            tvReceiveDate = itemView.findViewById(R.id.tvReceiveDate);
        }
    }
}
