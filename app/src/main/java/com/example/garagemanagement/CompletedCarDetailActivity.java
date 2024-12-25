package com.example.garagemanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.Objects.CarService;
import com.example.garagemanagement.Objects.CarSupply;
import com.example.garagemanagement.adapter.CarServiceAdapter;
import com.example.garagemanagement.adapter.CarSupplyAdapter;
import com.example.garagemanagement.adapter.ConfirmationDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompletedCarDetailActivity extends AppCompatActivity implements RecyclerViewInterface {
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    NumberFormat currencyFormatter = new DecimalFormat("#,###");

    CarServiceAdapter carServiceAdapter;
    TextView totalCarServicePrice;
    long totalCarServicePriceLong = 0;
    CarSupplyAdapter carSupplyAdapter;
    TextView totalCarSupplyPrice;
    long totalCarSupplyPriceLong = 0;
    TextView tvTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_completed_car_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
//        HIDING UNNECESSARY COMPONENTS:
        Button addCarServiceButton = findViewById(R.id.addCarServiceButton);
        Button addCarSupplyButton = findViewById(R.id.addCarSupplyButton);
        addCarServiceButton.setVisibility(View.GONE);
        addCarSupplyButton.setVisibility(View.GONE);

        //        BACK BUTTON:
        ImageButton imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        BASIC CAR INFORMATION:
        String carId = getIntent().getStringExtra("CAR_ID");
        String licensePlate = getIntent().getStringExtra("LICENSE_PLATE");
        String carBrandId = getIntent().getStringExtra("CAR_BRAND_ID");
        String carBrandText = getIntent().getStringExtra("CAR_BRAND_TEXT");
        String carTypeId = getIntent().getStringExtra("CAR_TYPE_ID");
        String carTypeText = getIntent().getStringExtra("CAR_TYPE_TEXT");
        String ownerName = getIntent().getStringExtra("OWNER_NAME");
        String phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        Date receiveDate = (Date) getIntent().getSerializableExtra("RECEIVE_DATE");
        int carImage = getIntent().getIntExtra("CAR_IMAGE", 0);
        int state = getIntent().getIntExtra("STATE", 0);
        List<CarService> selectedCarServices = (List<CarService>) getIntent().getSerializableExtra("CAR_SERVICES");
        List<CarSupply> selectedCarSupplies = (List<CarSupply>) getIntent().getSerializableExtra("CAR_SUPPLIES");
        Date paymentDate = (Date) getIntent().getSerializableExtra("PAYMENT_DATE");

        TextView tvLicensePlate = findViewById(R.id.tvLicensePlate);
        TextView tvCarBrand = findViewById(R.id.tvCarBrand);
        TextView tvCarType = findViewById(R.id.tvCarType);
        TextView tvOwnerName = findViewById(R.id.tvOwnerName);
        TextView tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        TextView tvReceiveDate = findViewById(R.id.tvReceiveDate);
        TextView tvPaymentDate = findViewById(R.id.tvPaymentDate);
        ImageView ivCarImage = findViewById(R.id.ivCarImage);

        tvLicensePlate.setText(licensePlate);
        tvCarBrand.setText(carBrandText);
        tvCarType.setText(carTypeText);
        tvOwnerName.setText(ownerName);
        tvPhoneNumber.setText(phoneNumber);
        tvReceiveDate.setText(dateFormatter.format(receiveDate));
        tvPaymentDate.setText(String.format("Ngày thanh toán: %s", dateFormatter.format(paymentDate)));
        if (carImage == 0) {
            ivCarImage.setImageResource(R.drawable.no_image);
        } else {
            ivCarImage.setImageResource(carImage);
        }

        TextView tvCarServicePriceHeader = findViewById(R.id.tvCarServicePriceHeader);
        tvCarServicePriceHeader.setText(String.format("Giá (%s)", carTypeText));

//        TOGGLE CAR INFORMATION BUTTON:
        LinearLayout carDetailWrapper = findViewById(R.id.car_detail_wrapper);
        Button toggleCarInformationButton = findViewById(R.id.toggleCarInformationButton);
        toggleCarInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carDetailWrapper.getVisibility() == View.GONE) {
                    carDetailWrapper.setVisibility(View.VISIBLE);
                    toggleCarInformationButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_keyboard_arrow_down_24, 0, 0, 0);
                } else if (carDetailWrapper.getVisibility() == View.VISIBLE) {
                    carDetailWrapper.setVisibility(View.GONE);
                    toggleCarInformationButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_keyboard_arrow_right_24, 0, 0, 0);
                }
            }

        });

