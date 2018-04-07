package com.example.walid.tfg;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import model.Apua;
import model.entities.Ruta;
import model.entities.Usuario;

public class RutaDetailActivity extends AppCompatActivity {
    Boolean QuickFactsExpanded = true;
    AsyncTask<Void, Void, Ruta > loadingTask;
    private Usuario usuario = null;
    private TextView departureView;
    private TextView priceRouteView;
    private TextView placesRouteView;
    private TextView publicationDateView;
    private TextView routeDetailView;
    private TextView nameUserView;
    private TextView fullNameUserView;
    private TextView ageUserView;
    private TextView carModelView;
    private TextView carCategoryView;
    private TextView carColorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_detail);
        getSupportActionBar().setHomeButtonEnabled(true);
        final Apua apua = new Apua(this);
        Bundle extras = getIntent().getExtras();
        String userRoute = extras.getString("usuario");
        int idRoute = extras.getInt("ruta");

        departureView = (TextView) findViewById(R.id.departureText);
        priceRouteView = (TextView) findViewById(R.id.priceRouteText);;
        placesRouteView = (TextView) findViewById(R.id.placesRouteText);;
        publicationDateView = (TextView) findViewById(R.id.publicationDateText);
        routeDetailView = (TextView) findViewById(R.id.routeDetail);
        nameUserView = (TextView) findViewById(R.id.nameUserText);
        fullNameUserView = (TextView) findViewById(R.id.fullNameUserText);
        ageUserView = (TextView) findViewById(R.id.ageUserText);
        carModelView = (TextView) findViewById(R.id.carModel);
        carCategoryView = (TextView) findViewById(R.id.carCategory);
        carColorView = (TextView) findViewById(R.id.carColor);

        if (loadingTask == null) {
            loadingTask = new LoadingTask(apua, userRoute, idRoute);
            loadingTask.execute();
        }

        final ImageButton showDetalle = (ImageButton) findViewById(R.id.downButton);

        showDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView routeDetal = (TextView) findViewById(R.id.routeDetail);

                if(QuickFactsExpanded) {
                    QuickFactsExpanded = false;
                    routeDetal.setVisibility(View.VISIBLE);
                    showDetalle.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }else{
                    QuickFactsExpanded = true;
                    routeDetal.setVisibility(View.GONE);
                    showDetalle.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });
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
                this.finish();
                return true;
            case R.id.menu_buscar:
                Intent intent = new Intent().setClass(
                        RutaDetailActivity.this, SearchAnounce.class);
                startActivity(intent);
                break;
            /*case R.id.acercaDe:
                //lanzarAcercaDe();
                break;*/
        }
        return true;
    }
    public class LoadingTask extends AsyncTask<Void, Void, Ruta> {
        private Apua apua;
        private String userRoute;
        private int idRoute;


        public LoadingTask(Apua apua, String userRoute, int idRoute) {
            this.apua=apua;
            this.userRoute = userRoute;
            this.idRoute = idRoute;
        }
    @Override
    protected Ruta doInBackground(Void... voids) {
        Ruta ruta = null;
        try {
            ruta = apua.serverAgent.getAnnouncmentFromServer(userRoute, idRoute);
            Log.d("APPUA ruta plazas",  String.valueOf(ruta.getPlazas()));

        } catch (Exception e) {
            Log.d("APPUA", "Error trying to log. ", e);
        }
        return ruta;
    }

    @Override
    protected void onPostExecute(Ruta ruta) {
        Log.d("APPUA ruta plazas",  String.valueOf(ruta.getPlazas()));
        departureView.setText(ruta.getOrigen());
        priceRouteView.setText(String.valueOf(ruta.getPrecio()) + " €");
        placesRouteView.setText(String.valueOf(ruta.getPlazas()) + " disponibles");
        publicationDateView.setText(String.valueOf(ruta.getFechaPublicacion()));
        routeDetailView.setText(ruta.getDetalles());
        nameUserView.setText(ruta.getUser().getNombre());
        fullNameUserView.setText(ruta.getUser().getApellido());
        ageUserView.setText(String.valueOf(ruta.getUser().getEdad()) + "años");
        carModelView.setText(ruta.getCar().getBrand());
        carCategoryView.setText("Categoria: " + ruta.getCar().getCategory());
        carColorView.setText("Color: " + ruta.getCar().getColor());
    }
}
}
