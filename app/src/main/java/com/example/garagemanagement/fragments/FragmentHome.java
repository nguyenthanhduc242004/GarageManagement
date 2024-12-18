package com.example.garagemanagement.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.garagemanagement.AddCarActivity;
import com.example.garagemanagement.AddRepairCardActivity;
import com.example.garagemanagement.DateDeserializer;
import com.example.garagemanagement.MainActivity;
import com.example.garagemanagement.NewCarDetailActivity;
import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.R;
import com.example.garagemanagement.RepairingCarDetailActivity;
import com.example.garagemanagement.adapter.CarAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
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

    public static int selectedState = -1;

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


        String json = "[\n" +
                "  {\n" +
                "    \"carId\": 1,\n" +
                "    \"licensePlate\": \"29A-123.45\",\n" +
                "    \"ownerName\": \"Nguyễn Thị Linh\",\n" +
                "    \"carBrandId\": 1,\n" +
                "    \"carBrandText\": \"Honda\",\n" +
                "    \"carTypeId\": 1,\n" +
                "    \"carTypeText\": \"Mini\",\n" +
                "    \"phoneNumber\": \"0901234567\",\n" +
                "    \"receiveDate\": \"2024, 02, 14\",\n" +
                "    \"carImage\": 0,\n" +
                "    \"state\": 0,\n" +
                "    \"carServices\": [],\n" +
                "    \"carSupplies\": []\n" +
                "  },\n" +
                "  {\n" +
                "    \"carId\": 2,\n" +
                "    \"licensePlate\": \"51F-987.65\",\n" +
                "    \"ownerName\": \"Trần Minh Phúc\",\n" +
                "    \"carBrandId\": 2,\n" +
                "    \"carBrandText\": \"Aston Martin\",\n" +
                "    \"carTypeId\": 2,\n" +
                "    \"carTypeText\": \"Sedan\",\n" +
                "    \"phoneNumber\": \"0987654321\",\n" +
                "    \"receiveDate\": \"2024, 04, 10\",\n" +
                "    \"carImage\": 0,\n" +
                "    \"state\": 1,\n" +
                "    \"carServices\": [\n" +
                "      {\"serviceId\": \"3\", \"serviceName\": \"BẢO DƯỠNG CẤP TRUNG BÌNH LỚN (20.000) KM\", \"price\": 599000},\n" +
                "      {\"serviceId\": \"6\", \"serviceName\": \"Vệ sinh kim phun (bao gồm dung dịch kèm theo)\", \"price\": 660000}\n" +
                "    ],\n" +
                "    \"carSupplies\": [\n" +
                "      {\"supplyId\": \"7\", \"supplyName\": \"Gạt mưa Bosch mềm\", \"price\": 600000, \"quantity\": 7},\n" +
                "      {\"supplyId\": \"5\", \"supplyName\": \"Còi Denso\", \"price\": 500000, \"quantity\": 3}\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"carId\": 3,\n" +
                "    \"licensePlate\": \"30H-456.78\",\n" +
                "    \"ownerName\": \"Lê Hương Giang\",\n" +
                "    \"carBrandId\": 3,\n" +
                "    \"carBrandText\": \"Suzuki\",\n" +
                "    \"carTypeId\": 3,\n" +
                "    \"carTypeText\": \"SUV\",\n" +
                "    \"phoneNumber\": \"0912345678\",\n" +
                "    \"receiveDate\": \"2024, 06, 18\",\n" +
                "    \"carImage\": 0,\n" +
                "    \"state\": 2,\n" +
                "    \"carServices\": [\n" +
                "      {\"serviceId\": \"1\", \"serviceName\": \"BẢO DƯỠNG CẤP NHỎ (5000) KM\", \"price\": 199000},\n" +
                "      {\"serviceId\": \"11\", \"serviceName\": \"Cân bằng động (100k/bánh)\", \"price\": 400000}\n" +
                "    ],\n" +
                "    \"carSupplies\": [\n" +
                "      {\"supplyId\": \"1\", \"supplyName\": \"Dung dịch phụ gia súc béc xăng (Wurth)\", \"price\": 300000, \"quantity\": 2},\n" +
                "      {\"supplyId\": \"4\", \"supplyName\": \"Nước làm mát (Asin, Jinco)\", \"price\": 150000, \"quantity\": 5}\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"carId\": 4,\n" +
                "    \"licensePlate\": \"79D-012.34\",\n" +
                "    \"ownerName\": \"Phạm Quốc Anh\",\n" +
                "    \"carBrandId\": 4,\n" +
                "    \"carBrandText\": \"Vinfast\",\n" +
                "    \"carTypeId\": 4,\n" +
                "    \"carTypeText\": \"Luxury\",\n" +
                "    \"phoneNumber\": \"0976543210\",\n" +
                "    \"receiveDate\": \"2024, 08, 25\",\n" +
                "    \"carImage\": 0,\n" +
                "    \"state\": 3,\n" +
                "    \"carServices\": [\n" +
                "      {\"serviceId\": \"9\", \"serviceName\": \"Kiểm tra hệ thống điện chuyên sâu\", \"price\": 1200000},\n" +
                "      {\"serviceId\": \"12\", \"serviceName\": \"Cân chỉnh độ chụm\", \"price\": 800000}\n" +
                "    ],\n" +
                "    \"carSupplies\": [\n" +
                "      {\"supplyId\": \"2\", \"supplyName\": \"Dung dịch hụ gia súc nhớt (Wurth)\", \"price\": 300000, \"quantity\": 3},\n" +
                "      {\"supplyId\": \"6\", \"supplyName\": \"Gạt mưa Bosch cứng\", \"price\": 350000, \"quantity\": 2}\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"carId\": 5,\n" +
                "    \"licensePlate\": \"60C-876.54\",\n" +
                "    \"ownerName\": \"Hoàng Thu Trang\",\n" +
                "    \"carBrandId\": 2,\n" +
                "    \"carBrandText\": \"Aston Martin\",\n" +
                "    \"carTypeId\": 4,\n" +
                "    \"carTypeText\": \"Luxury\",\n" +
                "    \"phoneNumber\": \"0961112222\",\n" +
                "    \"receiveDate\": \"2024, 10, 05\",\n" +
                "    \"carImage\": 0,\n" +
                "    \"state\": 0,\n" +
                "    \"carServices\": [],\n" +
                "    \"carSupplies\": []\n" +
                "  },\n" +
                "  {\n" +
                "    \"carId\": 6,\n" +
                "    \"licensePlate\": \"15B-789.01\",\n" +
                "    \"ownerName\": \"Đỗ Văn Minh\",\n" +
                "    \"carBrandId\": 4,\n" +
                "    \"carBrandText\": \"Vinfast\",\n" +
                "    \"carTypeId\": 4,\n" +
                "    \"carTypeText\": \"Luxury\",\n" +
                "    \"phoneNumber\": \"0345967735\",\n" +
                "    \"receiveDate\": \"2024, 11, 30\",\n" +
                "    \"carImage\": 0,\n" +
                "    \"state\": 1,\n" +
                "    \"carServices\": [\n" +
                "      {\"serviceId\": \"9\", \"serviceName\": \"Kiểm tra hệ thống điện chuyên sâu\", \"price\": 1200000},\n" +
                "      {\"serviceId\": \"12\", \"serviceName\": \"Cân chỉnh độ chụm\", \"price\": 800000}\n" +
                "    ],\n" +
                "    \"carSupplies\": [\n" +
                "      {\"supplyId\": \"2\", \"supplyName\": \"Dung dịch hụ gia súc nhớt (Wurth)\", \"price\": 300000, \"quantity\": 3},\n" +
                "      {\"supplyId\": \"6\", \"supplyName\": \"Gạt mưa Bosch cứng\", \"price\": 350000, \"quantity\": 2}\n" +
                "    ]\n" +
                "  }\n" +
                "]";