//        TOGGLE CAR SERVICE LIST BUTTON:
        LinearLayout carServiceWrapper = findViewById(R.id.car_service_wrapper);
        Button toggleCarServicesButton = findViewById(R.id.toggleCarServicesButton);
        toggleCarServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carServiceWrapper.getVisibility() == View.GONE) {
                    carServiceWrapper.setVisibility(View.VISIBLE);
                    toggleCarServicesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_keyboard_arrow_down_24, 0, 0, 0);
                }
                else if (carServiceWrapper.getVisibility() == View.VISIBLE) {
                    carServiceWrapper.setVisibility(View.GONE);
                    toggleCarServicesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_keyboard_arrow_right_24, 0, 0, 0);
                }
            }
        });

        // CarServiceAdapter
        carServiceAdapter = new CarServiceAdapter(getApplicationContext(), CarServiceAdapter.TYPE_LIST, carTypeId, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerViewCarServiceList = findViewById(R.id.recyclerViewCarServiceList);
        recyclerViewCarServiceList.setLayoutManager(linearLayoutManager);
        recyclerViewCarServiceList.setFocusable(false);
        carServiceAdapter.setData(selectedCarServices);
        recyclerViewCarServiceList.setAdapter(carServiceAdapter);

//      SET totalCarServicePrice
        totalCarServicePrice = findViewById(R.id.totalCarServicePrice);
        for (int i = 0; i < selectedCarServices.size(); i++) {
            totalCarServicePriceLong += selectedCarServices.get(i).getPrices().get(carTypeId);
        }
        totalCarServicePrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalCarServicePriceLong)));

//        TOGGLE CAR SUPPLY LIST BUTTON:
        LinearLayout carSupplyWrapper = findViewById(R.id.car_supply_wrapper);
        Button toggleCarSuppliesButton = findViewById(R.id.toggleCarSuppliesButton);
        toggleCarSuppliesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carSupplyWrapper.getVisibility() == View.GONE) {
                    carSupplyWrapper.setVisibility(View.VISIBLE);
                    toggleCarSuppliesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_keyboard_arrow_down_24, 0, 0, 0);
                }
                else if (carSupplyWrapper.getVisibility() == View.VISIBLE) {
                    carSupplyWrapper.setVisibility(View.GONE);
                    toggleCarSuppliesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_keyboard_arrow_right_24, 0, 0, 0);
                }
            }
        });

//      SET totalCarSupplyPrice
        totalCarSupplyPrice = findViewById(R.id.totalCarSupplyPrice);
        for (int i = 0; i < selectedCarSupplies.size(); i++) {
            totalCarSupplyPriceLong += selectedCarSupplies.get(i).getPrice() * selectedCarSupplies.get(i).getQuantity();
        }
        totalCarSupplyPrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalCarSupplyPriceLong)));

        // CarServiceAdapter
        carSupplyAdapter = new CarSupplyAdapter(getApplicationContext(), CarSupplyAdapter.TYPE_LIST, this);
        RecyclerView recyclerViewCarSupplyList = findViewById(R.id.recyclerViewCarSupplyList);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        recyclerViewCarSupplyList.setLayoutManager(linearLayoutManager1);
        recyclerViewCarSupplyList.setFocusable(false);
        carSupplyAdapter.setData(selectedCarSupplies);
        recyclerViewCarSupplyList.setAdapter(carSupplyAdapter);

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalPrice.setText(String.format("Tổng tiền: %sđ", currencyFormatter.format(totalCarSupplyPriceLong + totalCarServicePriceLong)));

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(int position, int state) {

    }
}