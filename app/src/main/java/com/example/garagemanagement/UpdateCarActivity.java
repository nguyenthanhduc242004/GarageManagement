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
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
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

public class UpdateCarActivity extends AppCompatActivity {
    Button buttonDate;
    DatePickerDialog datePickerDialog;
    ImageView ivCarImage;
    Button buttonOpenCamera;
    Button buttonDeleteImage;

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    ProgressDialog progressDialog;
    List<CarBrand> allCarBrands = new ArrayList<>();
    List<CarType> allCarTypes = new ArrayList<>();

    String carBrandId;
    String carTypeId;

    boolean gotCarBrandText = false;
    boolean gotCarTypeText = false;
    String carBrandText;
    String carTypeText;
    Date receiveDate;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_car);
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

//        BASIC CAR INFORMATION:
        String carId = getIntent().getStringExtra("CAR_ID");
        String licensePlate = getIntent().getStringExtra("LICENSE_PLATE");
        carBrandId = getIntent().getStringExtra("CAR_BRAND_ID");
        carBrandText = getIntent().getStringExtra("CAR_BRAND_TEXT");
        carTypeId = getIntent().getStringExtra("CAR_TYPE_ID");
        carTypeText = getIntent().getStringExtra("CAR_TYPE_TEXT");
        String ownerName = getIntent().getStringExtra("OWNER_NAME");
        String phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        receiveDate = (Date) getIntent().getSerializableExtra("RECEIVE_DATE");
        int carImage = getIntent().getIntExtra("CAR_IMAGE", 0);
        int state = getIntent().getIntExtra("STATE", -1);

        EditText etLicensePlate = findViewById(R.id.etLicensePlate);
        EditText etOwnerName = findViewById(R.id.etOwnerName);
        EditText etPhoneNumber = findViewById(R.id.etPhoneNumber);
        buttonDate = findViewById(R.id.buttonDate);
        ivCarImage = findViewById(R.id.ivCarImage);

        etLicensePlate.setText(licensePlate);
        etOwnerName.setText(ownerName);
        etPhoneNumber.setText(phoneNumber);

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
                        for (int i = 0; i < allCarBrands.size(); i++) {
                            if (allCarBrands.get(i).getCarBrandId().equals(carBrandId)) {
                                spinnerCarBrand.setSelection(i);
                                break;
                            }
                        }
                        spinnerCarBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                // Toast.makeText(AddCarActivity.this, carBrandAdapter.getItem(i), Toast.LENGTH_SHORT).show();
                                carBrandId = carBrandSpinnerAdapter.getItem(i).getCarBrandId();
                                carBrandText = carBrandSpinnerAdapter.getItem(i).getCarBrandText();
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
                        for (int i = 0; i < allCarTypes.size(); i++) {
                            if (allCarTypes.get(i).getCarTypeId().equals(carTypeId)) {
                                spinnerCarType.setSelection(i);
                                break;
                            }
                        }
                        spinnerCarType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                //                Toast.makeText(AddCarActivity.this, carBrandAdapter.getItem(i), Toast.LENGTH_SHORT).show();
                                carTypeId = carTypeSpinnerAdapter.getItem(i).getCarTypeId();
                                carTypeText = carTypeSpinnerAdapter.getItem(i).getCarTypeText();
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
        buttonDate.setText(formatter.format(receiveDate));
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

//        FOOTER BUTTONS:
        Button buttonEscape = findViewById(R.id.footerButtonLeft);
        buttonEscape.setText("Quay Lại");
        buttonEscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button buttonUpdate = findViewById(R.id.footerButtonRight);
        buttonUpdate.setText("Cập nhật");
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
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
                        .document(carId)
                        .update(carData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Sửa thông tin xe thành công!", Toast.LENGTH_SHORT).show();
                                NewCarDetailActivity.licensePlate = etLicensePlate.getText().toString();
                                NewCarDetailActivity.carBrandId = carBrandId;
                                NewCarDetailActivity.carTypeId = carTypeId;
                                NewCarDetailActivity.phoneNumber = etPhoneNumber.getText().toString();
                                NewCarDetailActivity.receiveDate = receiveDate;
                                NewCarDetailActivity.ownerName = etOwnerName.getText().toString();
                                NewCarDetailActivity.carBrandText = carBrandText;
                                NewCarDetailActivity.carTypeText = carTypeText;
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

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                buttonDate.setText(date);
                receiveDate = new Date (year - 1900, month - 1, day);
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

//    WHEN USER HAVE TAKEN A PICTURE:
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