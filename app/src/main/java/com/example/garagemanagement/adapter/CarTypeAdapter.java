package com.example.garagemanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagemanagement.AdminActivities.AdminEditCarTypeActivity;
import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.CarType;
import com.example.garagemanagement.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CarTypeAdapter extends RecyclerView.Adapter<CarTypeAdapter.CarTypeViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private List<CarType> carTypes;
    Context context;

    public CarTypeAdapter(Context context, RecyclerViewInterface recyclerViewInterface) {
        super();
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setData(List<CarType> carTypes) {
        this.carTypes = carTypes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarTypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_type_management, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CarTypeAdapter.CarTypeViewHolder holder, int position) {
        CarType carType = carTypes.get(position);
        if (carType == null) {
            return;
        }
        holder.tvCarType.setText(carType.getCarTypeText());
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
                // TODO: DO SOMETHING HERE!!!
            }
        });
    }

    @Override
    public int getItemCount() {
        if (carTypes != null) {
            return carTypes.size();
        }
        return 0;
    }

    public class CarTypeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCarType;
        private MaterialButton buttonEdit;
        private MaterialButton buttonDelete;

        public CarTypeViewHolder(View itemView) {
            super(itemView);
            tvCarType = itemView.findViewById(R.id.tvCarType);
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
