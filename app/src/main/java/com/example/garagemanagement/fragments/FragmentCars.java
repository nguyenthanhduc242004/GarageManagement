package com.example.garagemanagement.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.R;
import com.example.garagemanagement.adapter.CarDetailAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCars#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCars extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<Car> cars;
    CarDetailAdapter carDetailAdapter;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCars() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCars newInstance(String param1, String param2) {
        FragmentCars fragment = new FragmentCars();
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
        View view = inflater.inflate(R.layout.fragment_cars, container, false);

        //        Fake call API
        Car car1 = new Car("Nguyễn Thành Đức Đức Đức Đức Đức Đức", "78SH-000128", "Honda", "TPHCM", "0123456789", new Date(), 0, 1);
        Car car2 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Aston Martin", "TPHCM", "0123456789", new Date(), 0, 1);
        Car car3 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Volkswagenasdasdasd asd", "TPHCM", "0123456789", new Date(), 0, 2);
        Car car4 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Honda", "TPHCM", "0123456789", new Date(), 0, 0);
        Car car5 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Honda", "TPHCM", "0123456789", new Date(), 0, 0);
        Car car6 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Honda", "TPHCM", "0123456789", new Date(), 0, 0);

        cars = List.of(car1, car2, car3, car4, car5, car6);


//        CarDetailRecyclerView
        carDetailAdapter = new CarDetailAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerViewCarDetails = view.findViewById(R.id.recyclerViewCarDetails);
        recyclerViewCarDetails.setLayoutManager(linearLayoutManager);
        recyclerViewCarDetails.setFocusable(false);
        carDetailAdapter.setData(cars);
        recyclerViewCarDetails.setAdapter(carDetailAdapter);


//        DividerItemDeconration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewCarDetails.getContext(), RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerViewCarDetails.addItemDecoration(dividerItemDecoration);

//        SearchView
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setIconified(false);

//        Has to fakeNotifyDataSetChanged() because the keyboard abnormally disappear after first notifyDataSetChange()
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                carDetailAdapter.fakeNotifyDataSetChanged();
            }
        }, 1);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        return view;
    }

    private void filterList(String text) {
        List<Car> filteredList = new ArrayList<>();
        for (Car car : cars) {
            if (car.getOwnerName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(car);
            } else if (car.getLicensePlate().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(car);
            } else if (car.getCarBrand().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(car);
            } else if (formatter.format(car.getReceiveDate()).contains(text.toLowerCase())) {
                filteredList.add(car);
            }
        }
        carDetailAdapter.setData(filteredList);
    }
}