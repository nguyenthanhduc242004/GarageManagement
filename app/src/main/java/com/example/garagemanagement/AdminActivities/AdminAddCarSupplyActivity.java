package com.example.garagemanagement.AdminActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminAddCarSupplyActivity extends AppCompatActivity {
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_car_supply);
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

        EditText etCarSupply = findViewById(R.id.etCarSupply);
        EditText etPrice = findViewById(R.id.etPrice);
        MaterialButton buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCarSupply.getText().toString().isEmpty()) {
                    etCarSupply.setError("Vui lòng nhập đầy đủ thông tin!");
                } else if (etPrice.getText().toString().isEmpty()) {
                    etPrice.setError("Vui lòng nhập đầy đủ thông tin!");
                } else {
                    db = FirebaseFirestore.getInstance();
                    Map<String, Object> carSupplyData = new HashMap<>();
                    carSupplyData.put("supplyName", etCarSupply.getText().toString());
                    try {
                        carSupplyData.put("price", Long.parseLong(etPrice.getText().toString()));
                    } catch (Exception e) {
                        etPrice.setError("Giá không hợp lệ!");
                        return;
                    }
                    carSupplyData.put("usable", true);

                    // Add a new document with a generated ID
                    db.collection("CarSupply")
                            .add(carSupplyData)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(AdminAddCarSupplyActivity.this, "Thêm vật tư, dung dịch thành công!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminAddCarSupplyActivity.this, "Thêm vật tư, dung dịch không thành công!", Toast.LENGTH_SHORT).show();
                                }
                            });
                    finish();
                }
            }
        });
    }
}