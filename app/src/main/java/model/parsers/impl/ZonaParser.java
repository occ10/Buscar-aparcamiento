package model.parsers.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import model.entities.Zona;
import model.parsers.*;
import static model.entities.Zona.*;

/**
 * Created by walid on 04/03/2018.
 */

public class ZonaParser implements IParser<Zona>{

    private JSONObject getJsonObject(Zona model) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(ID, model.getId());
        obj.put(LON,model.getLon());
        obj.put(LAT,model.getLat());
        obj.put(APARCAMIENTO,model.getAparcamiento());
        obj.put(OCUPADA,model.getOcupada());

        return obj;
    }


    @Override
    public String fromModel(Zona model) throws JSONException {
        return getJsonObject(model).toString();
    }

    @Override
    public String fromModel(List<Zona> model) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Zona ZONA : model) {
            jsonArray.put(getJsonObject(ZONA));
        }
        return jsonArray.toString();
    }

    @Override
    public List<Zona> fromJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        List<Zona> zonas = new LinkedList<Zona>();

        for (int i = 0; i<jsonArray.length(); i++) {
            JSONObject nuevo = jsonArray.getJSONObject(i);
            Zona zona = new Zona();
            zona.setId(nuevo.getInt(ID));
            zona.setLon(nuevo.getDouble(LON));
            zona.setLat(nuevo.getDouble(LAT));
            zona.setAparcamiento(nuevo.getString(APARCAMIENTO));
            zona.setOcupada(nuevo.getInt(OCUPADA));

            zonas.add(zona);
            //Log.d("rutassssssssss tamaño",rutas.get(i).getOrigen() +" "+ rutas.get(i).getUser().getEmail() +" "+ rutas.get(i).getDetalles());
        }

        return zonas;
    }

    @Override
    public Zona getJsonObject(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        Zona zona = new Zona();
        zona.setId(obj.getInt(ID));
        zona.setLon(obj.getDouble(LON));
        zona.setLat(obj.getDouble(LAT));
        zona.setAparcamiento(obj.getString(APARCAMIENTO));
        zona.setOcupada(obj.getInt(OCUPADA));

        return zona;
    }
}
