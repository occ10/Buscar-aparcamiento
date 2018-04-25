package com.example.walid.tfg;

/**
 * Created by walid on 20/01/2018.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
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
import model.entities.Usuario;
import model.entities.Zona;

import android.app.ProgressDialog;

public class FragmentTabTree extends Fragment  implements OnMapReadyCallback, AdapterView.OnItemSelectedListener, View.OnClickListener {
    private GoogleMap mMap;
    private int idRuta;
    private Parking parking;
    AsyncTask<Void, Void, Zona >loadingTask;
    private List<Parking> parkingsList = new ArrayList<>();
    ArrayAdapter<SpinnerValue> dataAdapter;
    List<SpinnerValue> values;
    Spinner spinner;
    private ProgressBar progressBar;
    private Boolean mIsSpinnerFirstCall = false;
    private Boolean onresumeFirstCall = false;
    private Button searchParkingButton;
    Usuario usuario = null;
    Zona zone = null;
    private Apua apua = null;
    private SpinnerValue spinnerValue;

    public static FragmentTabTree newInstance() {
        FragmentTabTree fragment = new FragmentTabTree();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getActivity().getIntent().getExtras();
        usuario = extras.getParcelable("usuario");
        apua =  new Apua(getActivity());

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
        searchParkingButton = (Button) view.findViewById(R.id.searchParkingButton);
        searchParkingButton.setOnClickListener(this);
        spinner = (Spinner) view.findViewById(R.id.parkings_spinner);
        values= new ArrayList<SpinnerValue>();
        for (int i=0; i < parkingsList.size(); i++) {
            values.add(new SpinnerValue("parking " + (i+1),parkingsList.get(i).getCodigo()));
        }
        dataAdapter = new ArrayAdapter<SpinnerValue>(getActivity(),android.R.layout.simple_spinner_item, values);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        //spinner.setOnTouchListener(this);
        spinner.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent,
                               View v, int position, long id) {
        spinnerValue = (SpinnerValue) parent.getItemAtPosition(position);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //seleccion.setText("");
    }
    /*@Override
    public boolean onTouch(View v, MotionEvent event) {
        mIsSpinnerFirstCall = true;
        return false;
    }*/
    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("Call to ", "onResume");
        //mIsSpinnerFirstCall = true;
        //apua =  new Apua(getActivity());

        if (loadingTask == null && onresumeFirstCall) {
            loadingTask = new LoadingTask(apua);
            loadingTask.execute();
        }
    }
    @Override
    public void onStart()
    {
        super.onStart();
    }
    @Override
    public void onPause()
    {
        super.onPause();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i=0; i < parkingsList.size(); i++) {

            LatLng ua = new LatLng(parkingsList.get(i).getLat(), parkingsList.get(i).getLon());
            mMap.addMarker(new MarkerOptions().position(ua).title("parking: "+ (i+1) + "\naparcamiento: "+parkingsList.get(i).getCodigo() +
                    "\nsuperfie: " + parkingsList.get(i).getSuperficie() +
            "\nplazas: "+ parkingsList.get(i).getPlazas() + "\nlatitude: " + parkingsList.get(i).getLat() +
                    "\nlongitude: " + parkingsList.get(i).getLon()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ua));
            mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
        }
    }

    @Override
    public void onClick(View v) {

        if(zone == null) {
            String codeParking = spinnerValue.getValue();
            Log.d("idParking", codeParking);
            Intent intent = new Intent().setClass(
                    getActivity(), ZonaActivity.class);
            intent.putExtra("idParking", codeParking);
            startActivity(intent);
        }else{
            Intent intent = new Intent().setClass(
                    getActivity(), OcuppyZone.class);
            intent.putExtra("zona", zone);
            intent.putExtra("desocuppy", true);
            startActivity(intent);
        }
    }

    public class LoadingTask extends AsyncTask<Void, Void, Zona> {
        private Apua apua;

        public LoadingTask(Apua apua) {
            this.apua=apua;
        }

        @Override
        protected Zona doInBackground(Void... voids) {
            List<Parking> parkings = null;
            Zona zona = null;
            try {
                parkingsList = apua.serverAgent.getParkingsFromServer();
                zone = apua.serverAgent.userOcuppyZone(usuario.getEmail());
                //Log.d("zona aparcamiento", zone.getAparcamiento());

            } catch (Exception e) {
                cancel(true);
                Log.d("UNIVERSITY", "Error trying to log. ", e);
            }
            return zona;
        }

        @Override
        protected void onPostExecute(Zona zona) {
            //parkingsList = parkings;
            loadingTask = null;
            dataAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onCancelled() {
            loadingTask = null;
            Toast.makeText(getActivity(),
                    "try mas later, an error has occured in the server",
                    Toast.LENGTH_LONG).show();

        }
    }


}
