package com.example.garagemanagement.adapter;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.Objects.CarService;
import com.example.garagemanagement.R;

import java.util.List;

public class CarServiceAdapter extends RecyclerView.Adapter<CarServiceAdapter.CarServiceViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private List<CarService> carServices;
    Context context;

    NumberFormat formatter = new DecimalFormat("#,###");


    public CarServiceAdapter(Context context, RecyclerViewInterface recyclerViewInterface) {
        super();
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setData(List<CarService> carServices) {
        this.carServices = carServices;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarServiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_service, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CarServiceAdapter.CarServiceViewHolder holder, int position) {
        CarService carService = carServices.get(position);
        if (carService == null) {
            return;
        }
        holder.tvServiceId.setText(carService.getServiceId());
        holder.tvServiceName.setText(carService.getServiceName());
        holder.tvPrice.setText(formatter.format(carService.getPrice()) + "đ");
    }

    @Override
    public int getItemCount() {
        if (carServices != null) {
            return carServices.size();
        }
        return 0;
    }

    public class CarServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvServiceId;
        private TextView tvServiceName;
        private TextView tvPrice;;

        public CarServiceViewHolder(View itemView) {
            super(itemView);
            tvServiceId = itemView.findViewById(R.id.tvServiceId);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
