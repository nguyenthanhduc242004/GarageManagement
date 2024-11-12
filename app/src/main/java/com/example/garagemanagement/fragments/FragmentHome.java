package com.example.garagemanagement.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.R;
import com.example.garagemanagement.adapter.CarAdapter;

import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

//        Fake call API
        Car car1 = new Car("Nguyễn Thành Đức Đức Đức Đức Đức Đức", "78SH-000128", "Honda", "TPHCM", "0123456789", new Date(), 0, 1);
        Car car2 = new Car("Nguyễn Thành Đức", "78SH-000128", "Honda", "TPHCM", "0123456789", new Date(), 0, 1);
        Car car3 = new Car("Nguyễn Thành Đức", "78SH-000128", "Honda", "TPHCM", "0123456789", new Date(), 0, 2);
        Car car4 = new Car("Nguyễn Thành Đức", "78SH-000128", "Honda", "TPHCM", "0123456789", new Date(), 0, 0);
        Car car5 = new Car("Nguyễn Thành Đức", "78SH-000128", "Honda", "TPHCM", "0123456789", new Date(), 0, 0);
        Car car6 = new Car("Nguyễn Thành Đức", "78SH-000128", "Honda", "TPHCM", "0123456789", new Date(), 0, 0);
        List<Car> cars = List.of(car1, car2, car3, car4, car5, car6);


        CarAdapter newCarsAdapter = new CarAdapter();
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), 2);
        RecyclerView recyclerViewNewCars = view.findViewById(R.id.recyclerViewNewCars);
        recyclerViewNewCars.setLayoutManager(gridLayoutManager1);
        recyclerViewNewCars.setFocusable(false);
        recyclerViewNewCars.setNestedScrollingEnabled(false);
        newCarsAdapter.setData(cars);
        recyclerViewNewCars.setAdapter(newCarsAdapter);

        CarAdapter repairingCarsAdapter = new CarAdapter();
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 2);
        RecyclerView recyclerViewRepairingCars = view.findViewById(R.id.recyclerViewRepairingCars);
        recyclerViewRepairingCars.setLayoutManager(gridLayoutManager2);
        recyclerViewRepairingCars.setFocusable(false);
        recyclerViewRepairingCars.setNestedScrollingEnabled(false);
        repairingCarsAdapter.setData(cars);
        recyclerViewRepairingCars.setAdapter(repairingCarsAdapter);

        CarAdapter completedCarsAdapter = new CarAdapter();
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getContext(), 2);
        RecyclerView recyclerViewCompletedCars = view.findViewById(R.id.recyclerViewCompletedCars);
        recyclerViewCompletedCars.setLayoutManager(gridLayoutManager3);
        recyclerViewCompletedCars.setFocusable(false);
        recyclerViewCompletedCars.setNestedScrollingEnabled(false);
        completedCarsAdapter.setData(cars);
        recyclerViewCompletedCars.setAdapter(completedCarsAdapter);

        return view;
    }
}