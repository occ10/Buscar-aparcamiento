package com.example.walid.tfg;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import model.Apua;
import model.entities.Zona;

/**
 * Created by walid on 04/03/2018.
 */

public class ZonaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    AsyncTask<Void, Void, List<Zona>> loadingTask;
    private List<Zona> zonasList = new ArrayList<>();
    ArrayAdapter<SpinnerValue> dataAdapter;
    List<SpinnerValue> values;
    Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Apua apua = new Apua(this);
        Bundle extras = getIntent().getExtras();
        String code = extras.getString("idZona");

        Log.d("UNIVERSITY22222", code);

        if (loadingTask == null) {
            loadingTask = new LoadingTask(apua,code);
            loadingTask.execute();
        }

        setContentView(R.layout.fragment_layout_tree);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        spinner = (Spinner) findViewById(R.id.parkings_spinner);
        TextView textview = (TextView) findViewById(R.id.parkingMessage);

        values = new ArrayList<SpinnerValue>();
        if(zonasList.size() > 0) {
            textview.setText("Elige zona para aparcar");
            spinner.setVisibility(View.VISIBLE);
            for (int i = 0; i < zonasList.size(); i++) {

                values.add(new SpinnerValue("Zona " + (i + 1), String.valueOf(zonasList.get(i).getId())));
            }


            dataAdapter = new ArrayAdapter<SpinnerValue>(this, android.R.layout.simple_spinner_item, values);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner.setSelected(false);
            spinner.setSelection(0, true);
            spinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent,
                                                   android.view.View v, int position, long id) {
                            SpinnerValue spinnerValue = (SpinnerValue) parent.getSelectedItem();
                            Log.d("UNIVERSITY", spinnerValue.getValue());

                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
        }else{

            spinner.setVisibility(View.GONE);
            textview.setText("De momento todas las zonas estan ocupadas, vuelve a buscar en otro parking");
            Toast.makeText(this, "De momento todas las zonas estan ocupadas, vuelve a buscar en otro parking ", Toast.LENGTH_SHORT).show();
        }


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
        switch (item.getItemId()) {
            case R.id.acercaDe:
                //lanzarAcercaDe();
                break;
        }
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if( zonasList.size() > 0 ){
            for (int i = 0; i < zonasList.size(); i++) {

                // Add a marker in Sydney and move the camera
                LatLng ua = new LatLng(zonasList.get(i).getLat(), zonasList.get(i).getLon());
                mMap.addMarker(new MarkerOptions().position(ua).title("aparcamiento: " + zonasList.get(i).getAparcamiento()
                        + "\nlatitude: " + zonasList.get(i).getLat() +
                        "\nlongitude: " + zonasList.get(i).getLon()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(ua));
                mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
                //mPerth.setTag("z");

            }
        }else{

            // Add a marker in Sydney and move the camera
            LatLng ua = new LatLng(38.384638888889, -0.51314722222222);
            mMap.addMarker(new MarkerOptions().position(ua)).setVisible(false);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ua));
            mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
        }
    }

    public class LoadingTask extends AsyncTask<Void, Void, List<Zona>> {
        private Apua apua;
        private String code;

        public LoadingTask(Apua apua, String code) {
            this.apua = apua;
            this.code = code;
        }

        @Override
        protected List<Zona> doInBackground(Void... voids) {
            List<Zona> parkings = null;
            try {
                zonasList = apua.serverAgent.getZonesFromServer(code);

            } catch (Exception e) {
                Log.d("UNIVERSITY", "Error trying to log. ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Zona> parkings) {
            //parkingsList = parkings;
            //dataAdapter.notifyDataSetChanged();

        }
    }
}

