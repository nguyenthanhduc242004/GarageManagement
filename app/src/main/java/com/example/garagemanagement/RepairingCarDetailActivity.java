package com.example.garagemanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RepairingCarDetailActivity extends AppCompatActivity implements RecyclerViewInterface, CustomCarSupplyDialog.CustomCarSupplyDialogInterface {
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    NumberFormat currencyFormatter = new DecimalFormat("#,###");
    CarServiceAdapter carServiceAdapter;
    TextView totalCarServicePrice;
    long totalCarServicePriceLong = 0;
    CarSupplyAdapter carSupplyAdapter;
    TextView totalCarSupplyPrice;
    long totalCarSupplyPriceLong = 0;
    TextView tvTotalPrice;

    public void openCarSupplyDialog(View view) {
        CustomCarSupplyDialog customCarSupplyDialog = new CustomCarSupplyDialog();
        customCarSupplyDialog.show(getSupportFragmentManager(), "Chọn Vật Tư, Dung Dịch");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_repairing_car_detail);
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
        Intent intent = getIntent();
        String licensePlate = intent.getStringExtra("LICENSE_PLATE");
        String carBrandId = intent.getStringExtra("CAR_BRAND_ID");
        String carBrandText = intent.getStringExtra("CAR_BRAND_TEXT");
        String carTypeId = intent.getStringExtra("CAR_TYPE_ID");
        String carTypeText = intent.getStringExtra("CAR_TYPE_TEXT");
        String ownerName = intent.getStringExtra("OWNER_NAME");
        String phoneNumber = intent.getStringExtra("PHONE_NUMBER");
        String receiveDate = intent.getStringExtra("RECEIVE_DATE");
        int carImage = intent.getIntExtra("CAR_IMAGE", 0);
        int state = intent.getIntExtra("STATE", -1);
        List<CarService> selectedCarServices = (List<CarService>) intent.getSerializableExtra("CAR_SERVICES");
        List<CarSupply> selectedCarSupplies = (List<CarSupply>) intent.getSerializableExtra("CAR_SUPPLIES");

        TextView tvLicensePlate = findViewById(R.id.tvLicensePlate);
        TextView tvCarBrand = findViewById(R.id.tvCarBrand);
        TextView tvCarType = findViewById(R.id.tvCarType);
        TextView tvOwnerName = findViewById(R.id.tvOwnerName);
        TextView tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        TextView tvReceiveDate = findViewById(R.id.tvReceiveDate);
        ImageView ivCarImage = findViewById(R.id.ivCarImage);

        tvLicensePlate.setText(licensePlate);
        tvCarBrand.setText(carBrandText);
        tvCarType.setText(carTypeText);
        tvOwnerName.setText(ownerName);
        tvPhoneNumber.setText(phoneNumber);
        tvReceiveDate.setText(receiveDate);
        if (carImage == 0) {
            ivCarImage.setImageResource(R.drawable.no_image);
        } else {
            ivCarImage.setImageResource(carImage);
        }

//        EDIT BUTTON:
        Button buttonEdit = findViewById(R.id.buttonUpdateCar);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RepairingCarDetailActivity.this, UpdateRepairingCarActivity.class);
                intent.putExtra("LICENSE_PLATE", licensePlate);
                intent.putExtra("CAR_BRAND_ID", carBrandId);
                intent.putExtra("CAR_BRAND_TEXT", carBrandText);
                intent.putExtra("CAR_TYPE_ID", carTypeId);
                intent.putExtra("CAR_TYPE_TEXT", carTypeText);
                intent.putExtra("OWNER_NAME", ownerName);
                intent.putExtra("PHONE_NUMBER", phoneNumber);
                intent.putExtra("RECEIVE_DATE", receiveDate);
                intent.putExtra("CAR_IMAGE", carImage);
                intent.putExtra("CAR_SERVICES", (Serializable) selectedCarServices);
                intent.putExtra("CAR_SUPPLIES", (Serializable) selectedCarSupplies);
                startActivity(intent);
            }
        });

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
        carServiceAdapter = new CarServiceAdapter(getApplicationContext(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerViewCarServiceList = findViewById(R.id.recyclerViewCarServiceList);
        recyclerViewCarServiceList.setLayoutManager(linearLayoutManager);
        recyclerViewCarServiceList.setFocusable(false);
        carServiceAdapter.setData(selectedCarServices);
        recyclerViewCarServiceList.setAdapter(carServiceAdapter);

//      SET totalCarServicePrice
        totalCarServicePrice = findViewById(R.id.totalCarServicePrice);
        for (int i = 0; i < selectedCarServices.size(); i++) {
            totalCarServicePriceLong += selectedCarServices.get(i).getPrice();
        }
        totalCarServicePrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalCarServicePriceLong)));

