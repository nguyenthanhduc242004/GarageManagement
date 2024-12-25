package com.example.garagemanagement;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.Objects.CarBrand;
import com.example.garagemanagement.Objects.CarService;
import com.example.garagemanagement.Objects.CarSupply;
import com.example.garagemanagement.adapter.AdapterViewPager;
import com.example.garagemanagement.adapter.CarSpinnerAdapter;
import com.example.garagemanagement.fragments.FragmentAccount;
import com.example.garagemanagement.fragments.FragmentCars;
import com.example.garagemanagement.fragments.FragmentHome;
import com.example.garagemanagement.fragments.FragmentPayment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    public static ProgressDialog mainProgressDialog;
    FragmentActivity _this = this;

    ViewPager2 pagerMain;
    public static BottomNavigationView bottomNav;

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    public static List<Car> cars = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        mainProgressDialog = new ProgressDialog(this);
        mainProgressDialog.setCancelable(false);
        mainProgressDialog.setMessage("Đang tải...");
//        mainProgressDialog.show();

        try {
            fetchDataFromMultipleCollections();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Deleting the weird margin bottom of the BottomNavigationView
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnApplyWindowInsetsListener((v, insets) ->
                insets
        );

//        BottomNav and Pager: BEGIN
        pagerMain = findViewById(R.id.pagerMain);

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new FragmentHome());
        fragmentArrayList.add(new FragmentCars());
        fragmentArrayList.add(new FragmentPayment());
        fragmentArrayList.add(new FragmentAccount());

        AdapterViewPager adapterViewPager = new AdapterViewPager(this, fragmentArrayList);
        // setAdapter
        pagerMain.setAdapter(adapterViewPager);
        pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    bottomNav.setSelectedItemId(R.id.itHome);
                } else if (position == 1) {
                    bottomNav.setSelectedItemId(R.id.itCars);
                } else if (position == 2) {
                    bottomNav.setSelectedItemId(R.id.itPayment);
                } else if (position == 3) {
                    bottomNav.setSelectedItemId(R.id.itAccount);
                }
                super.onPageSelected(position);
            }
        });

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.itHome) {
                    pagerMain.setCurrentItem(0);
                } else if (id == R.id.itCars) {
                    pagerMain.setCurrentItem(1);
                } else if (id == R.id.itPayment) {
                    pagerMain.setCurrentItem(2);
                } else if (id == R.id.itAccount) {
                    pagerMain.setCurrentItem(3);
                }
                return true;
            }
        });
//        BottomNav and Pager: END

//        Header buttons logic:
        ImageButton logoBtn = findViewById(R.id.logoBtn);
        logoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNav.setSelectedItemId(R.id.itHome);
            }
        });

        ImageButton notificationBtn = findViewById(R.id.notificationBtn);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to notification activity
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    public static List<Car> getCars() {
        return cars;
    }

    public static void setCars(List<Car> cars) {
        MainActivity.cars = cars;
    }

    public static Future<Void> fetchDataFromMultipleCollections() throws Exception {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Fetch data from both collections asynchronously
        Task<QuerySnapshot> collectionCarTask = db.collection("Car").get();
        Task<QuerySnapshot> collectionCarBrandTask = db.collection("CarBrand").get();
        Task<QuerySnapshot> collectionCarTypeTask = db.collection("CarType").get();
        Task<QuerySnapshot> collectionCarServiceTask = db.collection("CarService").get();
        Task<QuerySnapshot> collectionCarSupplyTask = db.collection("CarSupply").get();

        Tasks.whenAllSuccess(collectionCarTask, collectionCarBrandTask, collectionCarTypeTask, collectionCarServiceTask, collectionCarSupplyTask)
                .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {
                        try {
                            // Get the results
                            QuerySnapshot collectionCarSnapshot = Tasks.await(collectionCarTask);
                            QuerySnapshot collectionCarBrandSnapshot = Tasks.await(collectionCarBrandTask);
                            QuerySnapshot collectionCarTypeSnapshot = Tasks.await(collectionCarTypeTask);
                            QuerySnapshot collectionCarServiceSnapshot = Tasks.await(collectionCarServiceTask);
                            QuerySnapshot collectionCarSupplySnapshot = Tasks.await(collectionCarSupplyTask);

                            // Process data and populate spinnerItems
                            for (DocumentSnapshot documentCar : collectionCarSnapshot.getDocuments()) {
                                Car car = documentCar.toObject(Car.class);
                                car.setCarId(documentCar.getId());
                                int state = car.getState();

                                String carBrandId = car.getCarBrandId();
                                for (DocumentSnapshot documentCarBrand : collectionCarBrandSnapshot.getDocuments()) {
                                    if (carBrandId.equals(documentCarBrand.getId())) {
                                        car.setCarBrandText(documentCarBrand.getString("carBrandText"));
                                        break;
                                    }
                                }

                                String carTypeId = car.getCarTypeId();
                                for (DocumentSnapshot documentCarType : collectionCarTypeSnapshot.getDocuments()) {
                                    if (carTypeId.equals(documentCarType.getId())) {
                                        car.setCarTypeText(documentCarType.getString("carTypeText"));
                                        break;
                                    }
                                }

                                List<String> carServices = car.getCarServices();
                                List<CarService> carServiceList = new ArrayList<>();
                                for (DocumentSnapshot documentCarService : collectionCarServiceSnapshot.getDocuments()) {
                                    for (int i = 0; i < carServices.size(); i++) {
                                        String carServiceId = carServices.get(i);
                                        if (carServiceId.equals(documentCarService.getId())) {
                                            CarService carService = documentCarService.toObject(CarService.class);
                                            carService.setServiceId(carServiceId);
                                            carServiceList.add(carService);
                                        }
                                    }
                                }
                                car.setCarServiceList(carServiceList);

                                Map<String, Integer> carSupplies = car.getCarSupplies();
                                List<CarSupply> carSupplyList = new ArrayList<>();
                                for (DocumentSnapshot documentCarSupply : collectionCarSupplySnapshot.getDocuments()) {
                                    String carSupplyId = documentCarSupply.getId();
                                    int quantity = carSupplies.get(carSupplyId);
                                    CarSupply carSupply = documentCarSupply.toObject(CarSupply.class);
                                    carSupply.setQuantity(quantity);
                                    carSupply.setSupplyId(carSupplyId);
                                    carSupplyList.add(carSupply);
                                }
                                car.setCarSupplyList(carSupplyList);

                                cars.add(car);
                            }
                            if (mainProgressDialog.isShowing()) {
                                mainProgressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            // Handle exceptions (InterruptedException, ExecutionException)
                            if (mainProgressDialog.isShowing()) {
                                mainProgressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }
                });
        return null;
    }
}