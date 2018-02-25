package model.parsers.impl;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import model.entities.Ruta;

import model.entities.Usuario;
import model.parsers.*;

import static model.entities.Ruta.*;
import static model.entities.Usuario.*;
/**
 * Created by walid on 18/02/2018.
 */

public class RutaParser implements IParser<Ruta>{


    private JSONObject getJsonObject(Ruta model) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(ID, model.getId());
        obj.put(PLAZAS, model.getPlazas());
        obj.put(PLAZASOCUPADAS, model.getPlazasOcupadas());
        obj.put(ORIGEN, model.getOrigen());
        //obj.put(PASSWORD, model.getPassword());
        obj.put(DETALLES, model.getDetalles());
        obj.put(FECHAPUBLICACION, model.getFechaPublicacion());
        obj.put(OPCION, model.getOpcion());
        obj.put(USER, model.getUser());

        return obj;
    }

    @Override
    public Ruta getJsonObject(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
        Date date = null;
        try {
            date = formatter.parse(obj.getString(FECHAPUBLICACION));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Ruta ruta = new Ruta();
        Usuario usuario = new Usuario();
        ruta.setId(obj.getInt(ID));
        ruta.setPlazas(obj.getInt(PLAZAS));
        ruta.setPlazasOcupadas(obj.getInt(PLAZASOCUPADAS));
        ruta.setOrigen(obj.getString(ORIGEN));
        ruta.setDetalles(obj.getString(DETALLES));
        ruta.setPrecio(obj.getDouble(PRECIO));
        ruta.setFechaPublicacion(date);
        ruta.setOpcion(obj.getInt(OPCION));
        JSONObject obj2 = new JSONObject(obj.getString(USER));
        obj2.getString(EMAIL);

        usuario.setNombre(obj2.getString(NOMBRE));
        usuario.setApellido(obj2.getString(APELLIDO));
        usuario.setEdad(obj2.getInt(EDAD));
        usuario.setTelefono(obj2.getString(TELEFONO));
        usuario.setDescripcion(obj2.getString(DESCRIPCION));
        usuario.setFoto(obj2.getString(FOTO));

        ruta.setUser(usuario);
        return ruta;
    }
    @Override
    public String fromModel(Ruta model) throws JSONException {
        return getJsonObject(model).toString();
    }

    @Override
    public String fromModel(List<Ruta> model) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Ruta RUTA : model) {
            jsonArray.put(getJsonObject(RUTA));
        }
        return jsonArray.toString();
    }

    @Override
    public List<Ruta> fromJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        List<Ruta> rutas = new LinkedList<Ruta>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        for (int i = 0; i<jsonArray.length(); i++) {
            JSONObject nuevo = jsonArray.getJSONObject(i);

            Ruta ruta = new Ruta();
            Usuario usuario = new Usuario();
            ruta.setId(nuevo.getInt(ID));
            ruta.setPlazas(nuevo.getInt(PLAZAS));
            ruta.setPlazasOcupadas(nuevo.getInt(PLAZASOCUPADAS));
            ruta.setOrigen(nuevo.getString(ORIGEN));
            ruta.setDetalles(nuevo.getString(DETALLES));
            ruta.setPrecio(nuevo.getDouble(PRECIO));

            try {

                date = formatter.parse(nuevo.getString(FECHAPUBLICACION));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ruta.setFechaPublicacion(date);
            ruta.setOpcion(nuevo.getInt(OPCION));
            JSONObject obj2 = new JSONObject(nuevo.getString(USER));
            usuario.setEmail(obj2.getString(EMAIL));
            usuario.setNombre(obj2.getString(NOMBRE));
            usuario.setApellido(obj2.getString(APELLIDO));
            usuario.setEdad(obj2.getInt(EDAD));
            usuario.setTelefono(obj2.getString(TELEFONO));
            usuario.setDescripcion(obj2.getString(DESCRIPCION));
            usuario.setFoto(obj2.getString(FOTO));

            ruta.setUser(usuario);

            rutas.add(ruta);
            //Log.d("rutassssssssss tamaño",rutas.get(i).getOrigen() +" "+ rutas.get(i).getUser().getEmail() +" "+ rutas.get(i).getDetalles());
        }

        return rutas;
    }


}
