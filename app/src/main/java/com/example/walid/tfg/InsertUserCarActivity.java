package com.example.walid.tfg;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Apua;
import model.entities.Car;

public class InsertUserCarActivity extends Fragment {

    private Spinner brandSpinner;
    private Spinner modelSpinner;
    private Spinner colorSpinner;
    private Spinner categorySpinner;
    private Spinner seatingSpinner;
    private String brand;
    private String model;
    private String color;
    private String category;
    private int seating;
    private Car car;
    private Button insertCarButton;
    private TextView carRegistration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_insert_user_car, container, false);
        car = new Car();
        carRegistration = (TextView) view.findViewById(R.id.carRegistration);
        insertCarButton = (Button) view.findViewById(R.id.insertCarButton);
        brandSpinner = (Spinner) view.findViewById(R.id.brandSpinner);
        insertCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertCar();
            }

        });
        ArrayAdapter<CharSequence> brandAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.car_brand, android.R.layout.simple_spinner_item);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(brandAdapter);
        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brand = parent.getItemAtPosition(position).toString();
                car.setBrand(brand);
                Log.d("marca del coche", brand);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        modelSpinner = (Spinner) view.findViewById(R.id.modelSpinner);
        ArrayAdapter<CharSequence> modelAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.car_model, android.R.layout.simple_spinner_item);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(modelAdapter);
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model = parent.getItemAtPosition(position).toString();
                car.setModel(model);
                Log.d("marca del coche", model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        colorSpinner = (Spinner) view.findViewById(R.id.colorSpinner);
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.car_color, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = parent.getItemAtPosition(position).toString();
                car.setColor(color);
                Log.d("marca del coche", color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.car_category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
                car.setCategory(category);
                Log.d("marca del coche", category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seatingSpinner = (Spinner) view.findViewById(R.id.seatingSpinner);
        ArrayAdapter<CharSequence> seatingAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.car_seating, android.R.layout.simple_spinner_item);
        seatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatingSpinner.setAdapter(seatingAdapter);
        seatingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seating = Integer.valueOf(parent.getItemAtPosition(position).toString());
                car.setSeating(seating);
                Log.d("marca del coche", String.valueOf(seating));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
    private void insertCar() {

        // Reset errors.
        carRegistration.setError(null);

        // Store values at the time of the login attempt.
        String registration = carRegistration.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(registration)) {
            carRegistration.setError(getString(R.string.error_field_required));
            focusView = carRegistration;
            cancel = true;
        } else if (!isRegitrationValid(registration)) {
            carRegistration.setError(getString(R.string.error_invalid_registration));
            focusView = carRegistration;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            //final Apua apua =  new Apua(this);
        }
    }
    private boolean isRegitrationValid(String regitration) {
        String regx = "^\\d{4}[A-Z]{3}";
        Pattern pat = Pattern.compile(regx);
        Matcher mat = pat.matcher(regitration);
        return mat.matches();
    }
}
