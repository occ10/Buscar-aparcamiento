package com.example.walid.tfg;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import model.Apua;
import model.entities.Zona;

/**
 * Created by walid on 10/03/2018.
 */

public class OcuppyZoneActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    AsyncTask<Void, Void, Zona> loadingTask;
    Zona zona = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Apua apua = new Apua(this);
        Bundle extras = getIntent().getExtras();
        String code = extras.getString("idZona");
        Zona zona = new Zona();
        Log.d("UNIVERSITY22222", code);

        if (loadingTask == null) {
            loadingTask = new LoadingTask(apua,code);
            loadingTask.execute();
        }

        setContentView(R.layout.fragment_layout_tree);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Spinner spinner = (Spinner) findViewById(R.id.parkings_spinner);
        TextView textview = (TextView) findViewById(R.id.parkingMessage);

        spinner.setVisibility(View.GONE);
        textview.setText("Info! Debes desocupar la zona ocupada y luego puedes volver a aparcar.");


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng ua = new LatLng(38.384638888889, -0.51314722222222);
        LatLng ua = new LatLng(zona.getLat(), zona.getLon());
        mMap.addMarker(new MarkerOptions().position(ua).title("aparcamiento:"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ua));
        mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
    }

    public class LoadingTask extends AsyncTask<Void, Void, Zona> {
        private Apua apua;
        private String code;

        public LoadingTask(Apua apua, String code) {
            this.apua = apua;
            this.code = code;
        }

        @Override
        protected Zona doInBackground(Void... voids) {
            //Zona zona = null;
            try {
                zona = apua.serverAgent.getZoneFromServer(code);

            } catch (Exception e) {
                Log.d("UNIVERSITY", "Error trying to log. ", e);
            }
            return zona;
        }

        @Override
        protected void onPostExecute(Zona zona) {
            //parkingsList = parkings;
            //dataAdapter.notifyDataSetChanged();

        }
    }
}
