package com.example.garagemanagement;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.garagemanagement.adapter.AdapterViewPager;
import com.example.garagemanagement.fragments.FragmentCars;
import com.example.garagemanagement.fragments.FragmentHome;
import com.example.garagemanagement.fragments.FragmentPayment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    ActivityMainBinding binding;

    ViewPager2 pagerMain;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    BottomNavigationView bottomNav;

    @SuppressLint("NonConstantResourceId")
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

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnApplyWindowInsetsListener((v, insets) ->
            insets
        );

        pagerMain = findViewById(R.id.pagerMain);

        fragmentArrayList.add(new FragmentHome());
        fragmentArrayList.add(new FragmentCars());
        fragmentArrayList.add(new FragmentPayment());

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
                }
                super.onPageSelected(position);
            }
        });



//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        replaceFragment(new HomeFragment());

//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//
//            if (itemId == R.id.home) {
//                replaceFragment(new HomeFragment());
//            } else if (itemId == R.id.cars) {
//                replaceFragment(new CarsFragment());
//            } else if (itemId == R.id.payments) {
//                replaceFragment(new PaymentFragment());
//            }
//
//            return true;
//        });
    }

//    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayout, fragment);
//        fragmentTransaction.commit();
//    }
}