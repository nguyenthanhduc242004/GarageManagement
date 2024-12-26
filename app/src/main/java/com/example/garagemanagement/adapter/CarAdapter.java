package com.example.garagemanagement.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagemanagement.AddRepairCardActivity;
import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.R;
import com.example.garagemanagement.RepairingCarDetailActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    public static final int TYPE_CAR_HOME = 0;
    public static final int TYPE_CAR_LIST = 1;
    public static final int TYPE_CAR_PAID = 2;
    private final int type;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
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
        else if (viewType == TYPE_CAR_PAID) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_list, parent, false);
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

                holder.homeCarUpperButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, AddRepairCardActivity.class);
                        intent.putExtra("CAR_ID", car.getCarId());
                        startActivity(context, intent, new Bundle());
                    }
                });

                holder.homeCarLowerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ConfirmationDialog.showConfirmationDialog(context, "Xác nhận xóa!",
                                "Bạn sẽ không thể hoàn tác sau khi thực hiện!",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        db.collection("Car")
                                                .document(car.getCarId())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(context, "Xóa xe thành công!", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, "Xóa xe không thành công!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                    }
                });
            }
            else if (state == Car.STATE_REPAIRING) {
                holder.homeCarUpperButton.setText("Sửa Xong");
                holder.homeCarLowerButton.setVisibility(View.GONE);
                holder.homeCarUpperButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
                holder.homeCarUpperButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24, 0, 0, 0);

                holder.homeCarUpperButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ConfirmationDialog.showConfirmationDialog(context, "Xác nhận?",
                                "Bạn có chắc xe này đã sửa xong?",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        db.collection("Car")
                                                .document(car.getCarId())
                                                .update("state", 2);
                                    }
                                });

                    }
                });
            }
            else if (state == Car.STATE_COMPLETED) {
                holder.homeCarLowerButton.setVisibility(View.GONE);
                holder.homeCarUpperButton.setText("Thanh Toán");
                holder.homeCarUpperButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
                holder.homeCarUpperButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_payment_24, 0, 0, 0);

                holder.homeCarUpperButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ConfirmationDialog.showConfirmationDialog(context, "Xác nhận?",
                                "Bạn có chắc đã thanh toán xe này?",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, Object> carData = new HashMap<>();
                                        carData.put("licensePlate", car.getLicensePlate());
                                        carData.put("carBrandId", car.getCarBrandId());
                                        carData.put("carTypeId", car.getCarTypeId());
                                        carData.put("ownerName", car.getOwnerName());
                                        carData.put("phoneNumber", car.getPhoneNumber());
                                        carData.put("receiveDate", car.getReceiveDate());
                                        carData.put("carImage", 0);
                                        carData.put("state", 3);
                                        carData.put("carServices", car.getCarServices());
                                        carData.put("carSupplies", car.getCarSupplies());
                                        carData.put("paymentDate", new Date());
                                        db.collection("Car")
                                                .document(car.getCarId())
                                                .update(carData);
                                    }
                                });
                    }
                });
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
        else if (type == TYPE_CAR_PAID) {
            holder.tvReceiveDate.setText(formatter.format(car.getReceiveDate()));
            holder.tvPaymentDate.setText(formatter.format(car.getPaymentDate()));
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
        private TextView tvPaymentDate;
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
            tvPaymentDate = itemView.findViewById(R.id.tvPaymentDate);

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
            } else if (type == TYPE_CAR_PAID) {
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