//        CALL API CAR SERVICES:
        String carServicesJson = "[\n" +
                "    {\n" +
                "        \"serviceId\": 1,\n" +
                "        \"serviceName\": \"BẢO DƯỠNG CẤP NHỎ (5000) KM\",\n" +
                "        \"description\": \"Cần thực hiện khi xe chạy được 5.000km, 25, 35, 45...\",\n" +
                "        \"prices\": {\n" +
                "            \"1\": 149000,\n" +
                "            \"2\": 149000,\n" +
                "            \"3\": 199000,\n" +
                "            \"4\": 199000\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"serviceId\": 2,\n" +
                "        \"serviceName\": \"BẢO DƯỠNG CẤP TRUNG BÌNH (10.000) KM\",\n" +
                "        \"description\": \"Cần thực hiện khi xe chạy được 10.000km, 30, 50, 70, 90...\",\n" +
                "        \"prices\": {\n" +
                "            \"1\": 299000,\n" +
                "            \"2\": 299000,\n" +
                "            \"3\": 399000,\n" +
                "            \"4\": 399000\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"serviceId\": 3,\n" +
                "        \"serviceName\": \"BẢO DƯỠNG CẤP TRUNG BÌNH LỚN (20.000) KM\",\n" +
                "        \"description\": \"Cần thực hiện khi xe chạy được 20.000km, 60, 100, 140, 180...\",\n" +
                "        \"prices\": {\n" +
                "            \"1\": 399000,\n" +
                "            \"2\": 499000,\n" +
                "            \"3\": 599000,\n" +
                "            \"4\": 699000\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"serviceId\": 4,\n" +
                "        \"serviceName\": \"BẢO DƯỠNG CẤP LỚN (40.000) KM\",\n" +
                "        \"description\": \"Cần thực hiện khi xe chạy được 40.000km, 80, 120, 160, 200...\",\n" +
                "        \"prices\": {\n" +
                "            \"1\": 799000,\n" +
                "            \"2\": 999000,\n" +
                "            \"3\": 1199000,\n" +
                "            \"4\": 1499000\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"serviceId\": 5,\n" +
                "        \"serviceName\": \"Bảo dưỡng phanh 4 bánh\",\n" +
                "        \"prices\": {\n" +
                "            \"1\": 500000,\n" +
                "            \"2\": 500000,\n" +
                "            \"3\": 500000,\n" +
                "            \"4\": 600000\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"serviceId\": 6,\n" +
                "        \"serviceName\": \"Vệ sinh kim phun (bao gồm dung dịch kèm theo)\",\n" +
                "        \"prices\": {\n" +
                "            \"1\": 660000,\n" +
                "            \"2\": 660000,\n" +
                "            \"3\": 660000,\n" +
                "            \"4\": 660000\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"serviceId\": 8,\n" +
                "        \"serviceName\": \"Kiểm tra hệ thống gầm\",\n" +
                "        \"prices\": {\n" +
                "            \"1\": 350000,\n" +
                "            \"2\": 350000,\n" +
                "            \"3\": 350000,\n" +
                "            \"4\": 350000\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"serviceId\": 9,\n" +
                "        \"serviceName\": \"Kiểm tra hệ thống điện chuyên sâu\",\n" +
                "        \"prices\": {\n" +
                "            \"1\": 1200000,\n" +
                "            \"2\": 1200000,\n" +
                "            \"3\": 1200000,\n" +
                "            \"4\": 1200000\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"serviceId\": 10,\n" +
                "        \"serviceName\": \"Kiểm tra tổng quát\",\n" +
                "        \"prices\": {\n" +
                "            \"1\": 600000,\n" +
                "            \"2\": 600000,\n" +
                "            \"3\": 600000,\n" +
                "            \"4\": 600000\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"serviceId\": 11,\n" +
                "        \"serviceName\": \"Cân bằng động (100k/bánh)\",\n" +
                "        \"prices\": {\n" +
                "            \"1\": 400000,\n" +
                "            \"2\": 400000,\n" +
                "            \"3\": 400000,\n" +
                "            \"4\": 400000\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"serviceId\": 12,\n" +
                "        \"serviceName\": \"Cân chỉnh độ chụm\",\n" +
                "        \"prices\": {\n" +
                "            \"1\": 500000,\n" +
                "            \"2\": 600000,\n" +
                "            \"3\": 700000,\n" +
                "            \"4\": 800000\n" +
                "        }\n" +
                "    }\n" +
                "]";
        Gson gson = new GsonBuilder().create();
        List<CarService> allCarServices = gson.fromJson(carServicesJson, new TypeToken<List<CarService>>() {}.getType());

