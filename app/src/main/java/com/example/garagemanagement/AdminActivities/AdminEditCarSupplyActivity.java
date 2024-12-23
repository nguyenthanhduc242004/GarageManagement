package com.example.garagemanagement.AdminActivities;

import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.garagemanagement.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminEditCarSupplyActivity extends AppCompatActivity {
    FirebaseFirestore db;

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

        //BACK BUTTON:
        ImageButton imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(view -> {
            finish();
        });

        String carSupplyId = getIntent().getStringExtra("CAR_SUPPLY_ID");
        String carSupplyText = getIntent().getStringExtra("CAR_SUPPLY_TEXT");
        long price = getIntent().getLongExtra("PRICE", 0);

        TextView tvCarSupply = findViewById(R.id.tvOldCarSupply);
        tvCarSupply.setText(carSupplyText);
        EditText etCarSupply = findViewById(R.id.etCarSupply);
        etCarSupply.setText(carSupplyText);

        TextView tvPrice = findViewById(R.id.tvOldPrice);
        tvPrice.setText(String.format("%sđ", formatter.format(price)));
        EditText etPrice = findViewById(R.id.etPrice);
        etPrice.setText(String.format(String.valueOf(price)));

        db = FirebaseFirestore.getInstance();
        MaterialButton buttonEdit = findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCarSupply.getText().toString().isEmpty()) {
                    etCarSupply.setError("Vui lòng đầy đủ thông tin!");
                } else if (etPrice.getText().toString().isEmpty()) {
                    etPrice.setError("Vui lòng nhập đầy đủ thông tin!");
                } else {
                    Map<String, Object> carSupplyData = new HashMap<>();
                    carSupplyData.put("supplyName", etCarSupply.getText().toString());
                    try {
                        carSupplyData.put("price", Long.parseLong(etPrice.getText().toString()));
                    } catch (Exception e) {
                        etPrice.setError("Giá không hợp lệ!");
                        return;
                    }

                    carSupplyData.put("usable", true);
                    db.collection("CarSupply")
                            .document(carSupplyId)
                            .update(carSupplyData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AdminEditCarSupplyActivity.this, "Sửa vật tư, dung dịch thành công!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminEditCarSupplyActivity.this, "Sửa vật tư, dung dịch không thành công!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}