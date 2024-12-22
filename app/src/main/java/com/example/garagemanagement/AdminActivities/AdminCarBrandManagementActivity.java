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
import com.example.garagemanagement.R;
import com.example.garagemanagement.adapter.CarBrandAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AdminCarBrandManagementActivity extends AppCompatActivity implements RecyclerViewInterface {
    CarBrandAdapter carBrandAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_car_brand_management);
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

        CarBrand carBrand1 = new CarBrand("1", "Honda");
        CarBrand carBrand2 = new CarBrand("1", "Aston Martin");
        CarBrand carBrand3 = new CarBrand("1", "Suzuki");
        CarBrand carBrand4 = new CarBrand("1", "Vinfast");
        List<CarBrand> carBrands = List.of(carBrand1, carBrand2, carBrand3, carBrand4);

        carBrandAdapter = new CarBrandAdapter(this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerViewCarBrandManagement = findViewById(R.id.recyclerViewCarBrandManagement);
        recyclerViewCarBrandManagement.setLayoutManager(linearLayoutManager);
        recyclerViewCarBrandManagement.setFocusable(false);
        recyclerViewCarBrandManagement.setAdapter(carBrandAdapter);
        carBrandAdapter.setData(carBrands);

        MaterialButton buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCarBrandManagementActivity.this, AdminAddCarBrandActivity.class);
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