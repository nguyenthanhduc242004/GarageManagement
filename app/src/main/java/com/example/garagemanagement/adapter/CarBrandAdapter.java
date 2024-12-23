package com.example.garagemanagement.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagemanagement.AdminActivities.AdminEditCarBrandActivity;
import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.CarBrand;
import com.example.garagemanagement.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarBrandAdapter extends RecyclerView.Adapter<CarBrandAdapter.CarBrandViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private List<CarBrand> carBrands;
    Context context;

    public CarBrandAdapter(Context context, RecyclerViewInterface recyclerViewInterface) {
        super();
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setData(List<CarBrand> carBrands) {
        this.carBrands = carBrands;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarBrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarBrandViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_brand_management, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CarBrandAdapter.CarBrandViewHolder holder, int position) {
        CarBrand carBrand = carBrands.get(position);
        if (carBrand == null) {
            return;
        }
        holder.tvCarBrand.setText(carBrand.getCarBrandText());
        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminEditCarBrandActivity.class);
                intent.putExtra("CAR_BRAND_ID", carBrand.getCarBrandId());
                intent.putExtra("CAR_BRAND_TEXT", carBrand.getCarBrandText());
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
                                Map<String, Object> carBrandData = new HashMap<>();
                                carBrandData.put("carBrandText", carBrand.getCarBrandText());
                                carBrandData.put("usable", false);
                                db.collection("CarBrand")
                                        .document(carBrand.getCarBrandId())
                                        .update(carBrandData)
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

    @Override
    public int getItemCount() {
        if (carBrands != null) {
            return carBrands.size();
        }
        return 0;
    }

    public class CarBrandViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCarBrand;
        private MaterialButton buttonEdit;
        private MaterialButton buttonDelete;

        public CarBrandViewHolder(View itemView) {
            super(itemView);
            tvCarBrand = itemView.findViewById(R.id.tvCarBrand);
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
