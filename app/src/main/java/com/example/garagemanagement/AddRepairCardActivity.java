package com.example.garagemanagement;

import android.content.DialogInterface;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.garagemanagement.adapter.CarSpinnerAdapter;
import com.example.garagemanagement.adapter.CarSupplyAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddRepairCardActivity extends AppCompatActivity implements RecyclerViewInterface, CustomCarSupplyDialog.CustomCarSupplyDialogInterface {
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
    protected void onStop() {
        super.onStop();
        selectedCarSupplies = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_repair_card);
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

        //        BASIC INFORMATION:
        TextView tvOwnerName = findViewById(R.id.tvOwnerName);
        TextView tvLicensePlate = findViewById(R.id.tvLicensePlate);
        TextView tvCarBrand = findViewById(R.id.tvCarBrand);
        TextView tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        TextView tvCarType = findViewById(R.id.tvCarType);
        TextView tvReceiveDate = findViewById(R.id.tvReceiveDate);
        ImageView ivCarImage = findViewById(R.id.ivCarImage);

        //        CAR BRAND SPINNER:
        Spinner spinner = findViewById(R.id.spinner);

        String json = "[\n" +
                "  {\n" +
                "    \"carId\": 1,\n" +
                "    \"licensePlate\": \"29A-123.45\",\n" +
                "    \"ownerName\": \"Nguyễn Thị Linh\",\n" +
                "    \"carBrandId\": 1,\n" +
                "    \"carBrandText\": \"Honda\",\n" +
                "    \"carTypeId\": 1,\n" +
                "    \"carTypeText\": \"Mini\",\n" +
                "    \"phoneNumber\": \"0901234567\",\n" +
                "    \"receiveDate\": \"2024, 02, 14\",\n" +
                "    \"carImage\": 0,\n" +
                "    \"state\": 0,\n" +
                "    \"carServices\": [],\n" +
                "    \"carSupplies\": []\n" +
                "  },\n" +
                "  {\n" +
                "    \"carId\": 2,\n" +
                "    \"licensePlate\": \"51F-987.65\",\n" +
                "    \"ownerName\": \"Trần Minh Phúc\",\n" +
                "    \"carBrandId\": 2,\n" +
                "    \"carBrandText\": \"Aston Martin\",\n" +
                "    \"carTypeId\": 2,\n" +
                "    \"carTypeText\": \"Sedan\",\n" +
                "    \"phoneNumber\": \"0987654321\",\n" +
                "    \"receiveDate\": \"2024, 04, 10\",\n" +
                "    \"carImage\": 0,\n" +
                "    \"state\": 1,\n" +
                "    \"carServices\": [\n" +
                "      {\"serviceId\": \"3\", \"serviceName\": \"BẢO DƯỠNG CẤP TRUNG BÌNH LỚN (20.000) KM\", \"price\": 599000},\n" +
                "      {\"serviceId\": \"6\", \"serviceName\": \"Vệ sinh kim phun (bao gồm dung dịch kèm theo)\", \"price\": 660000}\n" +
                "    ],\n" +
                "    \"carSupplies\": [\n" +
                "      {\"supplyId\": \"7\", \"supplyName\": \"Gạt mưa Bosch mềm\", \"price\": 600000, \"quantity\": 7},\n" +
                "      {\"supplyId\": \"5\", \"supplyName\": \"Còi Denso\", \"price\": 500000, \"quantity\": 3}\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"carId\": 3,\n" +
                "    \"licensePlate\": \"30H-456.78\",\n" +
                "    \"ownerName\": \"Lê Hương Giang\",\n" +
                "    \"carBrandId\": 3,\n" +
                "    \"carBrandText\": \"Suzuki\",\n" +
                "    \"carTypeId\": 3,\n" +
                "    \"carTypeText\": \"SUV\",\n" +
                "    \"phoneNumber\": \"0912345678\",\n" +
                "    \"receiveDate\": \"2024, 06, 18\",\n" +
                "    \"carImage\": 0,\n" +
                "    \"state\": 2,\n" +
                "    \"carServices\": [\n" +
                "      {\"serviceId\": \"1\", \"serviceName\": \"BẢO DƯỠNG CẤP NHỎ (5000) KM\", \"price\": 199000},\n" +
                "      {\"serviceId\": \"11\", \"serviceName\": \"Cân bằng động (100k/bánh)\", \"price\": 400000}\n" +
                "    ],\n" +
                "    \"carSupplies\": [\n" +
                "      {\"supplyId\": \"1\", \"supplyName\": \"Dung dịch phụ gia súc béc xăng (Wurth)\", \"price\": 300000, \"quantity\": 2},\n" +
                "      {\"supplyId\": \"4\", \"supplyName\": \"Nước làm mát (Asin, Jinco)\", \"price\": 150000, \"quantity\": 5}\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"carId\": 4,\n" +
                "    \"licensePlate\": \"79D-012.34\",\n" +
                "    \"ownerName\": \"Phạm Quốc Anh\",\n" +
                "    \"carBrandId\": 4,\n" +
                "    \"carBrandText\": \"Vinfast\",\n" +
                "    \"carTypeId\": 4,\n" +
                "    \"carTypeText\": \"Luxury\",\n" +
                "    \"phoneNumber\": \"0976543210\",\n" +
                "    \"receiveDate\": \"2024, 08, 25\",\n" +
                "    \"carImage\": 0,\n" +
                "    \"state\": 3,\n" +
                "    \"carServices\": [\n" +
                "      {\"serviceId\": \"9\", \"serviceName\": \"Kiểm tra hệ thống điện chuyên sâu\", \"price\": 1200000},\n" +
                "      {\"serviceId\": \"12\", \"serviceName\": \"Cân chỉnh độ chụm\", \"price\": 800000}\n" +
                "    ],\n" +
                "    \"carSupplies\": [\n" +
                "      {\"supplyId\": \"2\", \"supplyName\": \"Dung dịch hụ gia súc nhớt (Wurth)\", \"price\": 300000, \"quantity\": 3},\n" +
                "      {\"supplyId\": \"6\", \"supplyName\": \"Gạt mưa Bosch cứng\", \"price\": 350000, \"quantity\": 2}\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"carId\": 5,\n" +
                "    \"licensePlate\": \"60C-876.54\",\n" +
                "    \"ownerName\": \"Hoàng Thu Trang\",\n" +
                "    \"carBrandId\": 2,\n" +
                "    \"carBrandText\": \"Aston Martin\",\n" +
                "    \"carTypeId\": 4,\n" +
                "    \"carTypeText\": \"Luxury\",\n" +
                "    \"phoneNumber\": \"0961112222\",\n" +
                "    \"receiveDate\": \"2024, 10, 05\",\n" +
                "    \"carImage\": 0,\n" +
                "    \"state\": 0,\n" +
                "    \"carServices\": [],\n" +
                "    \"carSupplies\": []\n" +
                "  },\n" +
                "  {\n" +
                "    \"carId\": 6,\n" +
                "    \"licensePlate\": \"15B-789.01\",\n" +
                "    \"ownerName\": \"Đỗ Văn Minh\",\n" +
                "    \"carBrandId\": 4,\n" +
                "    \"carBrandText\": \"Vinfast\",\n" +
                "    \"carTypeId\": 4,\n" +
                "    \"carTypeText\": \"Luxury\",\n" +
                "    \"phoneNumber\": \"0345967735\",\n" +
                "    \"receiveDate\": \"2024, 11, 30\",\n" +
                "    \"carImage\": 0,\n" +
                "    \"state\": 1,\n" +
                "    \"carServices\": [\n" +
                "      {\"serviceId\": \"9\", \"serviceName\": \"Kiểm tra hệ thống điện chuyên sâu\", \"price\": 1200000},\n" +
                "      {\"serviceId\": \"12\", \"serviceName\": \"Cân chỉnh độ chụm\", \"price\": 800000}\n" +
                "    ],\n" +
                "    \"carSupplies\": [\n" +
                "      {\"supplyId\": \"2\", \"supplyName\": \"Dung dịch hụ gia súc nhớt (Wurth)\", \"price\": 300000, \"quantity\": 3},\n" +
                "      {\"supplyId\": \"6\", \"supplyName\": \"Gạt mưa Bosch cứng\", \"price\": 350000, \"quantity\": 2}\n" +
                "    ]\n" +
                "  }\n" +
                "]";

//        Converting json into List<Car>
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
        List<Car> cars = gson.fromJson(json, new TypeToken<List<Car>>() {}.getType());
        List<Car> newCars = new ArrayList<>();

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            if (car.getState() == 0) {
                newCars.add(car);
            }
        }

