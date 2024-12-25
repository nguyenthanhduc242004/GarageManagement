package com.example.garagemanagement.fragments;

import static com.example.garagemanagement.MainActivity.mainProgressDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.garagemanagement.AddCarActivity;
import com.example.garagemanagement.AddRepairCardActivity;
import com.example.garagemanagement.CompletedCarDetailActivity;
import com.example.garagemanagement.MainActivity;
import com.example.garagemanagement.NewCarDetailActivity;
import com.example.garagemanagement.Interfaces.RecyclerViewInterface;
import com.example.garagemanagement.Objects.Car;
import com.example.garagemanagement.Objects.CarService;
import com.example.garagemanagement.Objects.CarSupply;
import com.example.garagemanagement.R;
import com.example.garagemanagement.RepairingCarDetailActivity;
import com.example.garagemanagement.adapter.CarAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment implements RecyclerViewInterface {
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public static List<Car> cars = new ArrayList<>();
    List<Car> newCars = new ArrayList<>();
    List<Car> repairingCars = new ArrayList<>();
    List<Car> completedCars = new ArrayList<>();

    CarAdapter newCarsAdapter;
    CarAdapter repairingCarsAdapter;
    CarAdapter completedCarsAdapter;

    RecyclerView recyclerViewNewCars;

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


//        New Car RecyclerView:
        newCarsAdapter = new CarAdapter(getContext(), CarAdapter.TYPE_CAR_HOME, this);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), 2);
        recyclerViewNewCars = view.findViewById(R.id.recyclerViewNewCars);
        recyclerViewNewCars.setLayoutManager(gridLayoutManager1);
        recyclerViewNewCars.setFocusable(false);
        recyclerViewNewCars.setNestedScrollingEnabled(false);
        recyclerViewNewCars.setAdapter(newCarsAdapter);

//        Repairing Car RecyclerView:
        repairingCarsAdapter = new CarAdapter(getContext(), CarAdapter.TYPE_CAR_HOME, this);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 2);
        RecyclerView recyclerViewRepairingCars = view.findViewById(R.id.recyclerViewRepairingCars);
        recyclerViewRepairingCars.setLayoutManager(gridLayoutManager2);
        recyclerViewRepairingCars.setFocusable(false);
        recyclerViewRepairingCars.setNestedScrollingEnabled(false);
        recyclerViewRepairingCars.setAdapter(repairingCarsAdapter);

