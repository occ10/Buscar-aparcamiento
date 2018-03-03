package model.parsers.impl;

/**
 * Created by walid on 27/01/2018.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import model.entities.Usuario;
import model.parsers.*;

import static model.entities.Usuario.*;

public class UsuarioParser implements IParser<Usuario>{

    private JSONObject getJsonObject(Usuario model) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(EMAIL, model.getEmail());
        obj.put(NOMBRE, model.getNombre());
        obj.put(APELLIDO, model.getApellido());
        obj.put(EDAD, model.getEdad());
        //obj.put(PASSWORD, model.getPassword());
        obj.put(DESCRIPCION, model.getDescripcion());
        obj.put(TELEFONO, model.getTelefono());
        obj.put(CONFIRMADO, model.getConfirmado());
        return obj;
    }
    @Override
    public Usuario getJsonObject(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        Usuario usuario = new Usuario();
        usuario.setEmail(obj.getString(EMAIL));
        usuario.setNombre(obj.getString(NOMBRE));
        usuario.setApellido(obj.getString(APELLIDO));
        usuario.setEdad(Integer.valueOf(obj.getString(EDAD)));
        //usuario.setPassword(obj.getString(PASSWORD));
        usuario.setDescripcion(obj.getString(DESCRIPCION));
        usuario.setTelefono(obj.getString(TELEFONO));
        usuario.setConfirmado(obj.getString(CONFIRMADO));
        return usuario;
    }

    @Override
    public String fromModel(Usuario model) throws JSONException {
        return getJsonObject(model).toString();
    }

    @Override
    public String fromModel(List<Usuario> model) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Usuario usuario : model) {
            jsonArray.put(getJsonObject(usuario));
        }
        return jsonArray.toString();
    }

    @Override
    public List<Usuario> fromJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        List<Usuario> usuarios = new LinkedList<Usuario>();
        for (int i = 0; i<jsonArray.length(); i++) {
            JSONObject nuevo = jsonArray.getJSONObject(i);
            Usuario usuario = new Usuario();
            usuario.setEmail(nuevo.getString(EMAIL));
            usuario.setNombre(nuevo.getString(NOMBRE));

            usuarios.add(usuario);
        }
        return usuarios;
    }
}
