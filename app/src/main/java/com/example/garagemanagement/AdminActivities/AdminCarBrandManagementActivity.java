package com.example.garagemanagement.AdminActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.Objects.CarBrand;
import com.example.garagemanagement.R;
import com.example.garagemanagement.adapter.CarBrandAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminCarBrandManagementActivity extends AppCompatActivity implements RecyclerViewInterface {
    CarBrandAdapter carBrandAdapter;
    FirebaseFirestore db;
    List<CarBrand> carBrands = new ArrayList<>();
    ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang tải...");
        progressDialog.show();

        carBrandAdapter = new CarBrandAdapter(this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerViewCarBrandManagement = findViewById(R.id.recyclerViewCarBrandManagement);
        recyclerViewCarBrandManagement.setLayoutManager(linearLayoutManager);
        recyclerViewCarBrandManagement.setFocusable(false);
        recyclerViewCarBrandManagement.setAdapter(carBrandAdapter);

        db = FirebaseFirestore.getInstance();
        EventChangeListener();
        carBrandAdapter.setData(carBrands);

        MaterialButton buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCarBrandManagementActivity.this, AdminAddCarBrandActivity.class);
            startActivity(intent);
        });


    }

    private void EventChangeListener() {
        db.collection("CarBrand")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.e("Firestore error", error.getMessage());
                        return;
                    }
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        QueryDocumentSnapshot document = dc.getDocument();
                        DocumentChange.Type type = dc.getType();
                        Boolean usable = document.getBoolean("usable");
                        CarBrand carBrand = document.toObject(CarBrand.class);
                        carBrand.setCarBrandId(document.getId());
                        if (type == DocumentChange.Type.ADDED) {
                            if (usable) {
                                carBrands.add(carBrand);
                            }
                        } else if (type == DocumentChange.Type.MODIFIED) {
                            boolean isFound = false;
                            for (int i = 0; i < carBrands.size(); i++) {
                                if (carBrands.get(i).getCarBrandId().equals(carBrand.getCarBrandId())) {
                                    isFound = true;
                                    if (usable) {
                                        carBrands.set(i, carBrand);
                                    } else {
                                        carBrands.remove(i);
                                    }
                                    break;
                                }
                            }
                            if (!isFound && usable) {
                                carBrands.add(carBrand);
                            }
                        }
                        carBrandAdapter.setData(carBrands);
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                }
            });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(int position, int state) {

    }
}