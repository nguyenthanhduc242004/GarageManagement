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
import com.example.garagemanagement.Objects.CarType;
import com.example.garagemanagement.R;
import com.example.garagemanagement.adapter.CarTypeAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AdminCarTypeManagementActivity extends AppCompatActivity implements RecyclerViewInterface {
    CarTypeAdapter carTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_car_type_management);
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

        CarType carType1 = new CarType("1", "Mini");
        CarType carType2 = new CarType("2", "Sedan");
        CarType carType3 = new CarType("3", "SUV");
        CarType carType4 = new CarType("4", "Luxury");
        List<CarType> carTypes = List.of(carType1, carType2, carType3, carType4);

        carTypeAdapter = new CarTypeAdapter(this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerViewCarTypeManagement = findViewById(R.id.recyclerViewCarTypeManagement);
        recyclerViewCarTypeManagement.setLayoutManager(linearLayoutManager);
        recyclerViewCarTypeManagement.setFocusable(false);
        recyclerViewCarTypeManagement.setAdapter(carTypeAdapter);
        carTypeAdapter.setData(carTypes);

        MaterialButton buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCarTypeManagementActivity.this, AdminAddCarTypeActivity.class);
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