package com.example.garagemanagement.AdminActivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.CarBrand;
import com.example.garagemanagement.Objects.CarSupply;
import com.example.garagemanagement.R;
import com.example.garagemanagement.adapter.CarBrandAdapter;
import com.example.garagemanagement.adapter.CarSupplyAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AdminCarSupplyManagementActivity extends AppCompatActivity implements RecyclerViewInterface {
    CarSupplyAdapter carSupplyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_car_supply_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        BACK BUTTON:
        ImageButton imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(view -> {
            finish();
        });

        CarSupply carSupply1 = new CarSupply("1", "Dung dịch phụ gia súc béc xăng (Wurth)", 300000);
        CarSupply carSupply2 = new CarSupply("2", "Dung dịch phụ gia súc nhớt (Wurth)", 300000);
        CarSupply carSupply3 = new CarSupply("3", "Dung dịch vệ sinh kim phun (Wurth)", 350000);
        CarSupply carSupply4 = new CarSupply("4", "Nước làm mát (Asin, Jinco)", 150000);
        CarSupply carSupply5 = new CarSupply("5", "Còi Denso", 500000);
        CarSupply carSupply6 = new CarSupply("6", "Gạt mưa Bosch cứng", 350000);
        CarSupply carSupply7 = new CarSupply("7", "Gạt mưa Bosch mềm", 600000);
        List<CarSupply> carSupplies = List.of(carSupply1, carSupply2, carSupply3, carSupply4, carSupply5, carSupply6, carSupply7);

        carSupplyAdapter = new CarSupplyAdapter(this, CarSupplyAdapter.TYPE_MANAGEMENT, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerViewCarSupplyManagement = findViewById(R.id.recyclerViewCarSupplyManagement);
        recyclerViewCarSupplyManagement.setLayoutManager(linearLayoutManager);
        recyclerViewCarSupplyManagement.setFocusable(false);
        recyclerViewCarSupplyManagement.setAdapter(carSupplyAdapter);
        carSupplyAdapter.setData(carSupplies);

        MaterialButton buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCarSupplyManagementActivity.this, AdminAddCarSupplyActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(int position, int state) {

    }
}