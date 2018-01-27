package server;

import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.entities.Usuario;
import android.net.Uri;

/**
 * Created by walid on 27/01/2018.
 */

public class ServerAgent {

    public static final String USUARIOS_PATH = "http://10.0.2.2:8080/tfg/rest/UserService/users";
   // public static final String PUNTOS_PATH = "http://164.132.41.42:443/api/punto/devolverPuntos?idRuta=";
    public static final String LOGIN_PATH = "http://10.0.2.2:8080/tfg/rest/UserService/user";
    //public static final String OPINIONES_PATH = "http://sca.ovh:443/api/opinion/devolverOpinionesRuta?idRuta=";

    private Context context;
    private RestHelper restHelper;


    public ServerAgent(Context context) {
        this.context = context;
        this.restHelper = new RestHelper();
    }

    /*public List<Ruta> getRutasFromServer() throws IOException, NetworkException, JSONException {
        RestResponse response =  getResponseFromServer(RUTAS_PATH);
        IParser parser = ParserFactory.newInstance()
                .getRutaParser();

        String httpContent = response.getHttpContent();
        return parser.fromJson(httpContent);
    }

    public List<Punto> getPuntosFromServer(int id) throws IOException, NetworkException, JSONException {
        RestResponse response =  getResponseFromServer(PUNTOS_PATH+id);
        IParser parser = ParserFactory.newInstance().getPuntoParser();
        String httpContent = response.getHttpContent();
        return parser.fromJson(httpContent);
    }*/

    public Usuario getUser(String email) {
        Usuario usuario = new Usuario();
        usuario.setEmail("javi");
        usuario.setNombre("nombre");
        return usuario;
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
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("correo", email)
                .appendQueryParameter("contraseña", password);
        String query = builder.build().getEncodedQuery();
        Map<String, String> headers= new HashMap<String, String>();
        //headers.put("Accept", "application/json");
        headers.put("Content-type", "application/json");
        RestResponse response = restHelper.post(context, LOGIN_PATH, headers, query);
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
}
