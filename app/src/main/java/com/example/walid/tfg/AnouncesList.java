package com.example.walid.tfg;

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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.Apua;
import model.entities.Ruta;

public class AnouncesList extends AppCompatActivity {

    private RecyclerView recyclerView = null;
    AsyncTask<Void, Void, List<Ruta> > loadingTask;
    private static final String[] items = {"En", "un", "lugar", "de",
            "la", "Mancha", "de", "cuyo", "nombre", "no", "quiero",
            "acordarme", "no", "ha", "mucho", "tiempo", "que",
            "vivía", "un", "hidalgo", "de", "los", "de", "lanza",
            "en", "astillero", "adarga", "antigua", "rocín", "flaco",
            "y", "galgo", "corredor"};
    private List<Ruta> rutasList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutManager(new LinearLayoutManager(this));
        setAdapter(new AdaptadorConIcono());
        getSupportActionBar().setHomeButtonEnabled(true);
        Bundle extras = getIntent().getExtras();
        String origen = extras.getString("origen");
        final Apua apua = new Apua(this);
        if (loadingTask == null) {
            loadingTask = new LoadingTask(apua,origen);
            loadingTask.execute();
        }

    }

    public RecyclerView getRecyclerView() {
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
        public void onBindViewHolder(FilaHolder holder, int position) {
            holder.bindModel(items[position]);
        }
        @Override
        public int getItemCount() {
            return(items.length);
        }

    }
    static class FilaHolder extends RecyclerView.ViewHolder {
        TextView etiqueta = null;
        TextView tamanyo = null;
        ImageView icono = null;
        String template = null;
        FilaHolder(View fila) {
            super(fila);
            etiqueta = (TextView)fila.findViewById(R.id.etiqueta);
            tamanyo = (TextView)fila.findViewById(R.id.tamanyo);
            icono = (ImageView)fila.findViewById(R.id.icono);
            template = tamanyo.getContext()
                    .getString(R.string.tamanyo_template);
        }
        private void bindModel(String item) {
            etiqueta.setText(item);
            tamanyo.setText(String.format(template, item.length()));
            if (item.length()>4) {
                icono.setImageResource(R.drawable.unkonwnfoto);
            }

            else {
                icono.setImageResource(R.drawable.unkonwnfoto);
            }
        }
    }

    public class LoadingTask extends AsyncTask<Void, Void, List<Ruta>> {
        private Apua apua;
        private String origen;

        public LoadingTask(Apua apua,String origen) {
            this.apua=apua;
            this.origen = origen;
        }

        @Override
        protected List<Ruta> doInBackground(Void... voids) {
            List<Ruta> rutas = null;
            try {
                //rutas = apua.serverAgent.getRutasOriginFromServer(usuario);

            } catch (Exception e) {
                Log.d("RUNNER", "Error trying to log. ", e);
            }
            return rutas;
        }

        @Override
        protected void onPostExecute(List<Ruta> rutas) {
            //rutasList = rutas;
            //routesAdapter.notifyDataSetChanged();
        }
    }

}
