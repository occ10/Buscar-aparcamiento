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

import model.entities.Car;
import model.entities.Ruta;

import model.entities.Usuario;
import model.parsers.*;

import static model.entities.Car.*;
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
        obj.put(CAR, model.getCar());

        return obj;
    }

    @Override
    public Ruta getJsonObject(String json) throws JSONException {


        JSONObject obj = new JSONObject(json) ;

        obj = obj.getString("route").equals("null") ? new JSONObject(json) : new JSONObject(obj.getString("route"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Ruta ruta = new Ruta();
        Usuario usuario = new Usuario();
        Car car = new Car();

        try {
            date = (Date) formatter.parse(obj.getString(FECHAPUBLICACION));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ruta.setId(obj.getInt(ID));
        ruta.setPlazas(obj.getInt(PLAZAS));
        ruta.setPlazasOcupadas(obj.getInt(PLAZASOCUPADAS));
        ruta.setOrigen(obj.getString(ORIGEN));
        ruta.setDetalles(obj.getString(DETALLES));
        ruta.setPrecio(obj.getDouble(PRECIO));
        ruta.setFechaPublicacion(date);
        ruta.setOpcion(obj.getInt(OPCION));

        if (!obj.getString(USER).equals("null")) {
            JSONObject obj2 = new JSONObject(obj.getString(USER));
            usuario.setEmail(obj2.getString(EMAIL));
            usuario.setNombre(obj2.getString(NOMBRE));
            usuario.setApellido(obj2.getString(APELLIDO));
            usuario.setEdad(obj2.getInt(EDAD));
            usuario.setTelefono(obj2.getString(TELEFONO));
            usuario.setDescripcion(obj2.getString(DESCRIPCION));
            usuario.setFoto(obj2.getString(FOTO));

        }
        ruta.setUser(usuario);
        if(!obj.getString(CAR).equals("null")){
            JSONObject obj3 = new JSONObject(obj.getString(CAR));
            car.setRegistration(obj3.getString(REGISTRATION));
            car.setModel(obj3.getString(MODEL));
            car.setColor(obj3.getString(COLOR));
            car.setSeating(obj3.getInt(SEATING));
            car.setUser(obj3.getString(CARUSER));
            car.setBrand(obj3.getString(BRAND));
            car.setCategory(obj3.getString(CATEGORY));
            car.setImage(obj3.getString(IMAGE));

        }
        ruta.setCar(car);

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
        Ruta ruta = null;
        Usuario usuario = null;
        Car car = null;
        for (int i = 0; i<jsonArray.length(); i++) {
            JSONObject nuevo = jsonArray.getJSONObject(i);
            ruta = new Ruta();
            usuario = new Usuario();
            car = new Car();
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

            if (!nuevo.getString(USER).equals("null")) {
                JSONObject obj2 = new JSONObject(nuevo.getString(USER));
                usuario.setEmail(obj2.getString(EMAIL));
                usuario.setNombre(obj2.getString(NOMBRE));
                usuario.setApellido(obj2.getString(APELLIDO));
                usuario.setEdad(obj2.getInt(EDAD));
                usuario.setTelefono(obj2.getString(TELEFONO));
                usuario.setDescripcion(obj2.getString(DESCRIPCION));
                usuario.setFoto(obj2.getString(FOTO));
            }
            ruta.setUser(usuario);

            if(!nuevo.getString(CAR).equals("null")){
                Log.d("Apua jsong check car", nuevo.getString(CAR));
                JSONObject obj3 = new JSONObject(nuevo.getString(CAR));

                car.setRegistration(obj3.getString(REGISTRATION));
                car.setModel(obj3.getString(MODEL));
                car.setColor(obj3.getString(COLOR));
                car.setSeating(obj3.getInt(SEATING));
                car.setUser(obj3.getString(CARUSER));
                car.setBrand(obj3.getString(BRAND));
                car.setCategory(obj3.getString(CATEGORY));
                car.setImage(obj3.getString(IMAGE));
            }
            ruta.setCar(car);

            rutas.add(ruta);
            //Log.d("rutassssssssss tamaño",rutas.get(i).getOrigen() +" "+ rutas.get(i).getUser().getEmail() +" "+ rutas.get(i).getDetalles());
        }

        return rutas;
    }


}
