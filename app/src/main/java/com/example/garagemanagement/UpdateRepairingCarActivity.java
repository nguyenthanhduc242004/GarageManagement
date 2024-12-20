package com.example.garagemanagement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.garagemanagement.Objects.CarBrand;
import com.example.garagemanagement.Objects.CarService;
import com.example.garagemanagement.Objects.CarSupply;
import com.example.garagemanagement.Objects.CarType;
import com.example.garagemanagement.adapter.CarBrandSpinnerAdapter;
import com.example.garagemanagement.adapter.CarServiceAdapter;
import com.example.garagemanagement.adapter.CarSupplyAdapter;
import com.example.garagemanagement.adapter.CarTypeSpinnerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateRepairingCarActivity extends AppCompatActivity implements RecyclerViewInterface, CustomCarSupplyDialog.CustomCarSupplyDialogInterface {
    DatePickerDialog datePickerDialog;
    Button buttonOpenCamera;
    Button buttonDeleteImage;
    ImageView ivCarImage;
    Button buttonDate;

    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    NumberFormat currencyFormatter = new DecimalFormat("#,###");
    CarServiceAdapter carServiceAdapter;
    TextView totalCarServicePrice;
    long totalCarServicePriceLong = 0;
    CarSupplyAdapter carSupplyAdapter;
    TextView totalCarSupplyPrice;
    long totalCarSupplyPriceLong = 0;
    TextView tvTotalPrice;

    public static List<CarSupply> selectedCarSupplies = new ArrayList<>();

    androidx.appcompat.app.AlertDialog alertDialog;

    public void openCarSupplyDialog(View view) {
        CustomCarSupplyDialog customCarSupplyDialog = new CustomCarSupplyDialog();
        customCarSupplyDialog.show(getSupportFragmentManager(), "Chọn Vật Tư, Dung Dịch");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_repairing_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    //        CALL API CAR SERVICES:
//        TODO: WRONG JSON!!! -> CALLING API CAR SERVICES WILL BE BASED ON CAR TYPE!!!
        String carServicesJson = "[\n" +
                "    {\n" +
                "        \"serviceId\": 1,\n" +
                "        \"serviceName\": \"BẢO DƯỠNG CẤP NHỎ (5000) KM\",\n" +
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
        Gson gson2 = new GsonBuilder().create();
        List<CarService> allCarServices = gson2.fromJson(carServicesJson, new TypeToken<List<CarService>>() {}.getType());


//        BACK BUTTON:
        ImageButton imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        FOOTER BUTTONS:
        Button footerButtonLeft = findViewById(R.id.footerButtonLeft);
        footerButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button footerButtonRight = findViewById(R.id.footerButtonRight);

//        BASIC CAR INFORMATION:
        String licensePlate = getIntent().getStringExtra("LICENSE_PLATE");
        String carBrandId = getIntent().getStringExtra("CAR_BRAND_ID");
        String carBrandText = getIntent().getStringExtra("CAR_BRAND_TEXT");
        String carTypeId = getIntent().getStringExtra("CAR_TYPE_ID");
        String carTypeText = getIntent().getStringExtra("CAR_TYPE_TEXT");
        String ownerName = getIntent().getStringExtra("OWNER_NAME");
        String phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        String receiveDate = getIntent().getStringExtra("RECEIVE_DATE");
        int carImage = getIntent().getIntExtra("CAR_IMAGE", 0);
        int state = getIntent().getIntExtra("STATE", -1);
        List<CarService> selectedCarServices = (List<CarService>) getIntent().getSerializableExtra("CAR_SERVICES");
        selectedCarSupplies = (List<CarSupply>) getIntent().getSerializableExtra("CAR_SUPPLIES");

        EditText etLicensePlate = findViewById(R.id.etLicensePlate);
        Spinner spinnerCarBrand = findViewById(R.id.spinnerCarBrand);
        Spinner spinnerCarType = findViewById(R.id.spinnerCarType);
        EditText etOwnerName = findViewById(R.id.etOwnerName);
        EditText etPhoneNumber = findViewById(R.id.etPhoneNumber);
        buttonDate = findViewById(R.id.buttonDate);
        ivCarImage = findViewById(R.id.ivCarImage);

        etLicensePlate.setText(licensePlate);
        etOwnerName.setText(ownerName);
        etPhoneNumber.setText(phoneNumber);
        buttonDate.setText(receiveDate);

//        CAR BRAND SPINNER:
        // CALL API CarBrand:
        String carBrandJson = "[\n" +
                "        {\"carBrandId\": 1, \"carBrandText\": \"Honda\"},\n" +
                "        {\"carBrandId\": 2, \"carBrandText\": \"Aston Martin\"},\n" +
                "        {\"carBrandId\": 3, \"carBrandText\": \"Suzuki\"},\n" +
                "        {\"carBrandId\": 4, \"carBrandText\": \"Vinfast\"}\n" +
                "]";
        Gson gson = new GsonBuilder().create();
        List<CarBrand> carBrands = gson.fromJson(carBrandJson, new TypeToken<List<CarBrand>>() {}.getType());

        CarBrandSpinnerAdapter carBrandSpinnerAdapter = new CarBrandSpinnerAdapter(this, R.layout.item_car_brand_selected, carBrands);
        spinnerCarBrand.setAdapter(carBrandSpinnerAdapter);
        int spinnerCarBrandIndex = 0;
        for (int i = 0; i < carBrands.size(); i++) {
            if (carBrands.get(i).getCarBrandId().equals(carBrandId)) {
                spinnerCarBrandIndex = i;
                break;
            }
        }
        spinnerCarBrand.setSelection(spinnerCarBrandIndex);
        spinnerCarBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(AddCarActivity.this, carBrandAdapter.getItem(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        CAR TYPE SPINNER:
        // CALL API CarBrand:
        String carTypeJson = "[\n" +
                "  {\"carTypeId\": 1, \"carTypeText\": \"Mini\"},\n" +
                "  {\"carTypeId\": 2, \"carTypeText\": \"Sedan\"},\n" +
                "  {\"carTypeId\": 3, \"carTypeText\": \"SUV\"},\n" +
                "  {\"carTypeId\": 4, \"carTypeText\": \"Luxury\"}\n" +
                "]";
        Gson gson1 = new GsonBuilder().create();
        List<CarType> carTypes = gson1.fromJson(carTypeJson, new TypeToken<List<CarType>>() {}.getType());

        CarTypeSpinnerAdapter carTypeSpinnerAdapter = new CarTypeSpinnerAdapter(this, R.layout.item_car_type_selected, carTypes);
        spinnerCarType.setAdapter(carTypeSpinnerAdapter);
        int spinnerCarTypeIndex = 0;
        for (int i = 0; i < carTypes.size(); i++) {
            if (carTypes.get(i).getCarTypeId().equals(carTypeId)) {
                spinnerCarTypeIndex = i;
                break;
            }
        }
        TextView tvCarServicePriceHeader = findViewById(R.id.tvCarServicePriceHeader);
        spinnerCarType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(AddCarActivity.this, carBrandAdapter.getItem(i), Toast.LENGTH_SHORT).show();
                tvCarServicePriceHeader.setText(String.format("Giá (%s)", carTypes.get(i).getCarTypeText()));
//                TODO: CALL API CAR SERVICES BASES ON CAR TYPE
//                TODO: THEN SET DATA TO CAR SERVICE ADAPTER
//                TODO: THEN SET DATA TO allCarServices
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerCarType.setSelection(spinnerCarTypeIndex);

//        DATE PICKER:
        buttonDate = findViewById(R.id.buttonDate);
        initDatePicker();
        buttonDate.setText(receiveDate);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

//        CAR IMAGE:
        ivCarImage = findViewById(R.id.ivCarImage);
        buttonOpenCamera = findViewById(R.id.buttonOpenCamera);
        buttonDeleteImage = findViewById(R.id.buttonDeleteImage);
        if (carImage == 0) {
            ivCarImage.setImageResource(R.drawable.no_image);
        } else {
            ivCarImage.setImageResource(carImage);
        }

        buttonOpenCamera.setText("Chụp Lại Ảnh");
        buttonOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(openCamera, 100);
            }
        });

        buttonDeleteImage.setVisibility(View.VISIBLE);
        ImageView finalIvCarImage = ivCarImage;
        buttonDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalIvCarImage.setImageBitmap(null);
                buttonDeleteImage.setVisibility(View.GONE);
                buttonOpenCamera.setText("Thêm Ảnh Chụp");
            }
        });

