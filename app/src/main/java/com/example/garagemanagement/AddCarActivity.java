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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.garagemanagement.Objects.CarBrand;
import com.example.garagemanagement.Objects.CarType;
import com.example.garagemanagement.adapter.CarBrandSpinnerAdapter;
import com.example.garagemanagement.adapter.CarTypeSpinnerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Calendar;
import java.util.List;

public class AddCarActivity extends AppCompatActivity {
    ImageView ivCarImage;
    DatePickerDialog datePickerDialog;
    Button buttonDate;
    Button buttonOpenCamera;
    Button buttonDeleteImage;

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


//        CAR BRAND SPINNER:
        Spinner spinnerCarBrand = findViewById(R.id.spinnerCarBrand);
        // CALL API CarBrand:
        String json = "[\n" +
                "        {\"carBrandId\": 1, \"carBrandText\": \"Honda\"},\n" +
                "        {\"carBrandId\": 2, \"carBrandText\": \"Aston Martin\"},\n" +
                "        {\"carBrandId\": 3, \"carBrandText\": \"Suzuki\"},\n" +
                "        {\"carBrandId\": 4, \"carBrandText\": \"Vinfast\"}\n" +
                "]";
        Gson gson = new GsonBuilder().create();
        List<CarBrand> carBrands = gson.fromJson(json, new TypeToken<List<CarBrand>>() {}.getType());

        CarBrandSpinnerAdapter carBrandSpinnerAdapter = new CarBrandSpinnerAdapter(this, R.layout.item_car_brand_selected, carBrands);
        spinnerCarBrand.setAdapter(carBrandSpinnerAdapter);
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

        Button buttonAdd = findViewById(R.id.footerButtonRight);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Do Logic Here......
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


}