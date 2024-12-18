package com.example.garagemanagement.adapter;

import android.content.Context;
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
import com.example.garagemanagement.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    public static final int TYPE_CAR_HOME = 0;
    public static final int TYPE_CAR_LIST = 1;
    private final int type;

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private List<Car> cars;
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;

    public CarAdapter(Context context, int type, RecyclerViewInterface recyclerViewInterface) {
        super();
        this.context = context;
        this.type = type;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setData(List<Car> cars) {
        this.cars = cars;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_CAR_HOME) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_home, parent, false);
        }
        else if (viewType == TYPE_CAR_LIST) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_list, parent, false);
        }
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = cars.get(position);
        if (car == null) {
            return;
        }
        holder.state = car.getState();
        holder.tvLicensePlate.setText(car.getLicensePlate());
        holder.tvOwnerName.setText(car.getOwnerName());
        int type = holder.getItemViewType();
        if (type == TYPE_CAR_HOME) {
            holder.ivCarImage.setImageResource(car.getCarImage());
            holder.tvCarBrand.setText(car.getCarBrandText());
            int carImage = car.getCarImage();
            if (carImage == 0) {
                holder.ivCarImage.setImageResource(R.drawable.no_image);
            } else {
                holder.ivCarImage.setImageResource(carImage);
            }
            int state = car.getState();
            if (state == Car.STATE_NEW) {
                holder.homeCarUpperButton.setText("Sửa Chữa");
                holder.homeCarLowerButton.setText("Xóa");
                holder.homeCarUpperButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
                holder.homeCarLowerButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red));
                holder.homeCarUpperButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_home_repair_service_24, 0, 0, 0);
                holder.homeCarLowerButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_delete_outline_24, 0, 0, 0);
            }
            else if (state == Car.STATE_REPAIRING) {
                holder.homeCarUpperButton.setText("Sửa Xong");
                holder.homeCarLowerButton.setVisibility(View.GONE);
                holder.homeCarUpperButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
                holder.homeCarUpperButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24, 0, 0, 0);
            }
            else if (state == Car.STATE_COMPLETED) {
                holder.homeCarLowerButton.setVisibility(View.GONE);
                holder.homeCarUpperButton.setText("Thanh Toán");
                holder.homeCarUpperButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
                holder.homeCarUpperButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_payment_24, 0, 0, 0);
            }
        }
        else if (type == TYPE_CAR_LIST) {
            holder.tvReceiveDate.setText(formatter.format(car.getReceiveDate()));
            String carStateText = "aString";
            if (holder.state == 0) {
                carStateText = "Mới tiếp nhận";
                holder.tvCarState.setTextColor(ContextCompat.getColor(context, R.color.red));
            } else if (holder.state == 1) {
                carStateText = "Đang sửa";
                holder.tvCarState.setTextColor(ContextCompat.getColor(context, R.color.yellow));
            } else if (holder.state == 2) {
                carStateText = "Mới hoàn thành";
                holder.tvCarState.setTextColor(ContextCompat.getColor(context, R.color.green));
            }
            holder.tvCarState.setText(carStateText);
        }
    }

    @Override
    public int getItemCount() {
        if (cars != null) {
            return cars.size();
        }
        return 0;
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCarImage;
        private TextView tvLicensePlate;
        private TextView tvCarBrand;
        private TextView tvOwnerName;
        private TextView tvReceiveDate;
        private Button homeCarUpperButton;
        private Button homeCarLowerButton;
        private TextView tvCarState;
        private int state;

        public CarViewHolder(View itemView) {
            super(itemView);
            ivCarImage = itemView.findViewById(R.id.ivCarImage);
            tvLicensePlate = itemView.findViewById(R.id.tvLicensePlate);
            tvCarBrand = itemView.findViewById(R.id.tvCarBrand);
            tvOwnerName = itemView.findViewById(R.id.tvOwnerName);
            tvReceiveDate = itemView.findViewById(R.id.tvReceiveDate);
            homeCarUpperButton = itemView.findViewById(R.id.homeCarUpperButton);
            homeCarLowerButton = itemView.findViewById(R.id.homeCarLowerButton);
            tvCarState = itemView.findViewById(R.id.tvCarState);

            if (type == TYPE_CAR_HOME) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (recyclerViewInterface != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                recyclerViewInterface.onItemClick(position, state);
                            }
                        }
                    }
                });
            } else if (type == TYPE_CAR_LIST) {
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
}
