package com.example.walid.tfg;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.Apua;
import model.entities.Ruta;
import model.entities.Usuario;

public class AnouncesList extends AppCompatActivity {

    private RecyclerView recyclerView = null;
    AsyncTask<Void, Void, List<Ruta> > loadingTask;
    private List<Ruta> rutasList =new ArrayList<>();
    private Usuario user;
    public static final String RUTA_KEY = "ruta";
    public static final String USER = "usuario";
    private AdaptadorConIcono adaptadorConIcono;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anounces);
        getSupportActionBar().setHomeButtonEnabled(true);
        Bundle extras = getIntent().getExtras();
        //String origen = extras.getString("origen");
        //final Apua apua = new Apua(this);
        //user = extras.getParcelable("usuario");
        rutasList =  extras.getParcelableArrayList("mylist");
        Log.d("rustas list size", String.valueOf(rutasList.size()));
        RecyclerView rv = (RecyclerView) findViewById(R.id.cardList);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adaptadorConIcono = new AdaptadorConIcono();
        rv.setAdapter(adaptadorConIcono);

       /* if (loadingTask == null) {
            loadingTask = new LoadingTask(apua,origen);
            loadingTask.execute();
        }*/
    }

    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onResume(){
        super.onResume();
    }

    /*public RecyclerView getRecyclerView() {
        if (recyclerView == null) {
            synchronized (AnouncesList.class) {
                if (recyclerView == null) {
                    recyclerView = new RecyclerView(this);
                    recyclerView.setHasFixedSize(true);
                    setContentView(recyclerView);
                }
            }
        }
        return(recyclerView);
    }
    public void setAdapter(RecyclerView.Adapter adapter) {
        getRecyclerView().setAdapter(adapter);
    }
    public RecyclerView.Adapter getAdapter() {
        return(getRecyclerView().getAdapter());
    }
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        getRecyclerView().setLayoutManager(manager);
    }*/
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
                Intent intent = new Intent().setClass(AnouncesList.this, SearchAnounce.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    class AdaptadorConIcono extends RecyclerView.Adapter<FilaHolder> {

        @Override
        public FilaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return(new FilaHolder(getLayoutInflater()
                    .inflate(R.layout.activity_anounces_list, parent, false)));
        }

        @Override
        public void onBindViewHolder(final FilaHolder holder, int position) {
            holder.ruta = rutasList.get(position);
            holder.bindModel(rutasList.get(position));
            holder.mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Context context = v.getContext();
                    Intent intent = new Intent(context, RutaDetailActivity.class);
                    intent.putExtra(RUTA_KEY, holder.ruta.getId());
                    intent.putExtra(USER, holder.ruta.getUser().getEmail());
                    context.startActivity(intent);

                }
            });
        }
        @Override
        public int getItemCount() {
            return(rutasList.size());
        }

    }
    static class FilaHolder extends RecyclerView.ViewHolder {
        public Ruta ruta;
        public final View mView ;
        public final ImageView icono;
        public final String template = null;
        public final TextView cardNombreUsuario;
        public final TextView cardApellidoUsuario;
        public final TextView cardEdadUsuario;
        public final TextView cardOrigine;
        public final TextView cardPrecioPlaza;
        public final TextView cardPlazasDisponible;

        FilaHolder(View fila) {
            super(fila);
            mView = fila;
            cardNombreUsuario = (TextView)fila.findViewById(R.id.cardNombreUsuario);
            cardApellidoUsuario = (TextView)fila.findViewById(R.id.cardApellidoUsuario);
            cardEdadUsuario = (TextView)fila.findViewById(R.id.cardEdadUsuario);
            cardOrigine = (TextView)fila.findViewById(R.id.cardOrigine);
            cardPrecioPlaza = (TextView)fila.findViewById(R.id.cardPrecioPlaza);
            cardPlazasDisponible = (TextView)fila.findViewById(R.id.cardPlazasDisponible);
            icono = (ImageView)fila.findViewById(R.id.icono);
            //template = cardEdadUsuario.getContext()
                    //.getString(R.string.tamanyo_template);
        }
        private void bindModel(Ruta route) {
            cardNombreUsuario.setText(route.getUser().getNombre());
            cardApellidoUsuario.setText(route.getUser().getApellido());
            cardEdadUsuario.setText("Edad: " + String.valueOf(route.getUser().getEdad()));
            cardOrigine.setText("Salida:" + route.getOrigen());
            cardPrecioPlaza.setText("Precio:" + String.valueOf(route.getPrecio()));
            cardPlazasDisponible.setText("Plazas disponibles:" + String.valueOf(route.getPlazas() - route.getPlazasOcupadas()));
            //cardApellidoUsuario.setText(String.format(template, item.length()));
            icono.setImageResource(R.drawable.unkonwnfoto);

        }
    }

}
