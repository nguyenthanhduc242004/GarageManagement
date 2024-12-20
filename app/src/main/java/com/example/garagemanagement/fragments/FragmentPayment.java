package com.example.garagemanagement.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.garagemanagement.DateDeserializer;
import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.R;
import com.example.garagemanagement.adapter.CarAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPayment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPayment extends Fragment implements RecyclerViewInterface {
    DatePickerDialog receiveDateLeftPickerDialog;
    DatePickerDialog receiveDateRightPickerDialog;
    DatePickerDialog paymentDateLeftPickerDialog;
    DatePickerDialog paymentDateRightPickerDialog;

    final long aDayInMilies = 86400000;


    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    Button buttonReceiveDateLeft;
    Button buttonReceiveDateRight;
    Button buttonPaymentDateLeft;
    Button buttonPaymentDateRight;

    long todayTime = atEndOfDay(new Date()).getTime();
    long receiveDateMax = todayTime;
    long receiveDateMin = 0;
    long paymentDateMax = todayTime;
    long paymentDateMin = 0;

    Calendar c = Calendar.getInstance();

    CarAdapter carAdapter;
    List<Car> filteredList = new ArrayList<>();
    List<Car> paidCars = new ArrayList<>();
    List<Car> filterListByDate = new ArrayList<>();

    SearchView searchView;
    EditText txtSearch;

    public static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            endOfDay = localDateTime.with(LocalTime.MAX);
        }
        return localDateTimeToDate(endOfDay);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        }
        return null;
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentPayment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPayment newInstance(String param1, String param2) {
        FragmentPayment fragment = new FragmentPayment();
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
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        //        SEARCH VIEW
        searchView = view.findViewById(R.id.searchViewPayment);
        searchView.setIconified(false);
        txtSearch = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setIconified(false);
                return true;
            }
        });

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
                "    ],\n" +
                "    \"paymentDate\": \"2024, 12, 19\"\n" +
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
        paidCars = new ArrayList<>();
        for (Car car : cars) {
            if (car.getState() == 3) {
                paidCars.add(car);
            }
        }
        filteredList = paidCars;
        filterListByDate = paidCars;

        //        CarDetailRecyclerView
        carAdapter = new CarAdapter(getContext(),CarAdapter.TYPE_CAR_PAID, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerViewPaymentList = view.findViewById(R.id.recyclerViewPaymentList);
        recyclerViewPaymentList.setLayoutManager(linearLayoutManager);
        recyclerViewPaymentList.setFocusable(false);
        carAdapter.setData(filteredList);
        recyclerViewPaymentList.setAdapter(carAdapter);

//        DATE PICKER:
        buttonReceiveDateLeft = view.findViewById(R.id.buttonReceiveDateLeft);
        initReceiveDateLeftPickerDialog();
        buttonReceiveDateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveDateLeftPickerDialog.show();
            }
        });

        buttonReceiveDateRight = view.findViewById(R.id.buttonReceiveDateRight);
        initReceiveDateRightPickerDialog();
        buttonReceiveDateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveDateRightPickerDialog.show();
            }
        });

        buttonPaymentDateLeft = view.findViewById(R.id.buttonPaymentDateLeft);
        initPaymentDateLeftPickerDialog();
        buttonPaymentDateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentDateLeftPickerDialog.show();
            }
        });

        buttonPaymentDateRight = view.findViewById(R.id.buttonPaymentDateRight);
        initPaymentDateRightPickerDialog();
        buttonPaymentDateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentDateRightPickerDialog.show();
            }
        });

        ImageButton buttonFilter = view.findViewById(R.id.buttonFilter);
        LinearLayout filterWrapper = view.findViewById(R.id.filterWrapper);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterWrapper.getVisibility() == View.GONE) {
                    filterWrapper.setVisibility(View.VISIBLE);
                    buttonFilter.setImageResource(R.drawable.outline_filter_alt_off_24);
                }
                else if (filterWrapper.getVisibility() == View.VISIBLE) {
                    filterWrapper.setVisibility(View.GONE);
                    buttonFilter.setImageResource(R.drawable.outline_filter_alt_24);
                    clearDate();
                }

            }
        });

        return view;
    }

    private void filterList(String text) {
        filteredList = new ArrayList<>();
        for (Car car : filterListByDate) {
            String lowercaseText = text.toLowerCase();
            if (car.getOwnerName().toLowerCase().contains(lowercaseText)) {
                filteredList.add(car);
            } else if (deAccent(car.getOwnerName()).toLowerCase().contains(lowercaseText)) {
                filteredList.add(car);
            } else if (car.getLicensePlate().toLowerCase().contains(lowercaseText)) {
                filteredList.add(car);
            } else if (formatter.format(car.getReceiveDate()).contains(lowercaseText)) {
                filteredList.add(car);
            } else if (formatter.format(car.getPaymentDate()).contains(lowercaseText)) {
                filteredList.add(car);
            }
        }
        carAdapter.setData(filteredList);
    }

    public String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }


    private void getFilterListByDate(long receiveDateMin, long receiveDateMax, long paymentDateMin, long paymentDateMax) {
        filterListByDate = new ArrayList<>();
        if (receiveDateMin != 0 && paymentDateMin != 0) {
            for (int i = 0; i < paidCars.size(); i++) {
                Car currentCar = paidCars.get(i);
                if (currentCar.getReceiveDate().after(new Date(receiveDateMin - aDayInMilies))
                        && currentCar.getReceiveDate().before(new Date(receiveDateMax))
                        && currentCar.getPaymentDate().after(new Date(paymentDateMin - aDayInMilies))
                        && currentCar.getPaymentDate().before(new Date(paymentDateMax))) {
                    filterListByDate.add(paidCars.get(i));
                }
            }
        } else if (receiveDateMin != 0) {
            for (int i = 0; i < paidCars.size(); i++) {
                Car currentCar = paidCars.get(i);
                if (currentCar.getReceiveDate().after(new Date(receiveDateMin - aDayInMilies))
                        && currentCar.getReceiveDate().before(new Date(receiveDateMax))
                        && currentCar.getPaymentDate().before(new Date(paymentDateMax))) {
                    filterListByDate.add(paidCars.get(i));
                }
            }
        } else if (paymentDateMin != 0) {
            for (int i = 0; i < paidCars.size(); i++) {
                Car currentCar = paidCars.get(i);
                if (currentCar.getReceiveDate().before(new Date(receiveDateMax))
                        && currentCar.getPaymentDate().after(new Date(paymentDateMin - aDayInMilies))
                        && currentCar.getPaymentDate().before(new Date(paymentDateMax))) {
                    filterListByDate.add(paidCars.get(i));
                }
            }
        } else {
            for (int i = 0; i < paidCars.size(); i++) {
                Car currentCar = paidCars.get(i);
                if (currentCar.getReceiveDate().before(new Date(receiveDateMax))
                        && currentCar.getPaymentDate().before(new Date(paymentDateMax))) {
                    filterListByDate.add(paidCars.get(i));
                }
            }
        }
        filterList(txtSearch.getText().toString());
    }


    private void initReceiveDateLeftPickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                buttonReceiveDateLeft.setText(date);
                c.set(year, month - 1, day, 0, 0, 0);
                receiveDateMin = c.getTimeInMillis();
                receiveDateRightPickerDialog.getDatePicker().setMinDate(receiveDateMin);

                getFilterListByDate(receiveDateMin, receiveDateMax, paymentDateMin, paymentDateMax);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        receiveDateLeftPickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        receiveDateLeftPickerDialog.getDatePicker().setMaxDate(receiveDateMax);
    }

    private void initReceiveDateRightPickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                buttonReceiveDateRight.setText(date);
                c.set(year, month - 1, day, 0, 0, 0);
                receiveDateMax = c.getTimeInMillis();
                receiveDateLeftPickerDialog.getDatePicker().setMaxDate(receiveDateMax);
                getFilterListByDate(receiveDateMin, receiveDateMax, paymentDateMin, paymentDateMax);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        receiveDateRightPickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        receiveDateRightPickerDialog.getDatePicker().setMaxDate(receiveDateMax);
    }


    private void initPaymentDateLeftPickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                buttonPaymentDateLeft.setText(date);
                c.set(year, month - 1, day, 0, 0, 0);
                paymentDateMin = c.getTimeInMillis();
                paymentDateRightPickerDialog.getDatePicker().setMinDate(paymentDateMin);

                getFilterListByDate(receiveDateMin, receiveDateMax, paymentDateMin, paymentDateMax);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        paymentDateLeftPickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        paymentDateLeftPickerDialog.getDatePicker().setMaxDate(paymentDateMax);
    }

    private void initPaymentDateRightPickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                buttonPaymentDateRight.setText(date);
                c.set(year, month - 1, day, 0, 0, 0);
                paymentDateMax = c.getTimeInMillis();
                paymentDateLeftPickerDialog.getDatePicker().setMaxDate(paymentDateMax);

                getFilterListByDate(receiveDateMin, receiveDateMax, paymentDateMin, paymentDateMax);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        paymentDateRightPickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        paymentDateRightPickerDialog.getDatePicker().setMaxDate(paymentDateMax);
    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }
    private void clearDate() {
        buttonReceiveDateLeft.setText("../../....");
        buttonReceiveDateRight.setText("../../....");
        buttonPaymentDateLeft.setText("../../....");
        buttonPaymentDateRight.setText("../../....");
        receiveDateMin = 0;
        receiveDateMax = todayTime;
        paymentDateMin = 0;
        paymentDateMax = todayTime;
        receiveDateLeftPickerDialog.getDatePicker().setMinDate(receiveDateMin);
        receiveDateLeftPickerDialog.getDatePicker().setMaxDate(receiveDateMax);
        receiveDateRightPickerDialog.getDatePicker().setMaxDate(receiveDateMax);
        paymentDateLeftPickerDialog.getDatePicker().setMinDate(paymentDateMin);
        paymentDateLeftPickerDialog.getDatePicker().setMaxDate(paymentDateMax);
        paymentDateRightPickerDialog.getDatePicker().setMaxDate(paymentDateMax);
        c.setTime(new Date());
        receiveDateLeftPickerDialog.getDatePicker().updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        receiveDateRightPickerDialog.getDatePicker().updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        paymentDateLeftPickerDialog.getDatePicker().updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        paymentDateRightPickerDialog.getDatePicker().updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        filterListByDate = paidCars;
        filterList(txtSearch.getText().toString());
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(int position, int state) {

    }
}