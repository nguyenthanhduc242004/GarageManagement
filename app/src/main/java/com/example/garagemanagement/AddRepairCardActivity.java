package com.example.garagemanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.adapter.CarBrandSpinnerAdapter;
import com.example.garagemanagement.adapter.CarSpinnerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddRepairCardActivity extends AppCompatActivity {

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

        //        CAR BRAND SPINNER:
        Spinner spinner = findViewById(R.id.spinner);

        // Fake call API :
        List<Car> unpaidCars = new ArrayList<>();
        unpaidCars.add(new Car("Nguyễn Thành Đức", "78SH-000128", "Honda", "TPHCM", "0123456789", new Date(), 0, 0));
        unpaidCars.add(new Car("Nguyễn Thành Đức Đức ĐứcĐức ĐứcĐức ĐứcĐức ĐứcĐức ĐứcĐức", "78SH-000128", "Honda", "TPHCM", "0123456789", new Date(), 0, 1));
        unpaidCars.add(new Car("Nguyễn Thành Đức", "78SH-000128", "Honda", "TPHCM", "0123456789", new Date(), 0, 2));
        unpaidCars.add(new Car("Nguyễn Thành Đức", "78SH-000128", "Honda", "TPHCM", "0123456789", new Date(), 0, 3));

        CarSpinnerAdapter carSpinnerAdapter = new CarSpinnerAdapter(this, R.layout.item_car_brand_selected, unpaidCars);
        spinner.setAdapter(carSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(AddCarActivity.this, carBrandAdapter.getItem(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}