package com.example.garagemanagement;

import android.app.Dialog;
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

import java.util.ArrayList;
import java.util.List;

public class CustomCarSupplyDialog extends AppCompatDialogFragment implements RecyclerViewInterface {
    CustomCarSupplyDialogInterface customCarSupplyDialogInterface;
    public static List<CarSupply> allCarSupplies = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_car_supply, null);

        // FAKE CALL API SUPPLY LIST:
        CarSupply carSupply1 = new CarSupply("1", "Dung dịch phụ gia súc béc xăng (Wurth)", 300000);
        CarSupply carSupply2 = new CarSupply("2", "Dung dịch phụ gia súc nhớt (Wurth)", 300000);
        CarSupply carSupply3 = new CarSupply("3", "Dung dịch vệ sinh kim phun (Wurth)", 350000);
        CarSupply carSupply4 = new CarSupply("4", "Nước làm mát (Asin, Jinco)", 150000);
        CarSupply carSupply5 = new CarSupply("5", "Còi Denso", 500000);
        CarSupply carSupply6 = new CarSupply("6", "Gạt mưa Bosch cứng", 350000);
        CarSupply carSupply7 = new CarSupply("7", "Gạt mưa Bosch mềm", 600000);
        List<CarSupply> carSupplies = List.of(carSupply1, carSupply2, carSupply3, carSupply4, carSupply5, carSupply6, carSupply7);

        List<CarSupply> selectedCarSupplies = new ArrayList<>();
        if (!AddRepairCardActivity.selectedCarSupplies.isEmpty()) {
            selectedCarSupplies = AddRepairCardActivity.selectedCarSupplies;
        } else if (!UpdateRepairingCarActivity.selectedCarSupplies.isEmpty()) {
            selectedCarSupplies = UpdateRepairingCarActivity.selectedCarSupplies;
        }

        for (int i = 0; i < selectedCarSupplies.size(); i++) {
            for (int j = 0; j < carSupplies.size(); j++) {
                if (selectedCarSupplies.get(i).getSupplyId().equals(carSupplies.get(j).getSupplyId())) {
                    carSupplies.get(j).setQuantity(selectedCarSupplies.get(i).getQuantity());
                }
            }
        }

        CarSupplyAdapter carSupplyAdapter = new CarSupplyAdapter(getContext(), CarSupplyAdapter.TYPE_DIALOG,this);
        RecyclerView recyclerViewCarSupplyList = view.findViewById(R.id.recyclerViewCarSupplyDialogList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewCarSupplyList.setLayoutManager(linearLayoutManager);
        recyclerViewCarSupplyList.setFocusable(false);
        carSupplyAdapter.setData(carSupplies);
        recyclerViewCarSupplyList.setAdapter(carSupplyAdapter);

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
                        List<CarSupply> selectedCarSupplies = new ArrayList<>();
                        long totalPrice = 0;
                        for (int i = 0; i < carSupplies.size(); i++) {
                            int quantity = Integer.parseInt(((CarSupplyAdapter.CarSupplyViewHolder)recyclerViewCarSupplyList.findViewHolderForAdapterPosition(i)).tvSupplyQuantity.getText().toString());
                            if (quantity > 0) {
                                carSupplies.get(i).setQuantity(quantity);
                                selectedCarSupplies.add(carSupplies.get(i));
                                totalPrice += carSupplies.get(i).getPrice() * quantity;
                            }
                        }
                        carSupplyAdapter.setData(carSupplies);
                        customCarSupplyDialogInterface.setCarSupplyTotalPrice(totalPrice);
                        customCarSupplyDialogInterface.setCarSupplyAdapterData(selectedCarSupplies);
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
}
