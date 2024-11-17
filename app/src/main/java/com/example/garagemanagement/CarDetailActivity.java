package com.example.garagemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.garagemanagement.Objects.Car;

public class CarDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_car_detail);
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
        String address = getIntent().getStringExtra("ADDRESS");
        String receiveDate = getIntent().getStringExtra("RECEIVE_DATE");
        int carImage = getIntent().getIntExtra("CAR_IMAGE", 0);

        TextView tvOwnerName = findViewById(R.id.tvOwnerName);
        TextView tvLicensePlate = findViewById(R.id.tvLicensePlate);
        TextView tvCarBrand = findViewById(R.id.tvCarBrand);
        TextView tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        TextView tvAddress = findViewById(R.id.tvAddress);
        TextView tvReceiveDate = findViewById(R.id.tvReceiveDate);
        ImageView ivCarImage = findViewById(R.id.ivCarImage);

        ivCarImage.setImageResource(carImage);
        tvOwnerName.setText(ownerName);
        tvLicensePlate.setText(licensePlate);
        tvCarBrand.setText(carBrand);
        tvPhoneNumber.setText(phoneNumber);
        tvAddress.setText(address);
        tvReceiveDate.setText(receiveDate);
        if (carImage == 0) {
            ivCarImage.setImageResource(R.drawable.no_image);
        } else {
            ivCarImage.setImageResource(carImage);
        }

//        FOOTER BUTTONS:
        Button footerButtonRight = findViewById(R.id.footerButtonRight);
        Button footerButtonLeft = findViewById(R.id.footerButtonLeft);
        int state = getIntent().getIntExtra("STATE", -1);
        if (state == Car.STATE_NEW) {
            footerButtonRight.setText("Sửa Chữa");
            footerButtonLeft.setText("Xóa");
            footerButtonRight.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
            footerButtonLeft.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.red));
            footerButtonRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_home_repair_service_24, 0, 0, 0);
            footerButtonLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_delete_outline_24, 0, 0, 0);
        }
        else if (state == Car.STATE_REPAIRING) {
            footerButtonRight.setText("Sửa Xong");
            footerButtonLeft.setText("Sửa Thêm");
            footerButtonRight.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
            footerButtonLeft.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.yellow));
            footerButtonRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24, 0, 0, 0);
            footerButtonLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, 0, 0);
        }
        else if (state == Car.STATE_COMPLETED) {
            footerButtonRight.setText("Thanh Toán");
            footerButtonLeft.setText("Sửa Thêm");
            footerButtonRight.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
            footerButtonLeft.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.yellow));
            footerButtonRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_payment_24, 0, 0, 0);
            footerButtonLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, 0, 0);
        }


//        BUTTON UPDATE CAR:
        Button buttonUpdateCar = findViewById(R.id.buttonUpdateCar);
        buttonUpdateCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarDetailActivity.this, UpdateCarActivity.class);
                intent.putExtra("LICENSE_PLATE", licensePlate);
                intent.putExtra("CAR_BRAND", carBrand);
                intent.putExtra("OWNER_NAME", ownerName);
                intent.putExtra("PHONE_NUMBER", phoneNumber);
                intent.putExtra("ADDRESS", address);
                intent.putExtra("RECEIVE_DATE", receiveDate);
                intent.putExtra("CAR_IMAGE", carImage);
                intent.putExtra("STATE", state);

                startActivity(intent);
            }
        });
    }
}