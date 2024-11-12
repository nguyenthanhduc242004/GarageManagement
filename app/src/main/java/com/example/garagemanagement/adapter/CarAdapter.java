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

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private static int TYPE_CAR_NEW = 0;
    private static int TYPE_CAR_REPAIRING = 1;
    private static int TYPE_CAR_COMPLETED = 2;

    private List<Car> cars;

    public void setData(List<Car> cars) {
        this.cars = cars;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Car car = cars.get(position);
        if (car.getStatus() == 0) {
            return TYPE_CAR_NEW;
        } else if (car.getStatus() == 1) {
            return TYPE_CAR_REPAIRING;
        } else if (car.getStatus() == 2) {
            return TYPE_CAR_COMPLETED;
        }
        return -1;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAR_NEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_car, parent, false);
            return new CarViewHolder(view);
        } else if (viewType == TYPE_CAR_REPAIRING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repairing_car, parent, false);
            return new CarViewHolder(view);
        } else if (viewType == TYPE_CAR_COMPLETED) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_completed_car, parent, false);
            return new CarViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = cars.get(position);
        if (car == null) {
            return;
        }
//        holder.imageView.setImageResource(car.getPicture());
        holder.tvCar.setText(String.format("%s - %s", car.getCarBrand(), car.getLicensePlate()));
        holder.tvOwnerName.setText(car.getOwnerName());
        holder.tvPhone.setText(car.getPhoneNumber());

//        if (holder.getItemViewType() == TYPE_CAR_NEW) {
//
//        } else if (holder.getItemViewType() == TYPE_CAR_REPAIRING) {
//
//        } else if (holder.getItemViewType() == TYPE_CAR_COMPLETED) {
//
//        }
    }

    @Override
    public int getItemCount() {
        if (cars != null) {
            return cars.size();
        }
        return 0;
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tvCar;
        private TextView tvOwnerName;
        private TextView tvPhone;

        public CarViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tvCar = itemView.findViewById(R.id.tvCar);
            tvOwnerName = itemView.findViewById(R.id.tvOwnerName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
        }
    }
}
