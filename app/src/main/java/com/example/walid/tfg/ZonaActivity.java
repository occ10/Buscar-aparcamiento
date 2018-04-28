package com.example.walid.tfg;

import android.app.ActionBar;
import android.content.Intent;
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
import android.widget.Button;
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

public class ZonaActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private GoogleMap mMap;
    AsyncTask<Void, Void, List<Zona>> loadingTask;
    private List<Zona> zonasList = new ArrayList<>();
    ArrayAdapter<SpinnerValue> dataAdapter;
    List<SpinnerValue> values;
    Spinner spinner;
    SpinnerValue spinnerValue;
    private Button searchParkingButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Apua apua = new Apua(this);
        Bundle extras = getIntent().getExtras();
        String code = extras.getString("idParking");
        if (loadingTask == null) {
            loadingTask = new LoadingTask(apua,code);
            loadingTask.execute();
        }
        setContentView(R.layout.fragment_layout_tree);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        spinner = (Spinner) findViewById(R.id.parkings_spinner);
        TextView textview = (TextView) findViewById(R.id.parkingMessage);
        searchParkingButton = (Button) findViewById(R.id.searchParkingButton);
        searchParkingButton.setOnClickListener(this);
        values = new ArrayList<SpinnerValue>();

        if(zonasList.size() > 0) {
            textview.setText("Elige zona para aparcar");
            searchParkingButton.setText("APARCAR");
            spinner.setVisibility(View.VISIBLE);
            for (int i = 0; i < zonasList.size(); i++) {

                values.add(new SpinnerValue("Zona " + (i + 1), String.valueOf(zonasList.get(i).getId())));
            }
            dataAdapter = new ArrayAdapter<SpinnerValue>(this, android.R.layout.simple_spinner_item, values);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            //spinner.setSelected(false);
            //spinner.setSelection(0, true);
            spinner.setOnItemSelectedListener(this);
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
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menuSearch:
                intent = new Intent().setClass(
                        ZonaActivity.this, SearchAnounce.class);
                startActivity(intent);
                break;
            case R.id.closeSesion:
                intent = new Intent().setClass(
                        ZonaActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.searchUser:
                intent = new Intent().setClass(
                        ZonaActivity.this, SearchUserActivity.class);
                startActivity(intent);
                break;
            case R.id.editPerfil:
                intent = new Intent().setClass(
                        ZonaActivity.this, EditPerfilActivity.class);
                startActivity(intent);
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
                mMap.addMarker(new MarkerOptions().position(ua).title("zona: " + (i+1) + "\naparcamiento: " + zonasList.get(i).getAparcamiento()
                        + "\nlatitude: " + zonasList.get(i).getLat() +
                        "\nlongitude: " + zonasList.get(i).getLon()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(ua));
                mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
                //mPerth.setTag("z");

            }
        }else{

            LatLng ua = new LatLng(38.384638888889, -0.51314722222222);
            mMap.addMarker(new MarkerOptions().position(ua)).setVisible(false);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ua));
            mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spinnerValue = (SpinnerValue) parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        Zona zona = null;
        String codeZona = spinnerValue.getValue();

        for(int i=0;i<zonasList.size();i++){
            if(zonasList.get(i).getId() == Integer.valueOf(codeZona)){
                zona = zonasList.get(i);
                break;
            }
        }
        Intent intent = new Intent().setClass(
                ZonaActivity.this.getBaseContext(), OcuppyZone.class);
        intent.putExtra("zona", zona);
        intent.putExtra("desocuppy", false);
        startActivity(intent);
        finish();
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
                Log.d("zonasList", String.valueOf(zonasList.size()));
            } catch (Exception e) {
                cancel(true);
                Log.d("UNIVERSITY", "Error trying to log. ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Zona> parkings) {
            //parkingsList = parkings;
            //dataAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onCancelled() {
            loadingTask = null;
            Toast.makeText(ZonaActivity.this,
                    "try mas later, an error has occured in the server",
                    Toast.LENGTH_LONG).show();

        }
    }
}

