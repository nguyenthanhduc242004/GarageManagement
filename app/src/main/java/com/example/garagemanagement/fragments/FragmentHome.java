package com.example.garagemanagement.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.garagemanagement.AddCarActivity;
import com.example.garagemanagement.AddRepairCardActivity;
import com.example.garagemanagement.NewCarDetailActivity;
import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.R;
import com.example.garagemanagement.RepairingCarDetailActivity;
import com.example.garagemanagement.adapter.CarAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment implements RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    List<Car> cars;
    List<Car> newCars = new ArrayList<>();
    List<Car> repairingCars = new ArrayList<>();
    List<Car> completedCars = new ArrayList<>();

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton imageButtonAddCar = view.findViewById(R.id.imageButtonAddCar);
        imageButtonAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddCarActivity.class);
                startActivity(intent);
            }
        });

        ImageButton imageButtonAddRepairCard = view.findViewById(R.id.imageButtonAddRepairCard);
        imageButtonAddRepairCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddRepairCardActivity.class);
                startActivity(intent);
            }
        });



//        Fake call API
        Car car1 = new Car("Nguyễn Thành Đức Đức Đức Đức Đức Đức", "78SH-000128", "Honda", "Mini", "0123456789", new Date(), 0, 1);
        Car car2 = new Car("Nguyễn Thành Đức", "78SH-000128", "Honda", "Sedan", "0123456789", new Date(), 0, 1);
        Car car3 = new Car("Nguyễn Thành Đức", "78SH-000128", "Honda", "SUV", "0123456789", new Date(), 0, 2);
        Car car4 = new Car("Nguyễn Thành Đức", "78SH-000128", "Honda", "Luxury", "0123456789", new Date(), 0, 0);
        Car car5 = new Car("Nguyễn Thành Đức", "78SH-000128", "Honda", "Luxury", "0123456789", new Date(), 0, 1);
        Car car6 = new Car("Nguyễn Thành Đức", "78SH-000128", "Honda", "Sedan", "0123456789", new Date(), 0, 0);
        cars = List.of(car1, car2, car3, car4, car5, car6);

        newCars = new ArrayList<>();
        repairingCars = new ArrayList<>();
        completedCars = new ArrayList<>();

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            int state = car.getState();
            if (state == 0) {
                newCars.add(car);
            } else if (state == 1) {
                repairingCars.add(car);
            } else if (state == 2) {
                completedCars.add(car);
            }
        }

//        New Car RecyclerView:
        CarAdapter newCarsAdapter = new CarAdapter(getContext(), CarAdapter.TYPE_CAR_HOME, this);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), 2);
        RecyclerView recyclerViewNewCars = view.findViewById(R.id.recyclerViewNewCars);
        recyclerViewNewCars.setLayoutManager(gridLayoutManager1);
        recyclerViewNewCars.setFocusable(false);
        recyclerViewNewCars.setNestedScrollingEnabled(false);
        newCarsAdapter.setData(newCars);
        recyclerViewNewCars.setAdapter(newCarsAdapter);

//        Repairing Car RecyclerView:
        CarAdapter repairingCarsAdapter = new CarAdapter(getContext(), CarAdapter.TYPE_CAR_HOME, this);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 2);
        RecyclerView recyclerViewRepairingCars = view.findViewById(R.id.recyclerViewRepairingCars);
        recyclerViewRepairingCars.setLayoutManager(gridLayoutManager2);
        recyclerViewRepairingCars.setFocusable(false);
        recyclerViewRepairingCars.setNestedScrollingEnabled(false);
        repairingCarsAdapter.setData(repairingCars);
        recyclerViewRepairingCars.setAdapter(repairingCarsAdapter);

//        Completed Car RecyclerView:
        CarAdapter completedCarsAdapter = new CarAdapter(getContext(), CarAdapter.TYPE_CAR_HOME, this);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getContext(), 2);
        RecyclerView recyclerViewCompletedCars = view.findViewById(R.id.recyclerViewCompletedCars);
        recyclerViewCompletedCars.setLayoutManager(gridLayoutManager3);
        recyclerViewCompletedCars.setFocusable(false);
        recyclerViewCompletedCars.setNestedScrollingEnabled(false);
        completedCarsAdapter.setData(completedCars);
        recyclerViewCompletedCars.setAdapter(completedCarsAdapter);

        return view;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(int position, int state) {
        if (state == 0) {
            Intent intent = new Intent(getContext(), NewCarDetailActivity.class);
            intent.putExtra("LICENSE_PLATE", newCars.get(position).getLicensePlate());
            intent.putExtra("CAR_BRAND", newCars.get(position).getCarBrand());
            intent.putExtra("OWNER_NAME", newCars.get(position).getOwnerName());
            intent.putExtra("PHONE_NUMBER", newCars.get(position).getPhoneNumber());
            intent.putExtra("CAR_TYPE", newCars.get(position).getCarType());
            intent.putExtra("RECEIVE_DATE", formatter.format(newCars.get(position).getReceiveDate()));
            intent.putExtra("CAR_IMAGE", newCars.get(position).getCarImage());
            intent.putExtra("STATE", newCars.get(position).getState());
            startActivity(intent);
        }
        else if (state == 1) {
            Intent intent = new Intent(getContext(), RepairingCarDetailActivity.class);
            intent.putExtra("LICENSE_PLATE", repairingCars.get(position).getLicensePlate());
            intent.putExtra("CAR_BRAND", repairingCars.get(position).getCarBrand());
            intent.putExtra("OWNER_NAME", repairingCars.get(position).getOwnerName());
            intent.putExtra("PHONE_NUMBER", repairingCars.get(position).getPhoneNumber());
            intent.putExtra("CARTYPE", repairingCars.get(position).getCarType());
            intent.putExtra("RECEIVE_DATE", formatter.format(repairingCars.get(position).getReceiveDate()));
            intent.putExtra("CAR_IMAGE", repairingCars.get(position).getCarImage());
            intent.putExtra("STATE", repairingCars.get(position).getState());
            intent.putExtra("CAR_SERVICES", repairingCars.get(position).getCarServices());
            intent.putExtra("CAR_SUPPLIES", repairingCars.get(position).getCarSupplies());
            startActivity(intent);
        }


    }
}