package server;

import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.entities.Parking;
import model.entities.Ruta;
import model.entities.Usuario;
import model.entities.Zona;
import model.parsers.*;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by walid on 27/01/2018.
 */

public class ServerAgent {

    public static final String USUARIOS_PATH = "http://10.0.2.2:8080/tfg/rest/UserService/users";
    public static final String ZONAS_PATH = "http://10.0.2.2:8080/tfg/rest/ZonaService/zonas";
    public static final String LOGIN_PATH = "http://10.0.2.2:8080/tfg/rest/UserService/user";
    public static final String USUARIO_PATH = "http://10.0.2.2:8080/tfg/rest/UserService/userByMail";
    public static final String REGISTER_PATH = "http://10.0.2.2:8080/tfg/rest/UserService/insert";
    public static final String RUTAS_PATH = "http://10.0.2.2:8080/tfg/rest/RutaService/routes";
    public static final String PARKINGS_PATH = "http://10.0.2.2:8080/tfg/rest/ParkingService/parking";

    private Context context;
    private RestHelper restHelper;


    public ServerAgent(Context context) {
        this.context = context;
        this.restHelper = new RestHelper();
    }

    public List<Ruta> getRutasFromServer(Usuario usuario) throws IOException, NetworkException, JSONException {

        RestResponse response =  getResponseFromServer(RUTAS_PATH + "/" + usuario.getEmail());

        IParser parser = ParserFactory.newInstance()
                .getRutaParser();

        String httpContent = response.getHttpContent();
        Log.d("Apua jsong", httpContent);
        return parser.fromJson(httpContent);
    }

    public List<Zona> getZonesFromServer(String code) throws IOException, NetworkException, JSONException {
        Log.d("Zonasss path --------------------", ZONAS_PATH + "/" + code);
        RestResponse response =  getResponseFromServer(ZONAS_PATH + "/" + code);
        IParser parser = ParserFactory.newInstance().getZonaParser();
        String httpContent = response.getHttpContent();
        //Log.d("Parkings result --------------------", ZONAS_PATH + "/" + code);
        return parser.fromJson(httpContent);
    }

    public List<Parking> getParkingsFromServer() throws IOException, NetworkException, JSONException {
        RestResponse response =  getResponseFromServer(PARKINGS_PATH);
        IParser parser = ParserFactory.newInstance().getParkingParser();
        String httpContent = response.getHttpContent();
        //Log.d("Parkings result --------------------", httpContent);
        return parser.fromJson(httpContent);
    }

    public Usuario getUser(String email) throws IOException, NetworkException, JSONException {


        Usuario usuario = new Usuario();

        RestResponse response = restHelper.get(context, USUARIO_PATH+"/"+email, null);


        Log.d("Apua jsong", USUARIO_PATH);

             if (response.getHttpResponseCode() != 200) {
                throw new NetworkException(new StringBuilder()
                    .append("HttpCode: ")
                    .append(response.getHttpResponseCode())
                    .append(" - ")
                    .append(response.getHttpContent())
                    .toString());
        }
        IParser parser = ParserFactory.newInstance()
                .getUsuarioParser();

        String httpContent = response.getHttpContent();
        Log.d("Apua jsong", httpContent);
        return (Usuario) parser.getJsonObject(httpContent);
        //return usuario;
    }

   /* public List<Opinion> getOpinionesFromServer(int id) throws IOException, NetworkException, JSONException {
        // TODO
        RestResponse response = getResponseFromServer(OPINIONES_PATH + id);
        IParser parser = ParserFactory.newInstance().getOpinionParser();
        String httpContent = response.getHttpContent();
        return parser.fromJson(httpContent);
    }*/

    public boolean loginUsuario(String email, String password) throws IOException, NetworkException {
        // TODO
        /*Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("correo", email)
                .appendQueryParameter("contraseña", password);
        String query = builder.build().getEncodedQuery();*/

        JSONObject json = new JSONObject();
        try {
            json.put("correo", email);
            json.put("contraseña", password);
        }catch(JSONException e){

        }

        Map<String, String> headers= new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Content-type", "application/json");

        RestResponse response = restHelper.getUser(context, LOGIN_PATH, headers, json.toString());
        //Log.d("Apua jsong", json.toString());
        if (response.getHttpResponseCode() == 401) {
            return false;
        } else  if (response.getHttpResponseCode() != 200) {
            throw new NetworkException(new StringBuilder()
                    .append("HttpCode: ")
                    .append(response.getHttpResponseCode())
                    .append(" - ")
                    .append(response.getHttpContent())
                    .toString());
        }
        return true;
    }

    public RestResponse getResponseFromServer(String path) throws IOException, NetworkException {
        RestResponse response = restHelper.get(context, path, null);
        if (response.getHttpResponseCode() != 200) {
            throw new NetworkException(new StringBuilder()
                    .append("HttpCode: ")
                    .append(response.getHttpResponseCode())
                    .append(" - ")
                    .append(response.getHttpContent())
                    .toString());
        }
        return response;
    }

    public boolean registryUsuario(String nombre, String apellido, String email, String password, String telefono, String edad, String descripcion) throws IOException, NetworkException {
        // TODO
        /*Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("correo", email)
                .appendQueryParameter("contraseña", password);
        String query = builder.build().getEncodedQuery();*/

        Log.d("nombre",nombre);
        JSONObject json = new JSONObject();
        try {
            json.put("correo", email);
            json.put("contraseña", password);
            json.put("nombre", nombre);
            json.put("apellido", password);
            json.put("telefono", telefono);
            json.put("detalles", descripcion);
            json.put("edad", edad);
        }catch(JSONException e){

        }

        Map<String, String> headers= new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Content-type", "application/json");

        RestResponse response = restHelper.SetUser(context, REGISTER_PATH, headers, json.toString());
        //Log.d("Apua jsong", json.toString());
        if (response.getHttpResponseCode() == 401) {
            return false;
        } else  if (response.getHttpResponseCode() != 201) {
            throw new NetworkException(new StringBuilder()
                    .append("HttpCode: ")
                    .append(response.getHttpResponseCode())
                    .append(" - ")
                    .append(response.getHttpContent())
                    .toString());
        }
        return true;
    }
}
