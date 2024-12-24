package com.example.garagemanagement.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
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

import com.example.garagemanagement.AdminActivities.AdminEditCarBrandActivity;
import com.example.garagemanagement.AdminActivities.AdminEditCarServiceActivity;
import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.Objects.CarService;
import com.example.garagemanagement.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarServiceAdapter extends RecyclerView.Adapter<CarServiceAdapter.CarServiceViewHolder> {
    public static final int TYPE_LIST = 0;
    public static final int TYPE_MANAGEMENT = 1;
    public static final int TYPE_CAR_TYPE_PRICE = 2;
    private final int type;
    private final String carTypeId;

    private final RecyclerViewInterface recyclerViewInterface;
    private List<CarService> carServices;
    Context context;

    NumberFormat formatter = new DecimalFormat("#,###");

    public CarServiceAdapter(Context context, int type, String carTypeId, RecyclerViewInterface recyclerViewInterface) {
        super();
        this.type = type;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
        this.carTypeId = carTypeId;
    }

    public void setData(List<CarService> carServices) {
        this.carServices = carServices;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @NonNull
    @Override
    public CarServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_LIST) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_service, parent, false);
        }
        else if (viewType == TYPE_MANAGEMENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_service_management, parent, false);
        } else if (viewType == TYPE_CAR_TYPE_PRICE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_type_price, parent, false);
        }
        return new CarServiceAdapter.CarServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarServiceAdapter.CarServiceViewHolder holder, int position) {
        CarService carService = carServices.get(position);
        if (carService == null) {
            return;
        }
        holder.tvServiceName.setText(carService.getServiceName());
        int type = holder.getItemViewType();
        if (type == TYPE_LIST) {
            holder.tvServiceId.setText(String.valueOf(position + 1));
            holder.tvPrice.setText(String.format("%sđ", formatter.format(carService.getPrices().get(carTypeId))));
        } else if (type == TYPE_MANAGEMENT) {
            holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AdminEditCarServiceActivity.class);
                    intent.putExtra("CAR_SERVICE", carService);
                    context.startActivity(intent);
                }
            });
            holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ConfirmationDialog.showConfirmationDialog(context, "Xác nhận xóa!",
                                    "Bạn sẽ không thể hoàn tác sau khi thực hiện hành động này.", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            Map<String, Object> carServiceData = new HashMap<>();
                                            carServiceData.put("serviceName", carService.getServiceName());
                                            carServiceData.put("prices", carService.getPrices());
                                            carServiceData.put("usable", false);
                                            db.collection("CarService")
                                                    .document(carService.getServiceId())
                                                    .update(carServiceData)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(context, "Xóa dịch vụ thành công!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Xóa dịch vụ không thành công!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Perform actions when the user confirms
                                            // (e.g., delete data, proceed with an action)
                                        }
                                    });
                        }
                    });
                }
            });
        } else if (type == TYPE_CAR_TYPE_PRICE) {
            holder.tvPrice.setText(String.format("%sđ", formatter.format(carService.getPrices().get(carTypeId))));
        }
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
        private MaterialButton buttonEdit;
        private MaterialButton buttonDelete;

        public CarServiceViewHolder(View itemView) {
            super(itemView);
            tvServiceId = itemView.findViewById(R.id.tvServiceId);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

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