//        Converting json into List<Car>
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
        List<Car> cars = gson.fromJson(json, new TypeToken<List<Car>>() {}.getType());
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

        Button tvNewCars = view.findViewById(R.id.tvNewCars);
        tvNewCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragmentCars(0);
            }
        });
        Button btnShowAllNewCars = view.findViewById(R.id.btnShowAllNewCars);
        btnShowAllNewCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragmentCars(0);
            }
        });
        Button tvRepairingCars = view.findViewById(R.id.tvRepairingCars);
        tvRepairingCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragmentCars(1);
            }
        });
        Button buttonShowAllRepairingCars = view.findViewById(R.id.btnShowAllRepairingCars);
        buttonShowAllRepairingCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragmentCars(1);
            }
        });
        Button tvCompletedCars = view.findViewById(R.id.tvCompletedCars);
        tvCompletedCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragmentCars(2);
            }
        });
        Button buttonShowAllCompletedCars = view.findViewById(R.id.btnShowAllCompletedCars);
        buttonShowAllCompletedCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragmentCars(2);
            }
        });

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
            intent.putExtra("CAR_BRAND_ID", newCars.get(position).getCarBrandId());
            intent.putExtra("CAR_BRAND_TEXT", newCars.get(position).getCarBrandText());
            intent.putExtra("CAR_TYPE_ID", newCars.get(position).getCarTypeId());
            intent.putExtra("CAR_TYPE_TEXT", newCars.get(position).getCarTypeText());
            intent.putExtra("OWNER_NAME", newCars.get(position).getOwnerName());
            intent.putExtra("PHONE_NUMBER", newCars.get(position).getPhoneNumber());
            intent.putExtra("RECEIVE_DATE", formatter.format(newCars.get(position).getReceiveDate()));
            intent.putExtra("CAR_IMAGE", newCars.get(position).getCarImage());
            intent.putExtra("STATE", state);
            startActivity(intent);
        }
        else if (state == 1) {
            Intent intent = new Intent(getContext(), RepairingCarDetailActivity.class);
            intent.putExtra("LICENSE_PLATE", repairingCars.get(position).getLicensePlate());
            intent.putExtra("CAR_BRAND_ID", repairingCars.get(position).getCarBrandId());
            intent.putExtra("CAR_BRAND_TEXT", repairingCars.get(position).getCarBrandText());
            intent.putExtra("CAR_TYPE_ID", repairingCars.get(position).getCarTypeId());
            intent.putExtra("CAR_TYPE_TEXT", repairingCars.get(position).getCarTypeText());
            intent.putExtra("OWNER_NAME", repairingCars.get(position).getOwnerName());
            intent.putExtra("PHONE_NUMBER", repairingCars.get(position).getPhoneNumber());
            intent.putExtra("RECEIVE_DATE", formatter.format(repairingCars.get(position).getReceiveDate()));
            intent.putExtra("CAR_IMAGE", repairingCars.get(position).getCarImage());
            intent.putExtra("STATE", state);
            intent.putExtra("CAR_SERVICES", (Serializable) repairingCars.get(position).getCarServices());
            intent.putExtra("CAR_SUPPLIES", (Serializable) repairingCars.get(position).getCarSupplies());
            startActivity(intent);
        }
    }

    public void openFragmentCars(int state) {
        FragmentHome.selectedState = state;
        MainActivity.bottomNav.setSelectedItemId(R.id.itCars);
    }
}