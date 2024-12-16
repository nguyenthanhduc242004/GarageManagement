package com.example.garagemanagement;

import android.content.DialogInterface;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RepairingCarDetailActivity extends AppCompatActivity implements RecyclerViewInterface, CustomCarSupplyDialog.CustomCarSupplyDialogInterface {
    Car currentCar;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    NumberFormat currencyFormatter = new DecimalFormat("#,###");
    CarServiceAdapter carServiceAdapter;
    TextView totalCarServicePrice;
    long totalCarServicePriceLong = 0;
    CarSupplyAdapter carSupplyAdapter;
    TextView totalCarSupplyPrice;
    long totalCarSupplyPriceLong = 0;
    TextView tvTotalPrice;

    public static List<CarSupply> selectedCarSupplies = new ArrayList<>();

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

//        BACK BUTTON:
        ImageButton imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        BASIC CAR INFORMATION:
        String licensePlate = getIntent().getStringExtra("LICENSE_PLATE");
        String carBrand = getIntent().getStringExtra("CAR_BRAND");
        String ownerName = getIntent().getStringExtra("OWNER_NAME");
        String phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        String carType = getIntent().getStringExtra("CAR_TYPE");
        String receiveDate = getIntent().getStringExtra("RECEIVE_DATE");
        int carImage = getIntent().getIntExtra("CAR_IMAGE", 0);
        String[] carServiceArray = getIntent().getStringArrayExtra("CAR_SERVICES");
        String[] carSupplyArray = getIntent().getStringArrayExtra("CAR_SUPPLIES");

//        TODO: USE SERIALIZABLE INSTEAD OF STRING[] => WORK WITH BACKEND
//        GET SELECTED CAR SUPPLY DATA:
        // FAKE CALL API SUPPLY LIST:
//        CarSupply carSupply1 = new CarSupply("1", "Dung dịch phụ gia súc béc xăng (Wurth)", 300000);
//        CarSupply carSupply2 = new CarSupply("2", "Dung dịch phụ gia súc nhớt (Wurth)", 300000);
//        CarSupply carSupply3 = new CarSupply("3", "Dung dịch vệ sinh kim phun (Wurth)", 350000);
//        CarSupply carSupply4 = new CarSupply("4", "Nước làm mát (Asin, Jinco)", 150000);
//        CarSupply carSupply5 = new CarSupply("5", "Còi Denso", 500000);
//        CarSupply carSupply6 = new CarSupply("6", "Gạt mưa Bosch cứng", 350000);
//        CarSupply carSupply7 = new CarSupply("7", "Gạt mưa Bosch mềm", 600000);
//        List<CarSupply> carSupplies = List.of(carSupply1, carSupply2, carSupply3, carSupply4, carSupply5, carSupply6, carSupply7);
//        for (int i = 0; i < carSupplies.size(); i++) {
//            if (carSupplies.get(i).getSupplyId().equals())
//        }

        TextView tvOwnerName = findViewById(R.id.tvOwnerName);
        TextView tvLicensePlate = findViewById(R.id.tvLicensePlate);
        TextView tvCarBrand = findViewById(R.id.tvCarBrand);
        TextView tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        TextView tvCarType = findViewById(R.id.tvCarType);
        TextView tvReceiveDate = findViewById(R.id.tvReceiveDate);
        ImageView ivCarImage = findViewById(R.id.ivCarImage);

        ivCarImage.setImageResource(carImage);
        tvOwnerName.setText(ownerName);
        tvLicensePlate.setText(licensePlate);
        tvCarBrand.setText(carBrand);
        tvPhoneNumber.setText(phoneNumber);
        tvCarType.setText(carType);
        tvReceiveDate.setText(receiveDate);
        if (carImage == 0) {
            ivCarImage.setImageResource(R.drawable.no_image);
        } else {
            ivCarImage.setImageResource(carImage);
        }

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

//        CarServiceAdapter
        carServiceAdapter = new CarServiceAdapter(getApplicationContext(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerViewCarServiceList = findViewById(R.id.recyclerViewCarServiceList);
        recyclerViewCarServiceList.setLayoutManager(linearLayoutManager);
        recyclerViewCarServiceList.setFocusable(false);
        carServiceAdapter.setData(new ArrayList<>());
        recyclerViewCarServiceList.setAdapter(carServiceAdapter);

//        ADD CAR SERVICE BUTTON:
        // FAKE CALL API SERVICE LIST:
        CarService carService1 = new CarService("1", "Bảo dưỡng cấp nhỏ (5000) KM", 149000);
        CarService carService2 = new CarService("2", "Bảo dưỡng cấp trung bình (10.000) KM", 299000);
        CarService carService3 = new CarService("3", "Bảo dưỡng cấp trung bình lớn (20.000) KM", 399000);
        CarService carService4 = new CarService("4", "Bảo dưỡng cấp lớn (40.000) KM", 799000);
        CarService carService5 = new CarService("5", "Bảo dưỡng phanh 4 bánh", 500000);
        CarService carService6 = new CarService("6", "Vệ sinh kim phun (bao gồm dung dịch kèm theo)", 660000);
        CarService carService7 = new CarService("7", "Kiểm tra hệ thống giảm", 350000);
        CarService carService8 = new CarService("8", "Kiểm tra hệ thống điện chuyên sâu", 1200000);
        CarService carService9 = new CarService("9", "Kiểm tra tổng quát", 600000);
        CarService carService10 = new CarService("10", "Cân bằng động (100k/ bánh)", 400000);
        CarService carService11 = new CarService("11", "Cân chỉnh độ chụm", 500000);
        List<CarService> carServices = List.of(carService1, carService2, carService3, carService4, carService5, carService6, carService7, carService8, carService9, carService10, carService11);

        String[] carServiceNames = new String[carServices.size()];
        for (int i = 0; i < carServices.size(); i++) {
            carServiceNames[i] = carServices.get(i).getServiceName();
        }

        totalCarServicePrice = findViewById(R.id.totalCarServicePrice);
        boolean[] checkedServices = new boolean[carServices.size()];
        Button addCarServiceButton = findViewById(R.id.addCarServiceButton);
        List<CarService> finalCarServices = carServices;
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

//        CarServiceAdapter
        carSupplyAdapter = new CarSupplyAdapter(getApplicationContext(), CarSupplyAdapter.TYPE_LIST, this);
        RecyclerView recyclerViewCarSupplyList = findViewById(R.id.recyclerViewCarSupplyList);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        recyclerViewCarSupplyList.setLayoutManager(linearLayoutManager1);
        recyclerViewCarSupplyList.setFocusable(false);
        carSupplyAdapter.setData(selectedCarSupplies);
        recyclerViewCarSupplyList.setAdapter(carSupplyAdapter);


        totalCarSupplyPrice = findViewById(R.id.totalCarSupplyPrice);
        Button addCarSupplyButton = findViewById(R.id.addCarSupplyButton);
        addCarSupplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCarSupplyDialog(view);
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(int position, int state) {

    }

    @Override
    public void setCarSupplyListData(List<CarSupply> carSupplies) {
        carSupplyAdapter.setData(carSupplies);
        selectedCarSupplies = carSupplies;
    }

    @Override
    public void setCarSupplyTotalPrice(long totalPrice) {
        totalCarSupplyPrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalPrice)));
        totalCarSupplyPriceLong = totalPrice;
        tvTotalPrice.setText(String.format("Tổng tiền: %sđ", currencyFormatter.format(totalCarSupplyPriceLong + totalCarServicePriceLong)));
    }
}