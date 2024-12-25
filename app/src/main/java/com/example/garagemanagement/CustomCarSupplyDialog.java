package com.example.garagemanagement;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.CarService;
import com.example.garagemanagement.Objects.CarSupply;
import com.example.garagemanagement.adapter.CarSupplyAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustomCarSupplyDialog extends AppCompatDialogFragment implements RecyclerViewInterface {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CustomCarSupplyDialogInterface customCarSupplyDialogInterface;
    public static List<CarSupply> allCarSupplies = new ArrayList<>();
    ProgressDialog progressDialog;

    public CarSupplyAdapter carSupplyAdapter;
    public static List<CarSupply> selectedCarSupplies = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_car_supply, null);

        carSupplyAdapter = new CarSupplyAdapter(getContext(), CarSupplyAdapter.TYPE_DIALOG,this);
        RecyclerView recyclerViewCarSupplyList = view.findViewById(R.id.recyclerViewCarSupplyDialogList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewCarSupplyList.setLayoutManager(linearLayoutManager);
        recyclerViewCarSupplyList.setFocusable(false);
        recyclerViewCarSupplyList.setAdapter(carSupplyAdapter);

        List<CarSupply> editableAllCarSupplies = allCarSupplies;
        for (int i = 0; i < selectedCarSupplies.size(); i++) {
            for (int j = 0; j < editableAllCarSupplies.size(); j++) {
                if (selectedCarSupplies.get(i).getSupplyId().equals(editableAllCarSupplies.get(j).getSupplyId())) {
                    editableAllCarSupplies.get(j).setQuantity(selectedCarSupplies.get(i).getQuantity());
                }
            }
        }

        carSupplyAdapter.setData(editableAllCarSupplies);



        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(view)
                .setTitle("Chọn Vật Tư, Dung Dịch")
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        List<CarSupply> newlySelectedCarSupplies = new ArrayList<>();
                        long totalPrice = 0;
                        for (int i = 0; i < editableAllCarSupplies.size(); i++) {
                            int quantity = Integer.parseInt(((CarSupplyAdapter.CarSupplyViewHolder)recyclerViewCarSupplyList.findViewHolderForAdapterPosition(i)).tvSupplyQuantity.getText().toString());
                            editableAllCarSupplies.get(i).setQuantity(quantity);
                            totalPrice += editableAllCarSupplies.get(i).getPrice() * quantity;
                            if (quantity > 0) {
                                newlySelectedCarSupplies.add(editableAllCarSupplies.get(i));
                            }
                        }
                        carSupplyAdapter.setData(editableAllCarSupplies);
                        customCarSupplyDialogInterface.setCarSupplyTotalPrice(totalPrice);
                        customCarSupplyDialogInterface.setCarSupplyAdapterData(newlySelectedCarSupplies);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        customCarSupplyDialogInterface = (CustomCarSupplyDialogInterface) context;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(int position, int state) {

    }

    public interface CustomCarSupplyDialogInterface {
        void setCarSupplyTotalPrice(long totalPrice);
        void setCarSupplyAdapterData(List<CarSupply> carSupplies);
    }

    public CarSupplyAdapter getCarSupplyAdapter() {
        return carSupplyAdapter;
    }
}
