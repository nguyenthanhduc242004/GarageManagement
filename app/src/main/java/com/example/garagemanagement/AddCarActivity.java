package com.example.garagemanagement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.garagemanagement.Objects.CarBrand;
import com.example.garagemanagement.Objects.CarType;
import com.example.garagemanagement.adapter.CarBrandSpinnerAdapter;
import com.example.garagemanagement.adapter.CarTypeSpinnerAdapter;
import com.example.garagemanagement.fragments.FragmentPayment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCarActivity extends AppCompatActivity {
    ImageView ivCarImage;
    DatePickerDialog datePickerDialog;
    Button buttonDate;
    Button buttonOpenCamera;
    Button buttonDeleteImage;
    ProgressDialog progressDialog;

    List<CarBrand> allCarBrands = new ArrayList<>();
    List<CarType> allCarTypes = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String carBrandId;
    String carTypeId;
    Date receiveDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_car);
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


//        CAR BRAND SPINNER:
        Spinner spinnerCarBrand = findViewById(R.id.spinnerCarBrand);
        progressDialog.show();
        db.collection("CarBrand")
                .whereEqualTo("usable", true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            CarBrand carBrand = documentSnapshot.toObject(CarBrand.class);
                            carBrand.setCarBrandId(documentSnapshot.getId());
                            allCarBrands.add(carBrand);
                        }

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        CarBrandSpinnerAdapter carBrandSpinnerAdapter = new CarBrandSpinnerAdapter(getApplicationContext(), R.layout.item_car_brand_selected, allCarBrands);
                        spinnerCarBrand.setAdapter(carBrandSpinnerAdapter);
                        spinnerCarBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                // Toast.makeText(AddCarActivity.this, carBrandAdapter.getItem(i), Toast.LENGTH_SHORT).show();
                                carBrandId = carBrandSpinnerAdapter.getItem(i).getCarBrandId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                });


//        CAR BRAND SPINNER:
        Spinner spinnerCarType = findViewById(R.id.spinnerCarType);
        progressDialog.show();
        db.collection("CarType")
                .whereEqualTo("usable", true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            CarType carType = documentSnapshot.toObject(CarType.class);
                            carType.setCarTypeId(documentSnapshot.getId());
                            allCarTypes.add(carType);
                        }

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        CarTypeSpinnerAdapter carTypeSpinnerAdapter = new CarTypeSpinnerAdapter(getApplicationContext(), R.layout.item_car_type_selected, allCarTypes);
                        spinnerCarType.setAdapter(carTypeSpinnerAdapter);
                        spinnerCarType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                //                Toast.makeText(AddCarActivity.this, carBrandAdapter.getItem(i), Toast.LENGTH_SHORT).show();
                                carTypeId = carTypeSpinnerAdapter.getItem(i).getCarTypeId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                });

//        DATE PICKER:
        buttonDate = findViewById(R.id.buttonDate);
        initDatePicker();
        buttonDate.setText(getTodaysDate());
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

//        CAR IMAGE:
        ivCarImage = findViewById(R.id.ivCarImage);
        buttonOpenCamera = findViewById(R.id.buttonOpenCamera);
        buttonOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(openCamera, 100);
            }
        });

        buttonDeleteImage = findViewById(R.id.buttonDeleteImage);
        buttonDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivCarImage.setImageBitmap(null);
                buttonDeleteImage.setVisibility(View.GONE);
                buttonOpenCamera.setText("Thêm Ảnh Chụp");
            }
        });

//        FOOTER BUTTONS:
        Button buttonEscape = findViewById(R.id.footerButtonLeft);
        buttonEscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EditText etLicensePlate = findViewById(R.id.etLicensePlate);
        EditText etOwnerName = findViewById(R.id.etOwnerName);
        EditText etPhoneNumber = findViewById(R.id.etPhoneNumber);
        Button buttonAdd = findViewById(R.id.footerButtonRight);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> carData = new HashMap<>();
                carData.put("licensePlate", etLicensePlate.getText().toString());
                carData.put("carBrandId", carBrandId);
                carData.put("carTypeId", carTypeId);
                carData.put("ownerName", etOwnerName.getText().toString());
                carData.put("phoneNumber", etPhoneNumber.getText().toString());
                carData.put("receiveDate", receiveDate);
                carData.put("carImage", 0);
                carData.put("state", 0);
                db.collection("Car")
                        .add(carData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(), "Tiếp nhận xe thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Tiếp nhận xe không thành công!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month++;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }


    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                 month = month + 1;
                 String date = makeDateString(day, month, year);
                 buttonDate.setText(date);
                 receiveDate = new Date(year - 1900, month - 1, day);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(FragmentPayment.atEndOfDay((new Date())).getTime());
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


}