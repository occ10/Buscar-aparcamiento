package com.example.walid.tfg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Apua;
import model.entities.Car;
import android.widget.Toast;

public class InsertUserCarActivity extends AppCompatActivity {

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
    //private Car car;
    private Button insertCarButton;
    private TextView carRegistration;
    AsyncTask<Void, Void, Car> loadingTask;
    private View insertCarProgress;
    private View insertCarScroll;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user_car);
        getSupportActionBar().setHomeButtonEnabled(true);
        final Car car = new Car();
        carRegistration = (TextView) findViewById(R.id.carRegistration);
        insertCarButton = (Button) findViewById(R.id.insertCarButton);
        insertCarProgress = findViewById(R.id.insertCarProgress);
        insertCarScroll = findViewById(R.id.insertCarScroll);
        brandSpinner = (Spinner) findViewById(R.id.brandSpinner);
        insertCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertCar(car);
            }

        });
        ArrayAdapter<CharSequence> brandAdapter = ArrayAdapter.createFromResource(InsertUserCarActivity.this,
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

        modelSpinner = (Spinner) findViewById(R.id.modelSpinner);
        ArrayAdapter<CharSequence> modelAdapter = ArrayAdapter.createFromResource(InsertUserCarActivity.this,
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

        colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(InsertUserCarActivity.this,
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

        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(InsertUserCarActivity.this,
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

        seatingSpinner = (Spinner) findViewById(R.id.seatingSpinner);
        ArrayAdapter<CharSequence> seatingAdapter = ArrayAdapter.createFromResource(InsertUserCarActivity.this,
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

    }
   @Override
    public void onStart(){
        super.onStart();
        //getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;
        // this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menuSearch:
                intent = new Intent().setClass(
                        InsertUserCarActivity.this, SearchAnounce.class);
                startActivity(intent);
                break;
            case R.id.closeSesion:
                intent = new Intent().setClass(
                        InsertUserCarActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            /*case R.id.editPerfil:
                intent = new Intent().setClass(
                        InsertUserCarActivity.this, EditPerfilActivity.class);
                startActivity(intent);
                break;*/
        }
        return true;
    }

    private void insertCar(Car car) {
        final Apua apua = new Apua(InsertUserCarActivity.this);
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
            if (loadingTask == null) {
                showProgress(true);
                car.setRegistration(registration);
                loadingTask = new LoadingTask(apua,car);
                loadingTask.execute();
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            insertCarScroll.setVisibility(show ? View.GONE : View.VISIBLE);
            insertCarScroll.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    insertCarScroll.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            insertCarProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            insertCarProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
            insertCarProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            insertCarProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            insertCarScroll.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    private boolean isRegitrationValid(String regitration) {
        String regx = "^\\d{4}[A-Z]{3}";
        Pattern pat = Pattern.compile(regx);
        Matcher mat = pat.matcher(regitration);
        return mat.matches();
    }

    public class LoadingTask extends AsyncTask<Void, Void, Car> {
        private Apua apua;
        private Car car;

        public LoadingTask(Apua apua, Car car) {
            this.apua = apua;
            this.car = car;
        }

        @Override
        protected Car doInBackground(Void... voids) {
            Car carResult = null;
            try {
                car.setUser("kkk@kkk.com");
                carResult = apua.serverAgent.insertCar(car);
            } catch (Exception e) {
                cancel(true);
                Log.d("UNIVERSITY", "Error trying to insert car. ", e);
            }
            return carResult;
        }

        @Override
        protected void onPostExecute(Car result) {
            //showProgress(false);
            loadingTask = null;
            if(result != null){
                Log.d("UNIVERSITY", "the car is inserted correctly");
                Toast.makeText(InsertUserCarActivity.this,
                        "the car is inserted correctly",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
        @Override
        protected void onCancelled() {
            showProgress(false);
            loadingTask = null;
                Toast.makeText(InsertUserCarActivity.this,
                        "try mas later, an error has occured in the server",
                        Toast.LENGTH_LONG).show();

        }
    }
}