//        TOGGLE CAR INFORMATION BUTTON:
        LinearLayout carDetailWrapper = findViewById(R.id.car_detail_wrapper);
        carDetailWrapper.setVisibility(View.VISIBLE);

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

//        ADD CAR SERVICE BUTTON:
        String[] carServiceNames = new String[allCarServices.size()];
        for (int i = 0; i < allCarServices.size(); i++) {
            carServiceNames[i] = allCarServices.get(i).getServiceName();
        }

        boolean[] checkedServices = new boolean[allCarServices.size()];
        boolean[] uncheckableServices = new boolean[allCarServices.size()];
        Button addCarServiceButton = findViewById(R.id.addCarServiceButton);
        List<CarService> finalCarServices = allCarServices;
        for (int i = 0; i < finalCarServices.size(); i++) {
            for (int k = 0; k < selectedCarServices.size(); k++) {
                if (finalCarServices.get(i).getServiceId().equals(selectedCarServices.get(k).getServiceId())) {
                    checkedServices[i] = true;
                    uncheckableServices[i] = true;
                    break;
                }
            }
        }
        addCarServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateRepairingCarActivity.this);
                builder.setTitle("Chọn Dịch Vụ");
                builder.setMultiChoiceItems(carServiceNames, checkedServices, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                        // Update current focused item's checked status
                        alertDialog.getListView().setItemChecked(which, uncheckableServices[which]);
                        if (uncheckableServices[which]) {
                            checkedServices[which] = true;
                        } else {
                            checkedServices[which] = isChecked;
                        }
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

                alertDialog = builder.create();
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
        Gson gson3 = new GsonBuilder().create();
        List<CarSupply> allCarSupplies = gson3.fromJson(carServicesJson, new TypeToken<List<CarSupply>>() {}.getType());

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
        Button addCarSupplyButton = findViewById(R.id.addCarSupplyButton);
        addCarSupplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCarSupplyDialog(view);
            }
        });

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalPrice.setText(String.format("Tổng tiền: %sđ", currencyFormatter.format(totalCarSupplyPriceLong + totalCarServicePriceLong)));
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                buttonDate.setText(date);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }


    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                buttonOpenCamera.setText("Chụp Lại Ảnh");
                buttonDeleteImage.setVisibility(View.VISIBLE);
                try {
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    ivCarImage.setImageBitmap(image);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
        UpdateRepairingCarActivity.selectedCarSupplies = carSupplies;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(int position, int state) {

    }
}