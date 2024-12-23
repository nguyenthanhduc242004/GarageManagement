package com.example.garagemanagement.AdminActivities;

import android.app.ProgressDialog;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.example.garagemanagement.Objects.CarType;
import com.example.garagemanagement.R;
import com.example.garagemanagement.adapter.CarTypeAdapter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminCarServiceDetailActivity extends AppCompatActivity implements RecyclerViewInterface {
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    CarTypeAdapter carTypeAdapter;
    RecyclerView recyclerViewCarServicePrice;
    List<CarType> carTypes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_car_service_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CarService passedCarService = (CarService) getIntent().getSerializableExtra("CAR_SERVICE");

        //BACK BUTTON:
        ImageButton imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(view -> {
            finish();
        });

        TextView tvServiceName = findViewById(R.id.tvServiceName);
        tvServiceName.setText(passedCarService.getServiceName());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang tải...");
        progressDialog.show();

        carTypeAdapter = new CarTypeAdapter(this, CarTypeAdapter.TYPE_CAR_SERVICE_PRICE_UNEDITABLE,passedCarService , this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewCarServicePrice = findViewById(R.id.recyclerViewCarServicePrice);
        recyclerViewCarServicePrice.setLayoutManager(linearLayoutManager);
        recyclerViewCarServicePrice.setFocusable(false);
        recyclerViewCarServicePrice.setAdapter(carTypeAdapter);

        db = FirebaseFirestore.getInstance();
        EventChangeListener();
        carTypeAdapter.setData(carTypes);
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