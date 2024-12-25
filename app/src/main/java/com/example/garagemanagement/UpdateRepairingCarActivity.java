package com.example.garagemanagement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.garagemanagement.Objects.CarSupply;
import com.example.garagemanagement.Objects.CarType;
import com.example.garagemanagement.adapter.CarBrandSpinnerAdapter;
import com.example.garagemanagement.adapter.CarServiceAdapter;
import com.example.garagemanagement.adapter.CarSupplyAdapter;
import com.example.garagemanagement.adapter.CarTypeSpinnerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    List<CarService> allCarServices = new ArrayList<>();
    List<CarSupply> allCarSupplies = new ArrayList<>();
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<CarService> fixedCarServices = new ArrayList<>();
    List<CarSupply> fixedCarSupplies = new ArrayList<>();

    List<CarSupply> selectedCarSupplies = new ArrayList<>();

    List<CarService> selectedCarServices = new ArrayList<>();

    androidx.appcompat.app.AlertDialog alertDialog;

    public void openCarSupplyDialog(View view) {
        CustomCarSupplyDialog customCarSupplyDialog = new CustomCarSupplyDialog();
        if (customCarSupplyDialog.getCarSupplyAdapter() != null) {
            customCarSupplyDialog.getCarSupplyAdapter().setData(selectedCarSupplies);
        }
        customCarSupplyDialog.show(getSupportFragmentManager(), "Chọn Vật Tư, Dung Dịch");
    }

    @Override
    protected void onStop() {
        super.onStop();
        CustomCarSupplyDialog.selectedCarSupplies = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */


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

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang tải...");
//        progressDialog.show();

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
        int state = getIntent().getIntExtra("STATE", -1);
        fixedCarServices = (List<CarService>) getIntent().getSerializableExtra("CAR_SERVICES");
        fixedCarSupplies = (List<CarSupply>) getIntent().getSerializableExtra("CAR_SUPPLIES");

        selectedCarServices = fixedCarServices;
        selectedCarSupplies = fixedCarSupplies;

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
        tvReceiveDate.setText(dateFormatter.format(receiveDate));
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
//
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

////        CarServiceAdapter
        carServiceAdapter = new CarServiceAdapter(getApplicationContext(), CarServiceAdapter.TYPE_LIST,  carTypeId, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerViewCarServiceList = findViewById(R.id.recyclerViewCarServiceList);
        recyclerViewCarServiceList.setLayoutManager(linearLayoutManager);
        recyclerViewCarServiceList.setFocusable(false);
        carServiceAdapter.setData(fixedCarServices);
        recyclerViewCarServiceList.setAdapter(carServiceAdapter);

        //      SET totalCarServicePrice
        totalCarServicePrice = findViewById(R.id.totalCarServicePrice);
        for (int i = 0; i < selectedCarServices.size(); i++) {
            totalCarServicePriceLong += selectedCarServices.get(i).getPrices().get(carTypeId);
        }
        totalCarServicePrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalCarServicePriceLong)));

