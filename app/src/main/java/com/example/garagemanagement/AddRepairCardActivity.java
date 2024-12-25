package com.example.garagemanagement;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.example.garagemanagement.fragments.FragmentHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddRepairCardActivity extends AppCompatActivity implements RecyclerViewInterface, CustomCarSupplyDialog.CustomCarSupplyDialogInterface {
    Car currentCar;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    NumberFormat currencyFormatter = new DecimalFormat("#,###");
    CarServiceAdapter carServiceAdapter;
    TextView totalCarServicePrice;
    long totalCarServicePriceLong = 0;
    CarSupplyAdapter carSupplyAdapter;
    TextView totalCarSupplyPrice;
    long totalCarSupplyPriceLong = 0;
    TextView tvTotalPrice;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;

    List<Car> cars = new ArrayList<>();
    List<Car> newCars = new ArrayList<>();
    CarSpinnerAdapter carSpinnerAdapter;
    List<CarService> allCarServices = new ArrayList<>();
    List<CarSupply> allCarSupplies = new ArrayList<>();

    List<CarService> selectedCarServices = new ArrayList<>();
    public static List<CarSupply> selectedCarSupplies = new ArrayList<>();



    public void openCarSupplyDialog(View view) {
        CustomCarSupplyDialog customCarSupplyDialog = new CustomCarSupplyDialog();
        customCarSupplyDialog.show(getSupportFragmentManager(), "Chọn Vật Tư, Dung Dịch");
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (int i = 0; i < newCars.size(); i++) {
            newCars.get(i).setCarServiceList(new ArrayList<>());
            newCars.get(i).setCarSupplyList(new ArrayList<>());
        }
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



//        BACK BUTTON:
        ImageButton imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang tải...");
//        progressDialog.show();


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

        // GET CARS DATA
        cars = FragmentHome.cars;
        newCars = new ArrayList<>();
        for (Car car : cars) {
            if (car.getState() == 0) {
                newCars.add(car);
            }
        }
        currentCar = newCars.get(0);
        carSpinnerAdapter = new CarSpinnerAdapter(getApplicationContext(), R.layout.item_car_brand_selected, newCars);
        spinner.setAdapter(carSpinnerAdapter);

//        SET SPINNER POSITION
        String passedCarId = getIntent().getStringExtra("CAR_ID");
        if (passedCarId != null) {
            int spinnerPosition = 0;
            for (int i = 0; i < newCars.size(); i++) {
                if (passedCarId.equals(newCars.get(i).getCarId())) {
                    spinnerPosition = i;
                    break;
                }
            }
            spinner.setSelection(spinnerPosition);
        }

        totalCarServicePrice = findViewById(R.id.totalCarServicePrice);
        totalCarSupplyPrice = findViewById(R.id.totalCarSupplyPrice);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

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
                    carServiceAdapter.setData(currentCar.getCarServiceList());
                    totalCarSupplyPriceLong = 0;
                    for (int j = 0; j < currentCar.getCarSupplyList().size(); j++) {
                        totalCarSupplyPriceLong += currentCar.getCarSupplyList().get(j).getPrice() * currentCar.getCarSupplyList().get(j).getQuantity();
                    }

                    totalCarSupplyPrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalCarSupplyPriceLong)));
                    carSupplyAdapter.setData(currentCar.getCarSupplyList());
                    totalCarServicePriceLong = 0;
                    for (int j = 0; j < currentCar.getCarServiceList().size(); j++) {
                        totalCarServicePriceLong += currentCar.getCarServiceList().get(j).getPrices().get(currentCar.getCarTypeId());
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
        carServiceAdapter = new CarServiceAdapter(getApplicationContext(), CarServiceAdapter.TYPE_LIST,  currentCar.getCarTypeId(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerViewCarServiceList = findViewById(R.id.recyclerViewCarServiceList);
        recyclerViewCarServiceList.setLayoutManager(linearLayoutManager);
        recyclerViewCarServiceList.setFocusable(false);
        carServiceAdapter.setData(new ArrayList<>());
        recyclerViewCarServiceList.setAdapter(carServiceAdapter);

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
                                    for (int i = 0; i < allCarServices.size(); i++) {
                                        carServiceNames[i] = allCarServices.get(i).getServiceName();
                                        for (int j = 0; j < selectedCarServices.size(); j++) {
                                            if (selectedCarServices.get(j).getServiceId().equals(allCarServices.get(i).getServiceId())) {
                                                checkedServices[i] = true;
                                                break;
                                            }
                                        }
                                    }

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
                                            selectedCarServices = new ArrayList<>();
                                            long totalPrice = 0;
                                            for (int i = 0; i < checkedServices.length; i++) {
                                                boolean checked = checkedServices[i];
                                                if (checked) {
                                                    selectedCarServices.add(allCarServices.get(i));
                                                    totalPrice += allCarServices.get(i).getPrices().get(currentCar.getCarTypeId());
                                                }
                                            }
                                            currentCar.setCarServiceList(selectedCarServices);
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
                                    alertDialog.show();
                                }
                            });
                }
                else {
                    String[] carServiceNames = new String[allCarServices.size()];
                    boolean[] checkedServices = new boolean[allCarServices.size()];
                    for (int i = 0; i < allCarServices.size(); i++) {
                        carServiceNames[i] = allCarServices.get(i).getServiceName();
                        for (int j = 0; j < selectedCarServices.size(); j++) {
                            if (selectedCarServices.get(j).getServiceId().equals(allCarServices.get(i).getServiceId())) {
                                checkedServices[i] = true;
                                break;
                            }
                        }
                    }

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
                            selectedCarServices = new ArrayList<>();
                            long totalPrice = 0;
                            for (int i = 0; i < checkedServices.length; i++) {
                                boolean checked = checkedServices[i];
                                if (checked) {
                                    selectedCarServices.add(allCarServices.get(i));
                                    totalPrice += allCarServices.get(i).getPrices().get(currentCar.getCarTypeId());
                                }
                            }
                            currentCar.setCarServiceList(selectedCarServices);
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
                                    openCarSupplyDialog(view);
                                }
                            });
                } else {
                    openCarSupplyDialog(view);
                }
            }
        });

        MaterialButton buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentCar.getCarServiceList().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn ít nhất một dịch vụ!", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> carData = new HashMap<>();
                    carData.put("licensePlate", currentCar.getLicensePlate());
                    carData.put("carBrandId", currentCar.getCarBrandId());
                    carData.put("carTypeId", currentCar.getCarTypeId());
                    carData.put("ownerName", currentCar.getOwnerName());
                    carData.put("phoneNumber", currentCar.getPhoneNumber());
                    carData.put("receiveDate", currentCar.getReceiveDate());
                    List<String> carServicesData = new ArrayList<>();
                    for (int i = 0; i < currentCar.getCarServiceList().size(); i++) {
                        carServicesData.add(currentCar.getCarServiceList().get(i).getServiceId());
                    }
                    carData.put("carServices", carServicesData);
                    Map<String, Integer> carSuppliesData = new HashMap<>();
                    for (int i = 0; i < currentCar.getCarSupplyList().size(); i++) {
                        carSuppliesData.put(currentCar.getCarSupplyList().get(i).getSupplyId(), currentCar.getCarSupplyList().get(i).getQuantity());
                    }
                    carData.put("carSupplies", carSuppliesData);
                    carData.put("state", 1);
                    carData.put("carImage", 0);
                    db.collection("Car")
                            .document(currentCar.getCarId())
                            .update(carData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Lập phiếu sửa chữa thành công!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Lập phiếu sửa chữa không thành công!", Toast.LENGTH_SHORT).show();
                                }
                            });
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

    @Override
    public void setCarSupplyTotalPrice(long totalPrice) {
        totalCarSupplyPrice.setText(String.format("Tổng: %sđ", currencyFormatter.format(totalPrice)));
        totalCarSupplyPriceLong = totalPrice;
        tvTotalPrice.setText(String.format("Tổng tiền: %sđ", currencyFormatter.format(totalCarSupplyPriceLong + totalCarServicePriceLong)));
    }

    @Override
    public void setCarSupplyAdapterData(List<CarSupply> carSupplies) {
        carSupplyAdapter.setData(carSupplies);
        currentCar.setCarSupplyList(carSupplies);
        AddRepairCardActivity.selectedCarSupplies = carSupplies;
    }
}