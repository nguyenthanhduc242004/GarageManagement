package com.example.garagemanagement.AdminActivities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.CarBrand;
import com.example.garagemanagement.Objects.CarService;
import com.example.garagemanagement.R;
import com.example.garagemanagement.adapter.CarServiceAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAddCarTypeActivity extends AppCompatActivity implements RecyclerViewInterface {
    FirebaseFirestore db;
    CarServiceAdapter carServiceAdapter;
    List<CarService> carServices = new ArrayList<>();
    ProgressDialog progressDialog;
    MaterialButton buttonAdd;
    RecyclerView recyclerViewCarTypePrice;
    Map<String, Long> serviceIdAndPrice = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_car_type);
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang tải...");
        progressDialog.show();

        carServiceAdapter = new CarServiceAdapter(this,CarServiceAdapter.TYPE_CAR_TYPE_PRICE, null, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewCarTypePrice = findViewById(R.id.recyclerViewCarTypePrice);
        recyclerViewCarTypePrice.setLayoutManager(linearLayoutManager);
        recyclerViewCarTypePrice.setFocusable(false);
        recyclerViewCarTypePrice.setAdapter(carServiceAdapter);

        db = FirebaseFirestore.getInstance();
        EventChangeListener();
        carServiceAdapter.setData(carServices);


        EditText etCarType = findViewById(R.id.etCarType);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSubmittable = true;

                if (etCarType.getText().toString().isEmpty()) {
                    etCarType.setError("Vui lòng nhập Loại Xe!");
                    isSubmittable = false;
                }

                for (int i = 0; i < carServices.size(); i++) {
                    View itemView = recyclerViewCarTypePrice.getChildAt(i);
                    if (itemView != null) {
                        EditText etPrice = itemView.findViewById(R.id.etPrice);
                        String priceString = etPrice.getText().toString();
                        if (priceString.isEmpty()) {
                            etPrice.setError("Vui lòng nhập trường này!");
                            isSubmittable = false;
                            continue;
                        }
                        Long priceLong = Long.parseLong(priceString);
                        serviceIdAndPrice.put(carServices.get(i).getServiceId(), priceLong);
                    }
                }

                if (!isSubmittable) {
                    return;
                }

                db = FirebaseFirestore.getInstance();
                Map<String, Object> carTypeData = new HashMap<>();
                carTypeData.put("carTypeText", etCarType.getText().toString());
                carTypeData.put("usable", true);

//                 Add a new document with a generated ID
                db.collection("CarType")
                        .add(carTypeData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
//                                Toast.makeText(AdminAddCarTypeActivity.this, "Thêm loại xe thành công!", Toast.LENGTH_SHORT).show();
                                for (String serviceId : serviceIdAndPrice.keySet()) {
                                    for (int i = 0; i < carServices.size(); i++) {
                                        CarService currentCarService = carServices.get(i);
                                        if (serviceId.equals(currentCarService.getServiceId())) {
                                            Map<String, Long> newPrices = currentCarService.getPrices();
                                            newPrices.put(documentReference.getId(), serviceIdAndPrice.get(serviceId));
                                            carServices.get(i).setPrices(newPrices);
                                        }
                                    }
                                }

                                for (int i = 0; i < carServices.size(); i++) {
                                    CarService currentCarService = carServices.get(i);
                                    Map<String, Object> carServiceData = new HashMap<>();
                                    carServiceData.put("serviceName", currentCarService.getServiceName());
                                    carServiceData.put("prices", currentCarService.getPrices());
                                    carServiceData.put("usable", true);
                                    db.collection("CarService")
                                            .document(currentCarService.getServiceId())
                                            .update(carServiceData)
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AdminAddCarTypeActivity.this, "Thêm loại xe không thành công!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminAddCarTypeActivity.this, "Thêm loại xe không thành công!", Toast.LENGTH_SHORT).show();
                            }
                        });
                finish();
            }
        });
    }

    private void EventChangeListener() {
        db.collection("CarService")
                .orderBy("serviceName")
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

    }

    @Override
    public void onItemClick(int position, int state) {

    }
}