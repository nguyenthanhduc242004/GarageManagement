package com.example.garagemanagement.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.garagemanagement.NewCarDetailActivity;
import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.R;
import com.example.garagemanagement.adapter.CarAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCars#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCars extends Fragment implements RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<Car> cars;
    CarAdapter carAdapter;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Button btnCarState;

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
        Car car1 = new Car("Nguyễn Thành Đức Đức Đức Đức Đức Đức", "78SH-000128", "Honda", "Mini", "0123456789", new Date(), 0, 1);
        Car car2 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Aston Martin", "Sedan", "0123456789", new Date(), 0, 1);
        Car car3 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Volkswagenasdasdasd asd", "TPHCM", "0123456789", new Date(), 0, 2);
        Car car4 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Honda", "SUV", "0123456789", new Date(), 0, 0);
        Car car5 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Honda", "Sedan", "0123456789", new Date(), 0, 0);
        Car car6 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Honda", "Mini", "0123456789", new Date(), 0, 0);
        Car car7 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Honda", "SUV", "0123456789", new Date(), 0, 0);
        Car car8 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Honda", "Sedan", "0123456789", new Date(), 0, 0);
        Car car9 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Honda", "Sedan", "0123456789", new Date(), 0, 0);
        Car car10 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Honda", "Sedan", "0123456789", new Date(), 0, 0);
        Car car11 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Honda", "SUV", "0123456789", new Date(), 0, 0);
        Car car12 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Honda", "Luxury", "0123456789", new Date(), 0, 0);
        Car car13 = new Car("Nguyễn Thành Đức", "78SH-001.28", "Honda", "Mni", "0123456789", new Date(), 0, 0);

        cars = List.of(car1, car2, car3, car4, car5, car6, car7, car8, car9, car10, car11, car12, car13);


//        CarDetailRecyclerView
        carAdapter = new CarAdapter(getContext(),CarAdapter.TYPE_CAR_LIST, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerViewCarList = view.findViewById(R.id.recyclerViewCarList);
        recyclerViewCarList.setLayoutManager(linearLayoutManager);
        recyclerViewCarList.setFocusable(false);
        carAdapter.setData(cars);
        recyclerViewCarList.setAdapter(carAdapter);


//        DividerItemDecoration
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewCarList.getContext(), RecyclerView.VERTICAL);
//        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
//        recyclerViewCarList.addItemDecoration(dividerItemDecoration);

//        SearchView
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setIconified(false);

//        Has to fake notifyDataSetChanged() because the keyboard abnormally disappear after first notifyDataSetChange()
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                carAdapter.notifyDataSetChanged();
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

        btnCarState = view.findViewById(R.id.btnCarState);
        registerForContextMenu(btnCarState);
        btnCarState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().openContextMenu(btnCarState);
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
        carAdapter.setData(filteredList);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), NewCarDetailActivity.class);

        intent.putExtra("LICENSE_PLATE", cars.get(position).getLicensePlate());
        intent.putExtra("CAR_BRAND", cars.get(position).getCarBrand());
        intent.putExtra("OWNER_NAME", cars.get(position).getOwnerName());
        intent.putExtra("PHONE_NUMBER", cars.get(position).getPhoneNumber());
        intent.putExtra("CAR_TYPE", cars.get(position).getCarType());
        intent.putExtra("RECEIVE_DATE", formatter.format(cars.get(position).getReceiveDate()));
        intent.putExtra("CAR_IMAGE", cars.get(position).getCarImage());
        intent.putExtra("STATE", cars.get(position).getState());

        startActivity(intent);
    }

    @Override
    public void onItemClick(int position, int state) {

    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.btnCarState) {
            getActivity().getMenuInflater().inflate(R.menu.menu_car_state, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.optionAll) {
            btnCarState.setText("Tất cả");
            btnCarState.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
        }
        else if (itemId == R.id.optionNew) {
            btnCarState.setText("Mới tiếp nhận");
            btnCarState.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        } else if (itemId == R.id.optionRepairing) {
            btnCarState.setText("Đang sửa");
            btnCarState.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow));
        } else if (itemId == R.id.optionCompleted) {
            btnCarState.setText("Mới hoàn thành");
            btnCarState.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
        }
        return super.onContextItemSelected(item);
    }
}