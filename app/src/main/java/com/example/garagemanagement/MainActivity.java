package com.example.garagemanagement;

import android.os.Bundle;
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
import androidx.viewpager2.widget.ViewPager2;

import com.example.garagemanagement.adapter.AdapterViewPager;
import com.example.garagemanagement.fragments.FragmentAccount;
import com.example.garagemanagement.fragments.FragmentCars;
import com.example.garagemanagement.fragments.FragmentHome;
import com.example.garagemanagement.fragments.FragmentPayment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager2 pagerMain;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    public static BottomNavigationView bottomNav;

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

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


        // Deleting the weird margin bottom of the BottomNavigationView
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnApplyWindowInsetsListener((v, insets) ->
                insets
        );

//        BottomNav and Pager: BEGIN
        pagerMain = findViewById(R.id.pagerMain);

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
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
}