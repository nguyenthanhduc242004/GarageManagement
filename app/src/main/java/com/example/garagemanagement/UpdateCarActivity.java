package com.example.garagemanagement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.garagemanagement.adapter.CarBrandSpinnerAdapter;
import com.example.garagemanagement.adapter.CarTypeSpinnerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateCarActivity extends AppCompatActivity {
    Button buttonDate;
    DatePickerDialog datePickerDialog;
    ImageView ivCarImage;
    Button buttonOpenCamera;
    Button buttonDeleteImage;

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

//        BASIC CAR INFORMATION:
        String licensePlate = getIntent().getStringExtra("LICENSE_PLATE");
        String carBrand = getIntent().getStringExtra("CAR_BRAND");
        String ownerName = getIntent().getStringExtra("OWNER_NAME");
        String phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        String carType = getIntent().getStringExtra("CARTYPE");
        String receiveDate = getIntent().getStringExtra("RECEIVE_DATE");
        int carImage = getIntent().getIntExtra("CAR_IMAGE", 0);

        TextView tvLicensePlate = findViewById(R.id.tvLicensePlate);
        EditText etOwnerName = findViewById(R.id.etOwnerName);
        EditText etPhoneNumber = findViewById(R.id.etPhoneNumber);

        tvLicensePlate.setVisibility(View.VISIBLE);
        tvLicensePlate.setText(licensePlate);
//        spinnerCarBrand.setSel
        etOwnerName.setText(ownerName);
        etPhoneNumber.setText(phoneNumber);


//        CAR BRAND SPINNER:
        Spinner spinnerCarBrand = findViewById(R.id.spinnerCarBrand);

        // Fake call API:
        List<String> carBrands = new ArrayList<>();
        carBrands.add("Honda");
        carBrands.add("Suzuki");
        carBrands.add("Aston Martin");

        CarBrandSpinnerAdapter carBrandSpinnerAdapter = new CarBrandSpinnerAdapter(this, R.layout.item_car_brand_selected, carBrands);
        spinnerCarBrand.setAdapter(carBrandSpinnerAdapter);
        spinnerCarBrand.setSelection(carBrands.indexOf(carBrand));
        spinnerCarBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(AddCarActivity.this, carBrandAdapter.getItem(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        CAR BRAND SPINNER:
        Spinner spinnerCarType = findViewById(R.id.spinnerCarType);

        // Fake call API:
        List<String> carTypes = new ArrayList<>();
        carTypes.add("Mini");
        carTypes.add("Sedan");
        carTypes.add("SUV");
        carTypes.add("Luxury");

        CarTypeSpinnerAdapter carTypeSpinnerAdapter = new CarTypeSpinnerAdapter(this, R.layout.item_car_type_selected, carTypes);
        spinnerCarType.setAdapter(carTypeSpinnerAdapter);
        spinnerCarType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(AddCarActivity.this, carBrandAdapter.getItem(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
        buttonEscape.setText("Quay Lại");
        buttonEscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button buttonUpdate = findViewById(R.id.footerButtonRight);
        buttonUpdate.setText("Cập Nhật");
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Do Logic Here......
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