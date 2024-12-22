package com.example.garagemanagement.AdminActivities;

import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.garagemanagement.R;

public class AdminEditCarSupplyActivity extends AppCompatActivity {

    NumberFormat formatter = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_car_supply);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(view -> {
            finish();
        });

        String supplyId = getIntent().getStringExtra("SUPPLY_ID");
        String supplyName = getIntent().getStringExtra("SUPPLY_NAME");
        long price = getIntent().getLongExtra("PRICE", 0);

        TextView tvSupplyName = findViewById(R.id.tvOldCarSupply);
        tvSupplyName.setText(supplyName);
        EditText etSupplyName = findViewById(R.id.etSupplyName);
        etSupplyName.setText(supplyName);

        TextView tvPrice = findViewById(R.id.tvOldPrice);
        tvPrice.setText(String.format("%sđ", formatter.format(price)));
        EditText etPrice = findViewById(R.id.etPrice);
        etPrice.setText(String.format(String.valueOf(price)));
    }
}