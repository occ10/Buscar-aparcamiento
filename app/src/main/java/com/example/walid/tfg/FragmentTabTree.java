package com.example.walid.tfg;

/**
 * Created by walid on 20/01/2018.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import model.entities.Parking;

public class FragmentTabTree extends Fragment  implements OnMapReadyCallback {
    private GoogleMap mMap;
    private int idRuta;
    private Parking parking;
    AsyncTask<Void, Void, List<Parking> >loadingTask;
    private List<Parking> parkingsList = new ArrayList<>();
    ArrayAdapter<SpinnerValue> dataAdapter;
    List<SpinnerValue> values;
    Spinner spinner;
    public static FragmentTabTree newInstance() {
        FragmentTabTree fragment = new FragmentTabTree();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Apua apua =  new Apua(getActivity());

        if (loadingTask == null) {
            loadingTask = new LoadingTask(apua);
            loadingTask.execute();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_tree, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        values= new ArrayList<SpinnerValue>();

        for (int i=0; i < parkingsList.size(); i++) {

            values.add(new SpinnerValue("parking " + (i+1),parkingsList.get(i).getCodigo()));
        }

        spinner = (Spinner) view.findViewById(R.id.parkings_spinner);
        dataAdapter = new ArrayAdapter<SpinnerValue>(getActivity(),android.R.layout.simple_spinner_item, values);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelected(false);
        spinner.setSelection(0,true);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        SpinnerValue spinnerValue = (SpinnerValue) parent.getSelectedItem();
                        String codeParking = spinnerValue.getValue();
                        Log.d("idZona", codeParking);
                        Intent intent = new Intent().setClass(
                                getActivity(), ZonaActivity.class);
                        intent.putExtra("idZona", codeParking);
                        /*Toast.makeText(LoginActivity.this,
                                "Login correcto ",
                                Toast.LENGTH_SHORT).show();*/
                        startActivity(intent);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i=0; i < parkingsList.size(); i++) {

            // Add a marker in Sydney and move the camera
            LatLng ua = new LatLng(parkingsList.get(i).getLat(), parkingsList.get(i).getLon());
            mMap.addMarker(new MarkerOptions().position(ua).title("aparcamiento: "+parkingsList.get(i).getCodigo() +
                    "\nsuperfie: " + parkingsList.get(i).getSuperficie() +
            "\nplazas: "+ parkingsList.get(i).getPlazas() + "\nlatitude: " + parkingsList.get(i).getLat() +
                    "\nlongitude: " + parkingsList.get(i).getLon()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ua));
            mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
            //mPerth.setTag("z");
        }
    }

    public class LoadingTask extends AsyncTask<Void, Void, List<Parking>> {
        private Apua apua;

        public LoadingTask(Apua apua) {
            this.apua=apua;
        }

        @Override
        protected List<Parking> doInBackground(Void... voids) {
            List<Parking> parkings = null;
            try {
                parkingsList = apua.serverAgent.getParkingsFromServer();

            } catch (Exception e) {
                Log.d("UNIVERSITY", "Error trying to log. ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Parking> parkings) {
            //parkingsList = parkings;
            dataAdapter.notifyDataSetChanged();

        }
    }


}
