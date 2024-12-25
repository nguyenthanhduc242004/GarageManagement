package com.example.garagemanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.adapter.ConfirmationDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewCarDetailActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public static String licensePlate;
    public static String carBrandId;
    public static String carBrandText;
    public static String carTypeId;
    public static String carTypeText;
    public static String ownerName;
    public static String phoneNumber;
    public static Date receiveDate;

    TextView tvLicensePlate;
    TextView tvOwnerName;
    TextView tvCarBrand;
    TextView tvPhoneNumber;
    TextView tvCarType;
    TextView tvReceiveDate;
    ImageView ivCarImage;

    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        tvLicensePlate.setText(licensePlate);
        tvCarBrand.setText(carBrandText);
        tvCarType.setText(carTypeText);
        tvOwnerName.setText(ownerName);
        tvPhoneNumber.setText(phoneNumber);
        tvReceiveDate.setText(formatter.format(receiveDate));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_car_detail);
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

        tvLicensePlate = findViewById(R.id.tvLicensePlate);
        tvOwnerName = findViewById(R.id.tvOwnerName);
        tvCarBrand = findViewById(R.id.tvCarBrand);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvCarType = findViewById(R.id.tvCarType);
        tvReceiveDate = findViewById(R.id.tvReceiveDate);
        ivCarImage = findViewById(R.id.ivCarImage);

//        BASIC CAR INFORMATION:
        String carId = getIntent().getStringExtra("CAR_ID");
        licensePlate = getIntent().getStringExtra("LICENSE_PLATE");
        carBrandId = getIntent().getStringExtra("CAR_BRAND_ID");
        carBrandText = getIntent().getStringExtra("CAR_BRAND_TEXT");
        carTypeId = getIntent().getStringExtra("CAR_TYPE_ID");
        carTypeText = getIntent().getStringExtra("CAR_TYPE_TEXT");
        ownerName = getIntent().getStringExtra("OWNER_NAME");
        phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        receiveDate = (Date) getIntent().getSerializableExtra("RECEIVE_DATE");
        int carImage = getIntent().getIntExtra("CAR_IMAGE", 0);
        int state = getIntent().getIntExtra("STATE", 0);

        tvLicensePlate.setText(licensePlate);
        tvCarBrand.setText(carBrandText);
        tvCarType.setText(carTypeText);
        tvOwnerName.setText(ownerName);
        tvPhoneNumber.setText(phoneNumber);
        tvReceiveDate.setText(formatter.format(receiveDate));
        if (carImage == 0) {
            ivCarImage.setImageResource(R.drawable.no_image);
        } else {
            ivCarImage.setImageResource(carImage);
        }


//        BUTTON UPDATE CAR:
        Button buttonUpdateCar = findViewById(R.id.buttonUpdateCar);
        buttonUpdateCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewCarDetailActivity.this, UpdateCarActivity.class);
                intent.putExtra("CAR_ID", carId);
                intent.putExtra("LICENSE_PLATE", licensePlate);
                intent.putExtra("CAR_BRAND_ID", carBrandId);
                intent.putExtra("CAR_BRAND_TEXT", carBrandText);
                intent.putExtra("CAR_TYPE_ID", carTypeId);
                intent.putExtra("CAR_TYPE_TEXT", carTypeText);
                intent.putExtra("OWNER_NAME", ownerName);
                intent.putExtra("PHONE_NUMBER", phoneNumber);
                intent.putExtra("RECEIVE_DATE", receiveDate);
                intent.putExtra("CAR_IMAGE", carImage);
                intent.putExtra("STATE", state);
                startActivity(intent);
            }
        });

        Button buttonDeleteCar = findViewById(R.id.footerButtonLeft);
        buttonDeleteCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmationDialog.showConfirmationDialog(NewCarDetailActivity.this, "Xác nhận xóa!",
                        "Bạn sẽ không thể hoàn tác sau khi thực hiện!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.collection("Car")
                                        .document(carId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(), "Xóa xe thành công!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Xóa xe không thành công!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
            }
        });

        Button buttonRepairCar = findViewById(R.id.footerButtonRight);
        buttonRepairCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewCarDetailActivity.this, AddRepairCardActivity.class);
                intent.putExtra("CAR_ID", carId);
                startActivity(intent);
            }
        });
    }
}