//        CAR SPINNER:
        CarSpinnerAdapter carSpinnerAdapter = new CarSpinnerAdapter(this, R.layout.item_car_brand_selected, newCars);
        spinner.setAdapter(carSpinnerAdapter);
        TextView tvCarServicePriceHeader = findViewById(R.id.tvCarServicePriceHeader);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentCar = (Car) adapterView.getItemAtPosition(i);
                if (currentCar.getLicensePlate() != tvLicensePlate.getText()) {
                    tvLicensePlate.setText(currentCar.getLicensePlate());
                    tvCarBrand.setText(currentCar.getCarBrandText());
                    tvCarType.setText(currentCar.getCarTypeText());
                    tvOwnerName.setText(currentCar.getOwnerName());
                    tvPhoneNumber.setText(currentCar.getPhoneNumber());
                    tvReceiveDate.setText(dateFormatter.format(currentCar.getReceiveDate()));
                    if (currentCar.getCarImage() == 0) {
                        ivCarImage.setImageResource(R.drawable.no_image);
                    } else {
                        ivCarImage.setImageResource(currentCar.getCarImage());
                    }

                    tvCarServicePriceHeader.setText(String.format("Giá (%s)", currentCar.getCarTypeText()));
//                TODO: CALL API CAR SERVICES BASES ON CAR TYPE
//                TODO: THEN SET DATA TO CAR SERVICE ADAPTER
//                TODO: THEN SET DATA TO allCarServices
                    carServiceAdapter.setData(currentCar.getCarServices());
                    carSupplyAdapter.setData(currentCar.getCarSupplies());
                    totalCarSupplyPriceLong = 0;
                    for (int j = 0; j < currentCar.getCarSupplies().size(); j++) {
                        totalCarSupplyPriceLong += currentCar.getCarSupplies().get(j).getPrice();
                    }
                    totalCarSupplyPrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalCarSupplyPriceLong)));
                    totalCarServicePriceLong = 0;
                    for (int j = 0; j < currentCar.getCarServices().size(); j++) {
                        totalCarServicePriceLong += currentCar.getCarServices().get(j).getPrice();
                    }
                    totalCarServicePrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalCarServicePriceLong)));
                    tvTotalPrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalCarServicePriceLong + totalCarSupplyPriceLong)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                AlertDialog.Builder builder = new AlertDialog.Builder(AddRepairCardActivity.this);
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
                        totalCarServicePrice.setText("Tổng: " + currencyFormatter.format(totalPrice) + "đ");
                        totalCarServicePriceLong = totalPrice;
                        tvTotalPrice.setText("Tổng tiền: " + currencyFormatter.format(totalCarSupplyPriceLong + totalCarServicePriceLong) + "đ");
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

//        CarSupplyAdapter
        carSupplyAdapter = new CarSupplyAdapter(getApplicationContext(), CarSupplyAdapter.TYPE_LIST, this);
        RecyclerView recyclerViewCarSupplyList = findViewById(R.id.recyclerViewCarSupplyList);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        recyclerViewCarSupplyList.setLayoutManager(linearLayoutManager1);
        recyclerViewCarSupplyList.setFocusable(false);
        carSupplyAdapter.setData(new ArrayList<>());
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
    public void setCarSupplyTotalPrice(long totalPrice) {
        totalCarSupplyPrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalPrice)));
        totalCarSupplyPriceLong = totalPrice;
        tvTotalPrice.setText(String.format("Tổng tiền: %sđ", currencyFormatter.format(totalCarSupplyPriceLong + totalCarServicePriceLong)));
    }
}