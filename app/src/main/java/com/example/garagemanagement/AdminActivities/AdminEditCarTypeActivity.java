package com.example.garagemanagement.AdminActivities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.garagemanagement.R;

public class AdminEditCarTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_car_type);
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

        String carTypeId = getIntent().getStringExtra("CAR_TYPE_ID");
        String carTypeText = getIntent().getStringExtra("CAR_TYPE_TEXT");

        TextView tvCarType = findViewById(R.id.tvOldCarType);
        tvCarType.setText(carTypeText);
        EditText etCarType = findViewById(R.id.etCarType);
        etCarType.setText(carTypeText);

    }
}