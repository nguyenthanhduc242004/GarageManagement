package com.example.garagemanagement.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagemanagement.AdminActivities.AdminEditCarSupplyActivity;
import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.CarService;
import com.example.garagemanagement.Objects.CarSupply;
import com.example.garagemanagement.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarSupplyAdapter extends RecyclerView.Adapter<CarSupplyAdapter.CarSupplyViewHolder> {
    public static final int TYPE_LIST = 0;
    public static final int TYPE_DIALOG = 1;
    public static final int TYPE_MANAGEMENT = 2;
    private final int type;

    private final RecyclerViewInterface recyclerViewInterface;
    private List<CarSupply> carSupplies;
    Context context;

    NumberFormat formatter = new DecimalFormat("#,###");


    public CarSupplyAdapter(Context context, int type, RecyclerViewInterface recyclerViewInterface) {
        super();
        this.context = context;
        this.type = type;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setData(List<CarSupply> carSupplies) {
        this.carSupplies = carSupplies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @NonNull
    @Override
    public CarSupplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_DIALOG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_car_supply_item, parent, false);
        }
        else if (viewType == TYPE_LIST) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_supply, parent, false);
        }
        else if (viewType == TYPE_MANAGEMENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_supply_management, parent, false);
        }
        return new CarSupplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarSupplyAdapter.CarSupplyViewHolder holder, int position) {
        CarSupply carSupply = carSupplies.get(position);
        if (carSupply == null) {
            return;
        }
        int type = holder.getItemViewType();
        if (type == TYPE_LIST) {
            holder.tvSupplyId.setText(String.valueOf(position + 1));
            holder.tvSupplyName.setText(carSupply.getSupplyName());
            holder.tvPrice.setText(String.format("%sđ", formatter.format(carSupply.getPrice())));
            holder.tvSupplyQuantity.setText(String.valueOf(carSupply.getQuantity()));
        }
        else if (type == TYPE_DIALOG) {
            holder.tvSupplyName.setText(carSupply.getSupplyName());
            holder.tvSupplyQuantity.setText(String.valueOf(carSupply.getQuantity()));
            if (!holder.tvSupplyQuantity.getText().toString().equals(String.valueOf(0))) {
                holder.minusButton.setColorFilter(Color.argb(255, 255, 255, 255));
            }
            holder.minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String supplyQuantityString = holder.tvSupplyQuantity.getText().toString();
                    if (supplyQuantityString.equals(String.valueOf(1))) {
                        holder.minusButton.setColorFilter(Color.argb(255, 51, 51, 51));
                    }
                    if (!supplyQuantityString.equals(String.valueOf(0))) {
                        holder.tvSupplyQuantity.setText(String.valueOf(Integer.parseInt(supplyQuantityString) - 1));
                    }
                }
            });
            holder.plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String supplyQuantityString = holder.tvSupplyQuantity.getText().toString();
                    if (supplyQuantityString.equals(String.valueOf(0))) {
                        holder.minusButton.setColorFilter(Color.argb(255, 255, 255, 255));
                    }
                    holder.tvSupplyQuantity.setText(String.valueOf(Integer.parseInt(supplyQuantityString) + 1));
                }
            });
        }
        else if (type == TYPE_MANAGEMENT) {
            holder.tvSupplyName.setText(carSupply.getSupplyName());
            holder.tvPrice.setText(String.format("%sđ", formatter.format(carSupply.getPrice())));
            holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AdminEditCarSupplyActivity.class);
                    intent.putExtra("CAR_SUPPLY_ID", carSupply.getSupplyId());
                    intent.putExtra("CAR_SUPPLY_TEXT", carSupply.getSupplyName());
                    intent.putExtra("PRICE", carSupply.getPrice());
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
                                    Map<String, Object> carSupplyData = new HashMap<>();
                                    carSupplyData.put("supplyName", carSupply.getSupplyName());
                                    carSupplyData.put("price", carSupply.getPrice());
                                    carSupplyData.put("usable", false);
                                    db.collection("CarSupply")
                                            .document(carSupply.getSupplyId())
                                            .update(carSupplyData)
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
    }

    @Override
    public int getItemCount() {
        if (carSupplies != null) {
            return carSupplies.size();
        }
        return 0;
    }

    public class CarSupplyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSupplyId;
        private TextView tvSupplyName;
        private TextView tvPrice;;
        public TextView tvSupplyQuantity;
        private ImageButton minusButton;
        private ImageButton plusButton;
        MaterialButton buttonEdit;
        MaterialButton buttonDelete;

        public CarSupplyViewHolder(View itemView) {
            super(itemView);
            tvSupplyId = itemView.findViewById(R.id.tvSupplyId);
            tvSupplyName = itemView.findViewById(R.id.tvSupplyName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSupplyQuantity = itemView.findViewById(R.id.tvSupplyQuantity);
            minusButton = itemView.findViewById(R.id.minusButton);
            plusButton = itemView.findViewById(R.id.plusButton);
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
