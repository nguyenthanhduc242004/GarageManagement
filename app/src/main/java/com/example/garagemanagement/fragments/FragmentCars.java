package com.example.garagemanagement.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.garagemanagement.DateDeserializer;
import com.example.garagemanagement.NewCarDetailActivity;
import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.R;
import com.example.garagemanagement.RepairingCarDetailActivity;
import com.example.garagemanagement.adapter.CarAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

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
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    Button btnCarState;

    List<Car> unpaidCars = new ArrayList<>();
    List<Car> newCars = new ArrayList<>();
    List<Car> repairingCars = new ArrayList<>();
    List<Car> completedCars = new ArrayList<>();
    List<Car> filteredList = new ArrayList<>();
    int selectedState = -1;

    SearchView searchView;

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

//        SEARCH VIEW
        searchView = view.findViewById(R.id.searchView);
        searchView.setIconified(false);
        EditText txtSearch = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setIconified(false);
                return true;
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
        unpaidCars = new ArrayList<>();

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            if (car.getState() == 0) {
                newCars.add(car);
                unpaidCars.add(car);
            } else if (car.getState() == 1) {
                repairingCars.add(car);
                unpaidCars.add(car);
            } else if (car.getState() == 2) {
                completedCars.add(car);
                unpaidCars.add(car);
            }
        }

//        int fragmentHomeState = FragmentHome.selectedState;
//        Log.i("fragmentHomeStateUpper", String.valueOf(fragmentHomeState));
//        if (fragmentHomeState == -1) {
//            filteredList = unpaidCars;
//        } else if (fragmentHomeState == 0) {
//            filteredList = newCars;
//        } else if (fragmentHomeState == 1) {
//            filteredList = repairingCars;
//        } else if (fragmentHomeState == 2) {
//            filteredList = completedCars;
//        }

