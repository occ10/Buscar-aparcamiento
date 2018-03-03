package model.parsers.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import model.parsers.*;
import model.entities.Parking;

import static model.entities.Parking.*;
import model.entities.Parking;
/**
 * Created by walid on 03/03/2018.
 */

public class ParkingParser implements IParser<Parking>{

    private JSONObject getJsonObject(Parking model) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(ID, model.getId());
        obj.put(CODIGO,model.getCodigo());
        obj.put(IDPARKING,model.getIdparking());
        obj.put(PLAZAS,model.getPlazas());
        obj.put(SUPERFICIE,model.getSuperficie());
        obj.put(LON,model.getLon());
        obj.put(LAT,model.getLat());
        return obj;
    }


    @Override
    public String fromModel(Parking model) throws JSONException {
        return getJsonObject(model).toString();
    }

    @Override
    public String fromModel(List<Parking> model) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Parking PARKING : model) {
            jsonArray.put(getJsonObject(PARKING));
        }
        return jsonArray.toString();
    }

    @Override
    public List<Parking> fromJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        List<Parking> parkings = new LinkedList<Parking>();

        for (int i = 0; i<jsonArray.length(); i++) {
            JSONObject nuevo = jsonArray.getJSONObject(i);
            Parking parking = new Parking();
            parking.setId(nuevo.getInt(ID));
            parking.setCodigo(nuevo.getString(CODIGO));
            parking.setIdparking(nuevo.getInt(IDPARKING));
            parking.setPlazas(nuevo.getInt(PLAZAS));
            parking.setSuperficie(nuevo.getDouble(SUPERFICIE));
            parking.setLon(nuevo.getDouble(LON));
            parking.setLat(nuevo.getDouble(LAT));

            parkings.add(parking);
            //Log.d("rutassssssssss tamaño",rutas.get(i).getOrigen() +" "+ rutas.get(i).getUser().getEmail() +" "+ rutas.get(i).getDetalles());
        }

        return parkings;
    }

    @Override
    public Parking getJsonObject(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        Parking parking = new Parking();

        parking.setId(obj.getInt(ID));
        parking.setCodigo(obj.getString(CODIGO));
        parking.setIdparking(obj.getInt(IDPARKING));
        parking.setPlazas(obj.getInt(PLAZAS));
        parking.setLon(obj.getDouble(LON));
        parking.setLat(obj.getDouble(LAT));

        return parking;
    }
}
