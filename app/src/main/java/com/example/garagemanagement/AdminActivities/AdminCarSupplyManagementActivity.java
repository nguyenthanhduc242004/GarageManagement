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
import com.example.garagemanagement.Objects.CarBrand;
import com.example.garagemanagement.Objects.CarSupply;
import com.example.garagemanagement.Objects.CarType;
import com.example.garagemanagement.R;
import com.example.garagemanagement.adapter.CarBrandAdapter;
import com.example.garagemanagement.adapter.CarSupplyAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminCarSupplyManagementActivity extends AppCompatActivity implements RecyclerViewInterface {
    CarSupplyAdapter carSupplyAdapter;
    FirebaseFirestore db;
    List<CarSupply> carSupplies = new ArrayList<>();
    ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang tải...");
        progressDialog.show();

        carSupplyAdapter = new CarSupplyAdapter(this, CarSupplyAdapter.TYPE_MANAGEMENT, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerViewCarSupplyManagement = findViewById(R.id.recyclerViewCarSupplyManagement);
        recyclerViewCarSupplyManagement.setLayoutManager(linearLayoutManager);
        recyclerViewCarSupplyManagement.setFocusable(false);
        recyclerViewCarSupplyManagement.setAdapter(carSupplyAdapter);

        db = FirebaseFirestore.getInstance();
        EventChangeListener();
        carSupplyAdapter.setData(carSupplies);

        MaterialButton buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCarSupplyManagementActivity.this, AdminAddCarSupplyActivity.class);
            startActivity(intent);
        });
    }

    private void EventChangeListener() {
        db.collection("CarSupply")
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
                        CarSupply carSupply = document.toObject(CarSupply.class);
                        carSupply.setSupplyId(document.getId());
                        if (type == DocumentChange.Type.ADDED) {
                            if (usable) {
                                carSupplies.add(carSupply);
                            }
                        } else if (type == DocumentChange.Type.MODIFIED) {
                            boolean isFound = false;
                            for (int i = 0; i < carSupplies.size(); i++) {
                                if (carSupplies.get(i).getSupplyId().equals(carSupply.getSupplyId())) {
                                    isFound = true;
                                    if (usable) {
                                        carSupplies.set(i, carSupply);
                                    } else {
                                        carSupplies.remove(i);
                                    }
                                    break;
                                }
                            }
                            if (!isFound && usable) {
                                carSupplies.add(carSupply);
                            }
                        }
                        carSupplyAdapter.setData(carSupplies);
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