//        Completed Car RecyclerView:
        completedCarsAdapter = new CarAdapter(getContext(), CarAdapter.TYPE_CAR_HOME, this);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getContext(), 2);
        RecyclerView recyclerViewCompletedCars = view.findViewById(R.id.recyclerViewCompletedCars);
        recyclerViewCompletedCars.setLayoutManager(gridLayoutManager3);
        recyclerViewCompletedCars.setFocusable(false);
        recyclerViewCompletedCars.setNestedScrollingEnabled(false);
        recyclerViewCompletedCars.setAdapter(completedCarsAdapter);

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

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang tải...");
        progressDialog.show();

        db = FirebaseFirestore.getInstance();
        EventChangeListener();

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



    private void EventChangeListener() {
        cars = new ArrayList<>();
        db.collection("Car")
                .orderBy("receiveDate", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            QueryDocumentSnapshot document = dc.getDocument();
                            DocumentChange.Type type = dc.getType();
                            Car car = document.toObject(Car.class);
                            car.setCarId(document.getId());
                            int state = car.getState();
                            if (type == DocumentChange.Type.ADDED) {
                                if (state == 0 && newCars.size() <= 6) {
                                    newCars.add(car);
                                    setCarBrandTextAndCarTypeTextToAdapter(car, newCars, newCarsAdapter);
                                } else if (state == 1 && repairingCars.size() <= 6) {
                                    repairingCars.add(car);
                                    setCarBrandTextAndCarTypeTextToAdapter(car, repairingCars, repairingCarsAdapter);
                                    setCarServiceListAndCarTypeListToAdapter(car, repairingCars, repairingCarsAdapter);

                                } else if (state == 2 && completedCars.size() <= 6) {
                                    completedCars.add(car);
                                    setCarBrandTextAndCarTypeTextToAdapter(car, completedCars, completedCarsAdapter);
                                    setCarServiceListAndCarTypeListToAdapter(car, completedCars, completedCarsAdapter);
                                } else if (state == 3) {
                                    setCarBrandTextAndCarTypeTextToAdapter(car, cars, null);
                                    setCarServiceListAndCarTypeListToAdapter(car, cars, null);
                                }
                                cars.add(car);
                            }
                            else if (type == DocumentChange.Type.MODIFIED) {
                                for (int i = 0; i < newCars.size(); i++) {
                                    if (car.getCarId().equals(newCars.get(i).getCarId())) {
                                        newCars.set(i, car);
                                        if (state == 1) {
                                            repairingCars.add(newCars.remove(i));
                                        }
                                        break;
                                    }
                                }
                                for (int i = 0; i < repairingCars.size(); i++) {
                                    if (car.getCarId().equals(repairingCars.get(i).getCarId())) {
                                        repairingCars.set(i, car);
                                        if (state == 2) {
                                            completedCars.add(repairingCars.remove(i));
                                        }
                                        break;
                                    }
                                }
                                for (int i = 0; i < completedCars.size(); i++) {
                                    if (car.getCarId().equals(completedCars.get(i).getCarId())) {
                                        completedCars.set(i, car);
                                        if (state == 3) {
                                            completedCars.remove(i);
                                        }
                                        break;
                                    }
                                }
                                if (state == 0) {
                                    setCarBrandTextAndCarTypeTextToAdapter(car, newCars, newCarsAdapter);
                                } else if (state == 1) {
                                    setCarBrandTextAndCarTypeTextToAdapter(car, repairingCars, repairingCarsAdapter);
                                    setCarServiceListAndCarTypeListToAdapter(car, repairingCars, repairingCarsAdapter);
                                } else if (state == 2 ){
                                    setCarBrandTextAndCarTypeTextToAdapter(car, completedCars, completedCarsAdapter);
                                    setCarServiceListAndCarTypeListToAdapter(car, completedCars, completedCarsAdapter);
                                }

                                for (int i = 0; i < cars.size(); i++) {
                                    if (car.getCarId().equals(cars.get(i).getCarId())) {
                                        completedCars.set(i, car);
                                        break;
                                    }
                                }
                            }
                            else if (type == DocumentChange.Type.REMOVED) {
                                for (int i = 0; i < newCars.size(); i++) {
                                    if (car.getCarId().equals(newCars.get(i).getCarId())) {
                                        newCars.remove(i);
                                        break;
                                    }
                                }
                            }
                            newCarsAdapter.setData(newCars);
                            repairingCarsAdapter.setData(repairingCars);
                            completedCarsAdapter.setData(completedCars);
                        }
                    }
                });
    }

    private void setCarBrandTextAndCarTypeTextToAdapter(Car car, List<Car> carList, CarAdapter carAdapter) {
        progressDialog.show();
        db.collection("CarBrand")
                .document(car.getCarBrandId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for (int i = 0; i < carList.size(); i++) {
                            if (carList.get(i).getCarId().equals(car.getCarId())) {
                                carList.get(i).setCarBrandText(documentSnapshot.getString("carBrandText"));
                                if (carAdapter != null) {
                                    carAdapter.notifyDataSetChanged();
                                }
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                                break;
                            }
                        }
                    }
                });
        progressDialog.show();
        db.collection("CarType")
                .document(car.getCarTypeId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for (int i = 0; i < carList.size(); i++) {
                            if (carList.get(i).getCarId().equals(car.getCarId())) {
                                carList.get(i).setCarTypeText(documentSnapshot.getString("carTypeText"));
                                if (carAdapter != null) {
                                    carAdapter.notifyDataSetChanged();
                                }
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                                break;
                            }
                        }
                    }
                });
    }

    private void setCarServiceListAndCarTypeListToAdapter(Car car, List<Car> carList, CarAdapter carAdapter) {
        List<String> carServices = car.getCarServices();
        List<CarService> carServiceList = car.getCarServiceList();
        for (int i = 0; i < carServices.size(); i++) {
            String carServiceId = carServices.get(i);
            int finalI = i;
            progressDialog.show();
            db.collection("CarService")
                    .document(carServiceId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            CarService carService = documentSnapshot.toObject(CarService.class);
                            carService.setServiceId(carServiceId);
                            carServiceList.add(carService);
                            if (finalI == carServices.size() - 1) {
                                for (int j = 0; j < carList.size(); j++) {
                                    if (carList.get(j).getCarId().equals(car.getCarId())) {
                                        carList.get(j).setCarServiceList(carServiceList);
                                        if (carAdapter != null) {
                                            carAdapter.notifyDataSetChanged();
                                        }
                                        if (progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        break;
                                    }
                                }
                            }
                        }
                    });
        }

        Map<String, Integer> carSupplies = car.getCarSupplies();
        List<CarSupply> carSupplyList = car.getCarSupplyList();
        int i = -1;
        for (String key : carSupplies.keySet()) {
            i++;
            String carSupplyId = key;
            int quantity = carSupplies.get(key);
            int finalI = i;
            progressDialog.show();
            db.collection("CarSupply")
                    .document(carSupplyId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            CarSupply carSupply = documentSnapshot.toObject(CarSupply.class);
                            carSupply.setQuantity(quantity);
                            carSupply.setSupplyId(carSupplyId);
                            carSupplyList.add(carSupply);
                            if (finalI == carSupplies.keySet().size() - 1) {
                                for (int j = 0; j < carList.size(); j++) {
                                    if (carList.get(j).getCarId().equals(car.getCarId())) {
                                        carList.get(j).setCarSupplyList(carSupplyList);
                                        if (carAdapter != null) {
                                            carAdapter.notifyDataSetChanged();
                                        }
                                        if (progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        break;
                                    }
                                }
                            }
                        }
                    });
        }
    }


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(int position, int state) {
        if (state == 0) {
            if (newCars.get(position).getCarBrandText() != null && newCars.get(position).getCarTypeText() != null) {
                Intent intent = new Intent(getContext(), NewCarDetailActivity.class);
                intent.putExtra("CAR_ID", newCars.get(position).getCarId());
                intent.putExtra("LICENSE_PLATE", newCars.get(position).getLicensePlate());
                intent.putExtra("CAR_BRAND_ID", newCars.get(position).getCarBrandId());
                intent.putExtra("CAR_BRAND_TEXT", newCars.get(position).getCarBrandText());
                intent.putExtra("CAR_TYPE_ID", newCars.get(position).getCarTypeId());
                intent.putExtra("CAR_TYPE_TEXT", newCars.get(position).getCarTypeText());
                intent.putExtra("OWNER_NAME", newCars.get(position).getOwnerName());
                intent.putExtra("PHONE_NUMBER", newCars.get(position).getPhoneNumber());
                intent.putExtra("RECEIVE_DATE", newCars.get(position).getReceiveDate());
                intent.putExtra("CAR_IMAGE", newCars.get(position).getCarImage());
                intent.putExtra("STATE", state);
                startActivity(intent);
            }
        }
        else if (state == 1) {
            Intent intent = new Intent(getContext(), RepairingCarDetailActivity.class);
            intent.putExtra("CAR_ID", repairingCars.get(position).getCarId());
            intent.putExtra("LICENSE_PLATE", repairingCars.get(position).getLicensePlate());
            intent.putExtra("CAR_BRAND_ID", repairingCars.get(position).getCarBrandId());
            intent.putExtra("CAR_BRAND_TEXT", repairingCars.get(position).getCarBrandText());
            intent.putExtra("CAR_TYPE_ID", repairingCars.get(position).getCarTypeId());
            intent.putExtra("CAR_TYPE_TEXT", repairingCars.get(position).getCarTypeText());
            intent.putExtra("OWNER_NAME", repairingCars.get(position).getOwnerName());
            intent.putExtra("PHONE_NUMBER", repairingCars.get(position).getPhoneNumber());
            intent.putExtra("RECEIVE_DATE", repairingCars.get(position).getReceiveDate());
            intent.putExtra("CAR_IMAGE", repairingCars.get(position).getCarImage());
            intent.putExtra("STATE", state);
            intent.putExtra("CAR_SERVICES", (Serializable) repairingCars.get(position).getCarServiceList());
            intent.putExtra("CAR_SUPPLIES", (Serializable) repairingCars.get(position).getCarSupplyList());
            startActivity(intent);
        }
        else if (state == 2) {
            Intent intent = new Intent(getContext(), CompletedCarDetailActivity.class);
            intent.putExtra("CAR_ID", completedCars.get(position).getCarId());
            intent.putExtra("LICENSE_PLATE", completedCars.get(position).getLicensePlate());
            intent.putExtra("CAR_BRAND_ID", completedCars.get(position).getCarBrandId());
            intent.putExtra("CAR_BRAND_TEXT", completedCars.get(position).getCarBrandText());
            intent.putExtra("CAR_TYPE_ID", completedCars.get(position).getCarTypeId());
            intent.putExtra("CAR_TYPE_TEXT", completedCars.get(position).getCarTypeText());
            intent.putExtra("OWNER_NAME", completedCars.get(position).getOwnerName());
            intent.putExtra("PHONE_NUMBER", completedCars.get(position).getPhoneNumber());
            intent.putExtra("RECEIVE_DATE", completedCars.get(position).getReceiveDate());
            intent.putExtra("CAR_IMAGE", completedCars.get(position).getCarImage());
            intent.putExtra("STATE", state);
            intent.putExtra("CAR_SERVICES", (Serializable) completedCars.get(position).getCarServiceList());
            intent.putExtra("CAR_SUPPLIES", (Serializable) completedCars.get(position).getCarSupplyList());
            startActivity(intent);
        }
    }

    public void openFragmentCars(int state) {
        FragmentHome.selectedState = state;
        MainActivity.bottomNav.setSelectedItemId(R.id.itCars);
    }
}