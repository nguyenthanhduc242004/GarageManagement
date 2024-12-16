package com.example.garagemanagement.adapter;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.CarService;
import com.example.garagemanagement.Objects.CarSupply;
import com.example.garagemanagement.R;

import java.util.List;

public class CarSupplyAdapter extends RecyclerView.Adapter<CarSupplyAdapter.CarSupplyViewHolder> {
    public static final int TYPE_LIST = 0;
    public static final int TYPE_DIALOG = 1;
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
            holder.tvSupplyId.setText(carSupply.getSupplyId());
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

        public CarSupplyViewHolder(View itemView) {
            super(itemView);
            tvSupplyId = itemView.findViewById(R.id.tvSupplyId);
            tvSupplyName = itemView.findViewById(R.id.tvSupplyName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSupplyQuantity = itemView.findViewById(R.id.tvSupplyQuantity);
            minusButton = itemView.findViewById(R.id.minusButton);
            plusButton = itemView.findViewById(R.id.plusButton);

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
