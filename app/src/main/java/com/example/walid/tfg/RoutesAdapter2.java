package com.example.walid.tfg;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.entities.Ruta;


public class RoutesAdapter2 extends RecyclerView.Adapter<RoutesAdapter2.ViewHolder>{
    private List<Ruta> rutasList = new ArrayList<>();

    private static final String[] items = {"En", "un", "lugar", "de",
            "la", "Mancha", "de", "cuyo", "nombre", "no", "quiero",
            "acordarme", "no", "ha", "mucho", "tiempo", "que",
            "vivía", "un", "hidalgo", "de", "los", "de", "lanza",
            "en", "astillero", "adarga", "antigua", "rocín", "flaco",
            "y", "galgo", "corredor"};


    public RoutesAdapter2() {
        this.rutasList=null;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        //holder.mItem = mValues.get(position);
        holder.nombreUsuario.setText(rutasList.get(position).getUser().getNombre());
        holder.apellidoUsuario.setText(rutasList.get(position).getUser().getApellido());
        holder.edad.setText(rutasList.get(position).getUser().getEdad());
        holder.origen.setText(rutasList.get(position).getOrigen());
        holder.precioPlaza.setText(String.valueOf(rutasList.get(position).getPrecio()));
        holder.plazasDisponible.setText(rutasList.get(position).getPlazas()- rutasList.get(position).getPlazasOcupadas());
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
