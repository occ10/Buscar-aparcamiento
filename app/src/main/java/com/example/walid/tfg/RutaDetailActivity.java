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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import model.Apua;
import model.entities.Ruta;
import model.entities.Usuario;

import static com.example.walid.tfg.CarDetailActivity.CAR_IMAGE_PATH;
import static com.example.walid.tfg.EditUserFotoActivity.IMAGE_PATH;

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
    private ImageView routeDetaiUser;
    private ImageView  routeDetailCarImage;
    private Boolean onresumeFirstCall = false;
    private String userRoute;
    private int idRoute;
    private Apua apua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_detail);
        getSupportActionBar().setHomeButtonEnabled(true);
        apua = new Apua(this);
        Bundle extras = getIntent().getExtras();
        userRoute = extras.getString("usuario");
        idRoute = extras.getInt("ruta");

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
        routeDetaiUser = (ImageView) findViewById(R.id.routeDetaiUser);
        routeDetailCarImage = (ImageView) findViewById(R.id.routeDetailCarImage);

        if (loadingTask == null) {
            loadingTask = new LoadingTask(apua, userRoute, idRoute);
            loadingTask.execute();
        }
        final TextView lin_perfil= (TextView) findViewById(R.id.lin_perfil);
        final ImageButton showDetalle = (ImageButton) findViewById(R.id.downButton);
        lin_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onresumeFirstCall = true;
                loadingTask = null;
                Intent intent = new Intent().setClass(
                        RutaDetailActivity.this, UserPerfilActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            }
        });

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
    public void onResume()
    {
        super.onResume();
        Log.d("Call to ", "onResume");
        if (loadingTask == null) {
            loadingTask = new LoadingTask(apua, userRoute, idRoute);
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
                        RutaDetailActivity.this, SearchAnounce.class);
                startActivity(intent);
                break;
            case R.id.closeSesion:
                intent = new Intent().setClass(
                        RutaDetailActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.editPerfil:
                intent = new Intent().setClass(
                        RutaDetailActivity.this, EditPerfilActivity.class);
                startActivity(intent);
                break;
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
                usuario = ruta.getUser();
                Log.d("APPUA ruta plazas",  String.valueOf(ruta.getPlazas()));

            } catch (Exception e) {
                Log.d("APPUA", "Error trying to log. ", e);
            }
            return ruta;
        }

        @Override
        protected void onPostExecute(Ruta ruta) {
            DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

            Log.d("APPUA ruta plazas",  String.valueOf(ruta.getPlazas()));
            departureView.setText(ruta.getOrigen());
            priceRouteView.setText(String.valueOf(ruta.getPrecio()) + " €");
            placesRouteView.setText(String.valueOf(ruta.getPlazas()) + " disponibles");
            publicationDateView.setText(df.format(ruta.getFechaPublicacion()));
            routeDetailView.setText(ruta.getDetalles());
            nameUserView.setText(ruta.getUser().getNombre());
            fullNameUserView.setText(ruta.getUser().getApellido());
            ageUserView.setText(String.valueOf(ruta.getUser().getEdad()) + " años");
            carModelView.setText(ruta.getCar().getBrand());
            carCategoryView.setText("Categoria: " + ruta.getCar().getCategory());
            carColorView.setText("Color: " + ruta.getCar().getColor());
            Picasso.with(RutaDetailActivity.this).load(IMAGE_PATH + ruta.getUser().getEmail())
            .error(R.drawable.unkonwnfoto)
            .into(routeDetaiUser, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }
                @Override
                public void onError() {
                }
            });
            Picasso.with(RutaDetailActivity.this).load(CAR_IMAGE_PATH + ruta.getUser().getEmail())
                    .placeholder(R.drawable.car)
                    .error(R.drawable.car)
                    .fit().centerCrop()
                    //.resize(500,500)
                    .into(routeDetailCarImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            //showProgress(false);
                        }

                        @Override
                        public void onError() {
                            Log.d("UNIVERSITY", "no se ha podido cargar la imagen");
                            //showProgress(false);
                        }
                    });
        }
    }
}