//        ADD CAR SERVICE BUTTON:
        Button addCarServiceButton = findViewById(R.id.addCarServiceButton);
        addCarServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allCarServices.isEmpty()) {
                    progressDialog.show();
                    db.collection("CarService")
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        CarService carService = documentSnapshot.toObject(CarService.class);
                                        carService.setServiceId(documentSnapshot.getId());
                                        allCarServices.add(carService);
                                    }
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                    String[] carServiceNames = new String[allCarServices.size()];
                                    boolean[] checkedServices = new boolean[allCarServices.size()];
                                    boolean[] uncheckableServices = new boolean[allCarServices.size()];
                                    for (int i = 0; i < allCarServices.size(); i++) {
                                        carServiceNames[i] = allCarServices.get(i).getServiceName();
                                        for (int j = 0; j < selectedCarServices.size(); j++) {
                                            if (selectedCarServices.get(j).getServiceId().equals(allCarServices.get(i).getServiceId())) {
                                                checkedServices[i] = true;
                                                uncheckableServices[i] = true;
                                                break;
                                            }
                                        }
                                    }

                                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateRepairingCarActivity.this);
                                    builder.setTitle("Chọn Dịch Vụ");
                                    builder.setMultiChoiceItems(carServiceNames, checkedServices, new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                                            // Update current focused item's checked status
                                            if (uncheckableServices[which]) {
                                                ((androidx.appcompat.app.AlertDialog) dialogInterface).getListView().setItemChecked(which, true);
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
                                            selectedCarServices = new ArrayList<>();
                                            long totalPrice = 0;
                                            for (int i = 0; i < checkedServices.length; i++) {
                                                boolean checked = checkedServices[i];
                                                if (checked) {
                                                    selectedCarServices.add(allCarServices.get(i));
                                                    totalPrice += allCarServices.get(i).getPrices().get(carTypeId);
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

                                    androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            });
                }
                else {
                    String[] carServiceNames = new String[allCarServices.size()];
                    boolean[] checkedServices = new boolean[allCarServices.size()];
                    boolean[] uncheckableServices = new boolean[allCarServices.size()];
                    for (int i = 0; i < allCarServices.size(); i++) {
                        carServiceNames[i] = allCarServices.get(i).getServiceName();
                        for (int j = 0; j < selectedCarServices.size(); j++) {
                            if (selectedCarServices.get(j).getServiceId().equals(allCarServices.get(i).getServiceId())) {
                                checkedServices[i] = true;
                                uncheckableServices[i] = true;
                                break;
                            }
                        }
                    }

                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateRepairingCarActivity.this);
                    builder.setTitle("Chọn Dịch Vụ");
                    builder.setMultiChoiceItems(carServiceNames, checkedServices, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                            // Update current focused item's checked status
                            if (uncheckableServices[which]) {
                                ((androidx.appcompat.app.AlertDialog) dialogInterface).getListView().setItemChecked(which, true);
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
                            selectedCarServices = new ArrayList<>();
                            long totalPrice = 0;
                            for (int i = 0; i < checkedServices.length; i++) {
                                boolean checked = checkedServices[i];
                                if (checked) {
                                    selectedCarServices.add(allCarServices.get(i));
                                    totalPrice += allCarServices.get(i).getPrices().get(carTypeId);
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

                    androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

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
        carSupplyAdapter.setData(fixedCarSupplies);
        recyclerViewCarSupplyList.setAdapter(carSupplyAdapter);

        //      SET totalCarSupplyPrice
        totalCarSupplyPrice = findViewById(R.id.totalCarSupplyPrice);
        for (int i = 0; i < selectedCarSupplies.size(); i++) {
            totalCarSupplyPriceLong += selectedCarSupplies.get(i).getPrice() * selectedCarSupplies.get(i).getQuantity();
        }
        totalCarSupplyPrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalCarSupplyPriceLong)));


        totalCarSupplyPrice = findViewById(R.id.totalCarSupplyPrice);
        Button addCarSupplyButton = findViewById(R.id.addCarSupplyButton);
        addCarSupplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CustomCarSupplyDialog.allCarSupplies.isEmpty()) {
                    progressDialog.show();
                    db.collection("CarSupply")
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                        CarSupply carSupply = documentSnapshot.toObject(CarSupply.class);
                                        carSupply.setSupplyId(documentSnapshot.getId());
                                        carSupply.setQuantity(0);
                                        allCarSupplies.add(carSupply);
                                    }
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    CustomCarSupplyDialog.allCarSupplies = allCarSupplies;
                                    CustomCarSupplyDialog.selectedCarSupplies = selectedCarSupplies;
                                    openCarSupplyDialog(view);
                                }
                            });
                } else {
                    openCarSupplyDialog(view);
                    CustomCarSupplyDialog.selectedCarSupplies = selectedCarSupplies;
                }
            }
        });

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalPrice.setText(String.format("Tổng tiền: %sđ", currencyFormatter.format(totalCarSupplyPriceLong + totalCarServicePriceLong)));

        Button buttonUpdate = findViewById(R.id.footerButtonRight);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> carServices = new ArrayList<>();
                for (int i = 0; i < selectedCarServices.size(); i++) {
                    carServices.add(selectedCarServices.get(i).getServiceId());
                }
                Map<String, Integer> carSupplies = new HashMap<>();
                for (int i = 0; i < selectedCarSupplies.size(); i++) {
                    carSupplies.put(selectedCarSupplies.get(i).getSupplyId(), selectedCarSupplies.get(i).getQuantity());
                }
                db.collection("Car")
                        .document(carId)
                        .update("carServices", carServices, "carSupplies", carSupplies)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Sửa thông tin xe thành công!", Toast.LENGTH_SHORT).show();
                                RepairingCarDetailActivity.selectedCarServices = selectedCarServices;
                                RepairingCarDetailActivity.selectedCarSupplies = selectedCarSupplies;
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Sửa thông tin xe không thành công!", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
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
        selectedCarSupplies = carSupplies;
//        CustomCarSupplyDialog.selectedCarSupplies = carSupplies;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(int position, int state) {

    }
}