//        CarDetailRecyclerView
        carAdapter = new CarAdapter(getContext(),CarAdapter.TYPE_CAR_LIST, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerViewCarList = view.findViewById(R.id.recyclerViewCarList);
        recyclerViewCarList.setLayoutManager(linearLayoutManager);
        recyclerViewCarList.setFocusable(false);
        Log.i("FragmentHome.selectedState", String.valueOf(FragmentHome.selectedState));
        if (FragmentHome.selectedState == -1) {
            carAdapter.setData(filteredList);
        }
        recyclerViewCarList.setAdapter(carAdapter);


//        DividerItemDecoration
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewCarList.getContext(), RecyclerView.VERTICAL);
//        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
//        recyclerViewCarList.addItemDecoration(dividerItemDecoration);


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

    @Override
    public void onResume() {
        super.onResume();
        selectedState = FragmentHome.selectedState;
        int fragmentHomeState = FragmentHome.selectedState;
        Log.i("fragmentHomeState", String.valueOf(fragmentHomeState));
        if (fragmentHomeState == -1) {
            filteredList = unpaidCars;
            searchView.setQuery("", false);
        } else if (fragmentHomeState == 0) {
            filteredList = newCars;
            searchView.setQuery("", false);
        } else if (fragmentHomeState == 1) {
            filteredList = repairingCars;
            searchView.setQuery("", false);
        } else if (fragmentHomeState == 2) {
            filteredList = completedCars;
            searchView.setQuery("", false);
        }
        carAdapter.setData(filteredList);
        if (fragmentHomeState == -1) {
            btnCarState.setText("Chọn tình trạng");
            btnCarState.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
        }
        else if (fragmentHomeState == 0) {
            btnCarState.setText("Mới tiếp nhận");
            btnCarState.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        } else if (fragmentHomeState == 1) {
            btnCarState.setText("Đang sửa");
            btnCarState.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow));
        } else if (fragmentHomeState == 2) {
            btnCarState.setText("Mới hoàn thành");
            btnCarState.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
        }

        FragmentHome.selectedState = -1;
    }

    private void filterList(String text) {
        List<Car> targetedCars = unpaidCars;
        Log.i("selectedState", String.valueOf(selectedState));
        if (selectedState == 0) {
            targetedCars = newCars;
        } else if (selectedState == 1) {
            targetedCars = repairingCars;
        } else if (selectedState == 2) {
            targetedCars = completedCars;
        }
        filteredList = new ArrayList<>();
        for (Car car : targetedCars) {
            String lowercaseText = text.toLowerCase();
            if (car.getOwnerName().toLowerCase().contains(lowercaseText)) {
                filteredList.add(car);
            } else if (deAccent(car.getOwnerName()).toLowerCase().contains(lowercaseText)) {
                filteredList.add(car);
            } else if (car.getLicensePlate().toLowerCase().contains(lowercaseText)) {
                filteredList.add(car);
            } else if (formatter.format(car.getReceiveDate()).contains(lowercaseText)) {
                filteredList.add(car);
            }
        }
        carAdapter.setData(filteredList);
    }

    @Override
    public void onItemClick(int position) {
        Car currentCar = filteredList.get(position);
        int state = currentCar.getState();
        if (state == 0) {
            Intent intent = new Intent(getContext(), NewCarDetailActivity.class);
            intent.putExtra("LICENSE_PLATE", currentCar.getLicensePlate());
            intent.putExtra("CAR_BRAND_ID", currentCar.getCarBrandId());
            intent.putExtra("CAR_BRAND_TEXT", currentCar.getCarBrandText());
            intent.putExtra("CAR_TYPE_ID", currentCar.getCarTypeId());
            intent.putExtra("CAR_TYPE_TEXT", currentCar.getCarTypeText());
            intent.putExtra("OWNER_NAME", currentCar.getOwnerName());
            intent.putExtra("PHONE_NUMBER", currentCar.getPhoneNumber());
            intent.putExtra("RECEIVE_DATE", formatter.format(currentCar.getReceiveDate()));
            intent.putExtra("CAR_IMAGE", currentCar.getCarImage());
            intent.putExtra("STATE", state);
            startActivity(intent);
        }
        else if (state == 1) {
            Intent intent = new Intent(getContext(), RepairingCarDetailActivity.class);
            intent.putExtra("LICENSE_PLATE", currentCar.getLicensePlate());
            intent.putExtra("CAR_BRAND_ID", currentCar.getCarBrandId());
            intent.putExtra("CAR_BRAND_TEXT", currentCar.getCarBrandText());
            intent.putExtra("CAR_TYPE_ID", currentCar.getCarTypeId());
            intent.putExtra("CAR_TYPE_TEXT", currentCar.getCarTypeText());
            intent.putExtra("OWNER_NAME", currentCar.getOwnerName());
            intent.putExtra("PHONE_NUMBER", currentCar.getPhoneNumber());
            intent.putExtra("RECEIVE_DATE", formatter.format(currentCar.getReceiveDate()));
            intent.putExtra("CAR_IMAGE", currentCar.getCarImage());
            intent.putExtra("STATE", state);
            intent.putExtra("CAR_SERVICES", (Serializable) currentCar.getCarServiceList());
            intent.putExtra("CAR_SUPPLIES", (Serializable) currentCar.getCarSupplyList());
            startActivity(intent);
        }
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
            filteredList = unpaidCars;
            carAdapter.setData(filteredList);
            selectedState = -1;
            searchView.setQuery("", false);
            searchView.clearFocus();
        }
        else if (itemId == R.id.optionNew) {
            btnCarState.setText("Mới tiếp nhận");
            btnCarState.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            filteredList = newCars;
            carAdapter.setData(filteredList);
            selectedState = 0;
            searchView.setQuery("", false);
            searchView.clearFocus();
        } else if (itemId == R.id.optionRepairing) {
            btnCarState.setText("Đang sửa");
            btnCarState.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow));
            filteredList = repairingCars;
            carAdapter.setData(filteredList);
            selectedState = 1;
            searchView.setQuery("", false);
            searchView.clearFocus();
        } else if (itemId == R.id.optionCompleted) {
            btnCarState.setText("Mới hoàn thành");
            btnCarState.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            filteredList = completedCars;
            carAdapter.setData(filteredList);
            selectedState = 2;
            searchView.setQuery("", false);
            searchView.clearFocus();
        }
        return super.onContextItemSelected(item);
    }

    public String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}