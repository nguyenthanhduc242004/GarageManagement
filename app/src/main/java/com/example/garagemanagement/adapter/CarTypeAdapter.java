package com.example.garagemanagement.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagemanagement.AdminActivities.AdminEditCarTypeActivity;
import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.CarService;
import com.example.garagemanagement.Objects.CarType;
import com.example.garagemanagement.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarTypeAdapter extends RecyclerView.Adapter<CarTypeAdapter.CarTypeViewHolder> {
    public static final int TYPE_MANAGEMENT = 0;
    public static final int TYPE_CAR_SERVICE_PRICE = 1;
    public static final int TYPE_CAR_SERVICE_PRICE_UNEDITABLE = 2;
    private final int type;
    private final CarService carService;

    NumberFormat formatter = new DecimalFormat("#,###");

    private final RecyclerViewInterface recyclerViewInterface;
    private List<CarType> carTypes;
    Context context;

    public CarTypeAdapter(Context context, int type, CarService carService, RecyclerViewInterface recyclerViewInterface) {
        super();
        this.type = type;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
        this.carService = carService;
    }

    public void setData(List<CarType> carTypes) {
        this.carTypes = carTypes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @NonNull
    @Override
    public CarTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_MANAGEMENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_type_management, parent, false);
        }
        else if (viewType == TYPE_CAR_SERVICE_PRICE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_service_price, parent, false);
        }
        else if (viewType == TYPE_CAR_SERVICE_PRICE_UNEDITABLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_service_price, parent, false);
        }
        return new CarTypeAdapter.CarTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarTypeAdapter.CarTypeViewHolder holder, int position) {
        CarType carType = carTypes.get(position);
        if (carType == null) {
            return;
        }
        holder.tvCarTypeText.setText(carType.getCarTypeText());
        int type = holder.getItemViewType();
        if (type == TYPE_MANAGEMENT) {
            holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AdminEditCarTypeActivity.class);
                    intent.putExtra("CAR_TYPE_ID", carType.getCarTypeId());
                    intent.putExtra("CAR_TYPE_TEXT", carType.getCarTypeText());
                    context.startActivity(intent);

                }
            });
            holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConfirmationDialog.showConfirmationDialog(context, "Xác nhận xóa!",
                            "Bạn sẽ không thể hoàn tác sau khi thực hiện hành động này.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    Map<String, Object> carTypeData = new HashMap<>();
                                    carTypeData.put("carTypeText", carType.getCarTypeText());
                                    carTypeData.put("usable", false);
                                    db.collection("CarType")
                                            .document(carType.getCarTypeId())
                                            .update(carTypeData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context, "Xóa hãng xe thành công!", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Xóa hãng xe không thành công!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                }
            });
        }
        else if (type == TYPE_CAR_SERVICE_PRICE) {
            if (carService != null) {
                holder.etPrice.setText(String.valueOf(carService.getPrices().get(carType.getCarTypeId())));
            }
        }
        else if (type == TYPE_CAR_SERVICE_PRICE_UNEDITABLE) {
            holder.etPrice.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.VISIBLE);
            if (carService != null) {
                holder.tvPrice.setText(String.format("%sđ", formatter.format(carService.getPrices().get(carType.getCarTypeId()))));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (carTypes != null) {
            return carTypes.size();
        }
        return 0;
    }

    public class CarTypeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCarTypeText;
        private MaterialButton buttonEdit;
        private MaterialButton buttonDelete;
        private EditText etPrice;
        private TextView tvPrice;

        public CarTypeViewHolder(View itemView) {
            super(itemView);
            tvCarTypeText = itemView.findViewById(R.id.tvCarTypeText);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            etPrice = itemView.findViewById(R.id.etPrice);
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