//        ADD CAR SERVICE BUTTON:
        String[] carServiceNames = new String[allCarServices.size()];
        for (int i = 0; i < allCarServices.size(); i++) {
            carServiceNames[i] = allCarServices.get(i).getServiceName();
        }

        boolean[] checkedServices = new boolean[allCarServices.size()];
        addCarServiceButton = findViewById(R.id.addCarServiceButton);
        List<CarService> finalCarServices = allCarServices;
        addCarServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(RepairingCarDetailActivity.this);
                builder.setTitle("Chọn Dịch Vụ");
                builder.setMultiChoiceItems(carServiceNames, checkedServices, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                        // Update current focused item's checked status
                        checkedServices[which] = isChecked;
                        // Get the current focused item
                        // CarService currentItem = finalCarServices.get(which);
                        // Notify the current action
                        // Toast.makeText(AddRepairCardActivity.this, currentItem.getServiceName() + " " + isChecked, Toast.LENGTH_SHORT).show();
                    }
                });

                // Set positive/yes button click listener
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        List<CarService> selectedCarServices = new ArrayList<>();
                        long totalPrice = 0;
                        for (int i = 0; i < checkedServices.length; i++) {
                            boolean checked = checkedServices[i];
                            if (checked) {
                                selectedCarServices.add(finalCarServices.get(i));
                                totalPrice += finalCarServices.get(i).getPrice();
                            }
                        }
                        carServiceAdapter.setData(selectedCarServices);
                        totalCarServicePrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalPrice)));
                        totalCarServicePriceLong = totalPrice;
                        tvTotalPrice.setText(String.format("Tổng tiền: %sđ", currencyFormatter.format(totalCarSupplyPriceLong + totalCarServicePriceLong)));
                    }
                });


                // Set neutral/cancel button click listener
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do something here
                    }
                });

                AlertDialog alertDialog = builder.create();
                // Show alert dialog
                alertDialog.show();
            }
        });

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

//        ADD CAR SUPPLY BUTTON:
//        CALL API CAR SUPPLIES:
        String carSuppliesJson = "[\n" +
                "    {\n" +
                "        \"supplyId\": 1,\n" +
                "        \"supplyName\": \"Dung dịch phụ gia súc béc xăng (Wurth)\",\n" +
                "        \"price\": 300000\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplyId\": 2,\n" +
                "        \"supplyName\": \"Dung dịch hụ gia súc nhớt (Wurth)\",\n" +
                "        \"price\": 300000\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplyId\": 3,\n" +
                "        \"supplyName\": \"Dung dịch vệ sinh kim phun (Wurth)\",\n" +
                "        \"price\": 350000\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplyId\": 4,\n" +
                "        \"supplyName\": \"Nước làm mát (Asin, Jinco)\",\n" +
                "        \"price\": 150000\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplyId\": 5,\n" +
                "        \"supplyName\": \"Còi Denso\",\n" +
                "        \"price\": 500000\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplyId\": 6,\n" +
                "        \"supplyName\": \"Gạt mưa Bosch cứng\",\n" +
                "        \"price\": 350000\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplyId\": 7,\n" +
                "        \"supplyName\": \"Gạt mưa Bosch mềm\",\n" +
                "        \"price\": 600000\n" +
                "    }\n" +
                "]";
        Gson gson1 = new GsonBuilder().create();
        List<CarSupply> allCarSupplies = gson1.fromJson(carServicesJson, new TypeToken<List<CarSupply>>() {}.getType());

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


        totalCarSupplyPrice = findViewById(R.id.totalCarSupplyPrice);
        totalCarSupplyPrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalCarSupplyPriceLong)));
        addCarSupplyButton = findViewById(R.id.addCarSupplyButton);
        addCarSupplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCarSupplyDialog(view);
            }
        });

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalPrice.setText(String.format("Tổng tiền: %sđ", currencyFormatter.format(totalCarSupplyPriceLong + totalCarServicePriceLong)));
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(int position, int state) {

    }

    @Override
    public void setCarSupplyTotalPrice(long totalPrice) {
        totalCarSupplyPrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalPrice)));
        totalCarSupplyPriceLong = totalPrice;
        tvTotalPrice.setText(String.format("Tổng tiền: %sđ", currencyFormatter.format(totalCarSupplyPriceLong + totalCarServicePriceLong)));
    }

    @Override
    public void setCarSupplyAdapterData(List<CarSupply> carSupplies) {
        carSupplyAdapter.setData(carSupplies);
    }
}