package model.parsers.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import model.entities.Car;
import model.parsers.IParser;

import static model.entities.Car.*;

/**
 * Created by walid on 07/04/2018.
 */

public class CarParser implements IParser<Car> {


    private JSONObject getJsonObject(Car model) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(REGISTRATION, model.getRegistration());
        obj.put(MODEL,model.getModel());
        obj.put(COLOR,model.getColor());
        obj.put(SEATING,model.getSeating());
        obj.put(CARUSER,model.getUser());
        obj.put(BRAND,model.getBrand());
        obj.put(CATEGORY,model.getCategory());
        obj.put(IMAGE,model.getImage());
        return obj;
    }

    @Override
    public String fromModel(Car model) throws JSONException {
        return getJsonObject(model).toString();
    }

    @Override
    public String fromModel(List<Car> model) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Car CAR : model) {
            jsonArray.put(getJsonObject(CAR));
        }
        return jsonArray.toString();
    }

    @Override
    public List<Car> fromJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        List<Car> cars = new LinkedList<Car>();

        for (int i = 0; i<jsonArray.length(); i++) {
            JSONObject nuevo = jsonArray.getJSONObject(i);
            Car car = new Car();
            car.setRegistration(nuevo.getString(REGISTRATION));
            car.setModel(nuevo.getString(MODEL));
            car.setColor(nuevo.getString(COLOR));
            car.setSeating(nuevo.getInt(SEATING));
            car.setColor(nuevo.getString(CARUSER));
            car.setUser(nuevo.getString(BRAND));
            car.setCategory(nuevo.getString(CATEGORY));
            car.setImage(nuevo.getString(IMAGE));

            cars.add(car);
            //Log.d("rutassssssssss tamaño",rutas.get(i).getOrigen() +" "+ rutas.get(i).getUser().getEmail() +" "+ rutas.get(i).getDetalles());
        }

        return cars;
    }

    @Override
    public Car getJsonObject(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        Car car = new Car();

        car.setRegistration(obj.getString(REGISTRATION));
        car.setModel(obj.getString(MODEL));
        car.setColor(obj.getString(COLOR));
        car.setSeating(obj.getInt(SEATING));
        car.setColor(obj.getString(CARUSER));
        car.setColor(obj.getString(BRAND));
        car.setCategory(obj.getString(CATEGORY));
        car.setColor(obj.getString(IMAGE));

        return car;
    }
}
