package com.example.walid.tfg;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class OcuppyZone extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    AsyncTask<Void, Void, Boolean> loadingTask;
    Zona zona = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Apua apua = new Apua(this);
        Bundle extras = getIntent().getExtras();
        zona = extras.getParcelable("zona");
        boolean desocuppy = extras.getBoolean("desocuppy");
        /*String code = extras.getString("idZona");
        Log.d("UNIVERSITY22222", code);*/

        if (loadingTask == null && !desocuppy) {
            loadingTask = new LoadingTask(apua,zona.getId(),true, desocuppy);
            loadingTask.execute();
        }

        setContentView(R.layout.activity_ocuppy_zone);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        //ActionBar actionBar = getActionBar();
        getSupportActionBar().setHomeButtonEnabled(true);
        TextView textview = (TextView) findViewById(R.id.ZoneMessage);

        Button emtpyZone = (Button) findViewById(R.id.emtpyZone);
        emtpyZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            loadingTask = new LoadingTask(apua,zona.getId(),false, false);
            loadingTask.execute();
            finish();

            }
        });

            textview.setText("Info! Debes desocupar la zona ocupada y luego puedes volver a aparcar.");
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
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            case R.id.menu_buscar:
                Intent intent = new Intent().setClass(
                        OcuppyZone.this, SearchAnounce.class);
                startActivity(intent);
                break;
            /*case R.id.acercaDe:
                //lanzarAcercaDe();
                break;*/
        }
        return true;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ua = null;
        if (zona == null) {
            ua = new LatLng(38.384638888889, -0.51314722222222);
            mMap.addMarker(new MarkerOptions().position(ua)).setVisible(false);
        }
        else {
            ua = new LatLng(zona.getLat(), zona.getLon());
            mMap.addMarker(new MarkerOptions().position(ua).title("aparcamiento:"));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(ua));
        mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    public class LoadingTask extends AsyncTask<Void, Void, Boolean> {
        private Apua apua;
        private int code;
        private boolean excute;
        private boolean desocuppy;

        public LoadingTask(Apua apua, int code,boolean excute, boolean desocuppy) {
            this.apua = apua;
            this.code = code;
            this.excute = excute;
            this.desocuppy = desocuppy;

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result = false;
            try {
                if(excute && !desocuppy)
                    result = apua.serverAgent.updateZoneFromServer(code);
                   // zona = apua.serverAgent.getZoneFromServer("55");
                else if(!desocuppy){
                     result = apua.serverAgent.desocuppyZoneFromServer(code);
                     finish();
                }

            } catch (Exception e) {
                Log.d("UNIVERSITY", "Error trying to acces parking. ", e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            //parkingsList = parkings;
            //dataAdapter.notifyDataSetChanged();

            if (result) {
                //incluir progresBar
                //Intent intent = new Intent().setClass(OcuppyZone.this, OcuppyZone.class);
                //startActivity(intent);
                Toast.makeText(OcuppyZone.this, "La operacion se ha realizado correctamente", Toast.LENGTH_LONG).show();
                //finish();
            }else {
                Toast.makeText(OcuppyZone.this, "Ha habido un error en el servidor intentalo mas tarde ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
