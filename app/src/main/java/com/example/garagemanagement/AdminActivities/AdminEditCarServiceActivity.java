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
import com.example.garagemanagement.Objects.CarService;
import com.example.garagemanagement.Objects.CarType;
import com.example.garagemanagement.R;
import com.example.garagemanagement.adapter.CarTypeAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminEditCarServiceActivity extends AppCompatActivity implements RecyclerViewInterface {
    FirebaseFirestore db;
    CarTypeAdapter carTypeAdapter;
    RecyclerView recyclerViewCarServicePrice;
    List<CarType> carTypes = new ArrayList<>();
    ProgressDialog progressDialog;
    MaterialButton buttonEdit;
    Map<String, Long> newPrices = new HashMap<>();
    CarService passedCarService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_car_service);
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

        passedCarService = (CarService) getIntent().getSerializableExtra("CAR_SERVICE");
        String passedServiceName = passedCarService.getServiceName();

        carTypeAdapter = new CarTypeAdapter(this, CarTypeAdapter.TYPE_CAR_SERVICE_PRICE, passedCarService, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewCarServicePrice = findViewById(R.id.recyclerViewCarServicePrice);
        recyclerViewCarServicePrice.setLayoutManager(linearLayoutManager);
        recyclerViewCarServicePrice.setFocusable(false);
        recyclerViewCarServicePrice.setAdapter(carTypeAdapter);

        db = FirebaseFirestore.getInstance();
        EventChangeListener();
        carTypeAdapter.setData(carTypes);



        TextView tvOldServiceName = findViewById(R.id.tvOldServiceName);
        tvOldServiceName.setText(passedServiceName);
        EditText etServiceName = findViewById(R.id.etServiceName);
        etServiceName.setText(passedServiceName);

        buttonEdit = findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSubmittable = true;

                if (etServiceName.getText().toString().isEmpty()) {
                    etServiceName.setError("Vui lòng nhập Loại Xe!");
                    isSubmittable = false;
                }

                newPrices = new HashMap<>();

                for (int i = 0; i < carTypes.size(); i++) {
                    View itemView = recyclerViewCarServicePrice.getChildAt(i);
                    if (itemView != null) {
                        EditText etPrice = itemView.findViewById(R.id.etPrice);
                        String priceString = etPrice.getText().toString();
                        if (priceString.isEmpty()) {
                            etPrice.setError("Vui lòng nhập trường này!");
                            isSubmittable = false;
                            continue;
                        }
                        Long priceLong = Long.parseLong(priceString);
                        newPrices.put(carTypes.get(i).getCarTypeId(), priceLong);
                    }
                }

                if (!isSubmittable) {
                    return;
                }

                db = FirebaseFirestore.getInstance();
                Map<String, Object> carServiceData = new HashMap<>();
                carServiceData.put("serviceName", etServiceName.getText().toString());
                carServiceData.put("prices", newPrices);
                carServiceData.put("usable", true);

                db.collection("CarService")
                        .document(passedCarService.getServiceId())
                        .update(carServiceData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AdminEditCarServiceActivity.this, "Sửa dịch vụ thành công!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminEditCarServiceActivity.this, "Sửa dịch vụ không thành công!", Toast.LENGTH_SHORT).show();
                            }
                        });
                finish();
            }
        });
    }

    private void EventChangeListener() {
        db.collection("CarType")
                .orderBy("carTypeText")
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
                            CarType carType = document.toObject(CarType.class);
                            carType.setCarTypeId(document.getId());
                            if (type == DocumentChange.Type.ADDED) {
                                if (usable) {
                                    carTypes.add(carType);
                                }
                            } else if (type == DocumentChange.Type.MODIFIED) {
                                boolean isFound = false;
                                for (int i = 0; i < carTypes.size(); i++) {
                                    if (carTypes.get(i).getCarTypeId().equals(carType.getCarTypeId())) {
                                        isFound = true;
                                        if (usable) {
                                            carTypes.set(i, carType);
                                        } else {
                                            carTypes.remove(i);
                                        }
                                        break;
                                    }
                                }
                                if (!isFound && usable) {
                                    carTypes.add(carType);
                                }
                            }
                            carTypeAdapter.setData(carTypes);
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