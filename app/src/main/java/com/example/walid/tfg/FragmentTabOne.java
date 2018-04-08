package com.example.walid.tfg;

/**
 * Created by walid on 20/01/2018.
 */

import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import model.Apua;
import model.entities.Ruta;
import model.entities.Usuario;

public class FragmentTabOne extends Fragment {
    //ListView   listView ;
    //private TextView selection;
    AsyncTask<Void, Void, List<Ruta> >loadingTask;
    private List<Ruta> rutasList = new ArrayList<>();
    RecyclerView recycleview;
    private RoutesAdapter routesAdapter;
    public static final String RUTA_KEY = "ruta";
    public static final String USER = "usuario";
    Usuario usuario = null;
    private Apua apua;
    private Boolean onresumeCall = false;
            // Session Manager Class
    SessionManager session;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Session class instance
        //session = new SessionManager(getApplicationContext());
        Bundle extras = getActivity().getIntent().getExtras();
        usuario = extras.getParcelable("usuario");

        Log.d("Usuario apellido", usuario.getApellido());

        apua =  new Apua(getActivity());

                    /*((MyAppContext)getApplicationContext()
                    .getApplicationContext())
                    .getApuaInstance();*/
        if (loadingTask == null) {
            loadingTask = new LoadingTask(apua, usuario);
            loadingTask.execute();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        //Log.d("rutas tamaño1", rutasList.toString());
        //routesAdapter = new RoutesAdapter();
        // Set the adapter
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_item_list, container, false);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        routesAdapter = new RoutesAdapter(getActivity());
        rv.setAdapter(routesAdapter);
        return rv;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("Call to ", "onResume");
        //mIsSpinnerFirstCall = true;
        //apua =  new Apua(getActivity());

        if (loadingTask == null && onresumeCall) {
            loadingTask = new LoadingTask(apua,usuario);
            loadingTask.execute();
        }
    }

    public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder>{
        public RoutesAdapter(Context context) {
        }
        // private List<Ruta> rutasList = new ArrayList<>();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Log.d("APPUA position ",  rutasList.get(position).getUser().getNombre());
            holder.ruta = rutasList.get(position);
            holder.nombreUsuario.setText(rutasList.get(position).getUser().getNombre());
            holder.apellidoUsuario.setText(rutasList.get(position).getUser().getApellido());
            holder.edad.setText("Edad: " + Integer.toString(rutasList.get(position).getUser().getEdad()));
            holder.origen.setText("Salida: " + rutasList.get(position).getOrigen());
            holder.precioPlaza.setText("Precio: " + String.valueOf(rutasList.get(position).getPrecio()));
            holder.plazasDisponible.setText("Plazas disponibles " +  Integer.toString(rutasList.get(position).getPlazas()- rutasList.get(position).getPlazasOcupadas()));
            holder.mView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onresumeCall = true;
                        /*session.checkLogin();*/
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
            return rutasList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public Ruta ruta;
            public final View mView;
            public final TextView nombreUsuario;
            public final TextView apellidoUsuario;
            public final TextView edad;
            public final TextView origen;
            public final TextView precioPlaza;
            public final TextView plazasDisponible;

            public ViewHolder(View view){

                super(view);
                mView = view;
                nombreUsuario = (TextView) view.findViewById(R.id.nombreUsuario);
                apellidoUsuario = (TextView) view.findViewById(R.id.apellidoUsuario);
                edad = (TextView) view.findViewById(R.id.edadUsuario);
                origen = (TextView) view.findViewById(R.id.origine);
                precioPlaza = (TextView) view.findViewById(R.id.precioPlaza);
                plazasDisponible = (TextView) view.findViewById(R.id.plazasDisponible);

            }
        }
    }

    public class LoadingTask extends AsyncTask<Void, Void, List<Ruta>> {
        private Apua apua;
        public Usuario usuario;

        public LoadingTask(Apua apua,Usuario usuario) {
            this.apua=apua;
            this.usuario = usuario;
        }

        @Override
        protected List<Ruta> doInBackground(Void... voids) {
            List<Ruta> rutas = null;
            try {
                rutas = apua.serverAgent.getRutasFromServer(usuario);
                //Log.d("APPUA ruta plazas",  String.valueOf(rutasList.size()));

            } catch (Exception e) {
                Log.d("APPUA", "Error trying to get Routes. ", e);
            }
            return rutas;
        }

        @Override
        protected void onPostExecute(List<Ruta> rutas) {
            FragmentTabOne.this.rutasList = rutas;
            routesAdapter.notifyDataSetChanged();
        }
    }

}

        /*if (view instanceof RecyclerView) {
            Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new RoutesAdapter(getActivity()));
        }*/
       /* listView = (ListView ) v.findViewById(R.id.list);

        TextView textview = (TextView ) v.findViewById(R.id.empty);
        // Obtener el ListView
        FrameLayout frameLayout =
                v.findViewById(R.id.frame);
        if (items.length != 0) {
            listView.setVisibility(View.VISIBLE);
            frameLayout.getChildAt(1).setVisibility(View.INVISIBLE);
        }
        else {
            listView.setVisibility(View.INVISIBLE);
            frameLayout.getChildAt(1).setVisibility(View.VISIBLE);
            textview.setText(this.getTag() + " Content 1");

        }


        ListView listView = (ListView)frameLayout.getChildAt(0);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1,
                        items);


        listView.setAdapter(arrayAdapter);*/
//listView.setOnItemSelectedListener(getActivity());
//selection = (TextView) v.findViewById(R.id.elemento_seleccionado);
        /*TextView tv = (TextView) v.findViewById(R.id.text);
        tv.setText(this.getTag() + " Content 1");*/
//return view;