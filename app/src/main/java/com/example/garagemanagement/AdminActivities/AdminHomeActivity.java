package com.example.garagemanagement.AdminActivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.garagemanagement.LoginActivity;
import com.example.garagemanagement.R;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonCarBrandManagement = findViewById(R.id.buttonCarBrandManagement);
        buttonCarBrandManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminCarBrandManagementActivity.class);
            startActivity(intent);
        });

        Button buttonCarTypeManagement = findViewById(R.id.buttonCarTypeManagement);
        buttonCarTypeManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminCarTypeManagementActivity.class);
            startActivity(intent);
        });

        Button buttonCarServiceManagement = findViewById(R.id.buttonCarServiceManagement);
        buttonCarServiceManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminCarServiceManagementActivity.class);
            startActivity(intent);
        });

        Button buttonCarSupplyManagement = findViewById(R.id.buttonCarSupplyManagement);
        buttonCarSupplyManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminCarSupplyManagementActivity.class);
            startActivity(intent);
        });

        Button buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
}