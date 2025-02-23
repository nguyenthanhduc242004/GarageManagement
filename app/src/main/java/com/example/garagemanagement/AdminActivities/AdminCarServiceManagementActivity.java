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
import com.example.garagemanagement.Objects.CarService;
import com.example.garagemanagement.R;
import com.example.garagemanagement.adapter.CarServiceAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminCarServiceManagementActivity extends AppCompatActivity implements RecyclerViewInterface {
    CarServiceAdapter carServiceAdapter;
    FirebaseFirestore db;
    List<CarService> carServices = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_car_service_management);
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

        carServiceAdapter = new CarServiceAdapter(this, CarServiceAdapter.TYPE_MANAGEMENT, null, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerViewCarServiceManagement = findViewById(R.id.recyclerViewCarServiceManagement);
        recyclerViewCarServiceManagement.setLayoutManager(linearLayoutManager);
        recyclerViewCarServiceManagement.setFocusable(false);
        recyclerViewCarServiceManagement.setAdapter(carServiceAdapter);

        db = FirebaseFirestore.getInstance();
        EventChangeListener();
        carServiceAdapter.setData(carServices);

        MaterialButton buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCarServiceManagementActivity.this, AdminAddCarServiceActivity.class);
            startActivity(intent);
        });
    }

    private void EventChangeListener() {
        db.collection("CarService")
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
                            CarService carService = document.toObject(CarService.class);
                            carService.setServiceId(document.getId());
                            if (type == DocumentChange.Type.ADDED) {
                                if (usable) {
                                    carServices.add(carService);
                                }
                            } else if (type == DocumentChange.Type.MODIFIED) {
                                boolean isFound = false;
                                for (int i = 0; i < carServices.size(); i++) {
                                    if (carServices.get(i).getServiceId().equals(carService.getServiceId())) {
                                        isFound = true;
                                        if (usable) {
                                            carServices.set(i, carService);
                                        } else {
                                            carServices.remove(i);
                                        }
                                        break;
                                    }
                                }
                                if (!isFound && usable) {
                                    carServices.add(carService);
                                }
                            }
                            carServiceAdapter.setData(carServices);
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(AdminCarServiceManagementActivity.this, AdminCarServiceDetailActivity.class);
        intent.putExtra("CAR_SERVICE", carServices.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position, int state) {

    }
}