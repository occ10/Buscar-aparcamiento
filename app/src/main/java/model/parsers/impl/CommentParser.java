package model.parsers.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.entities.Comment;
import model.entities.Usuario;
import model.parsers.IParser;

import static model.entities.Comment.*;
import static model.entities.Usuario.*;

/**
 * Created by walid on 11/04/2018.
 */

public class CommentParser implements IParser<Comment> {

    private JSONObject getJsonObject(Comment model) throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put(USERCOMENT, model.getUserComment());
        obj.put(USERCOMMENTED,model.getUserCommented());
        obj.put(COMMENT,model.getComment());
        obj.put(USER,model.getUser());
        return obj;
    }

    @Override
    public String fromModel(Comment model) throws JSONException {
        return getJsonObject(model).toString();
    }

    @Override
    public String fromModel(List<Comment> model) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Comment COMMENT : model) {
            jsonArray.put(getJsonObject(COMMENT));
        }
        return jsonArray.toString();
    }

    @Override
    public List<Comment> fromJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        List<Comment> comments = new ArrayList<>();
        Comment comment;
        Usuario usuario;
        for (int i = 0; i<jsonArray.length(); i++) {
            JSONObject nuevo = jsonArray.getJSONObject(i);
            comment = new Comment();
            usuario = new Usuario();
            comment.setUserComment(nuevo.getString(USERCOMENT));
            comment.setUserCommented(nuevo.getString(USERCOMMENTED));
            comment.setComment(nuevo.getString(COMMENT));

            JSONObject userObj = new JSONObject(nuevo.getString(USER));
            usuario.setEmail(userObj.getString(EMAIL));
            usuario.setNombre(userObj.getString(NOMBRE));
            usuario.setApellido(userObj.getString(APELLIDO));
            usuario.setEdad(userObj.getInt(EDAD));
            usuario.setTelefono(userObj.getString(TELEFONO));
            usuario.setDescripcion(userObj.getString(DESCRIPCION));
            usuario.setFoto(userObj.getString(FOTO));
            comment.setUser(usuario);
            comments.add(comment);
           }
        return comments;
    }

    @Override
    public Comment getJsonObject(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        Comment comment = new Comment();
        Usuario usuario = new Usuario();
        comment.setUserComment(obj.getString(USERCOMENT));
        comment.setUserCommented(obj.getString(USERCOMMENTED));
        comment.setComment(obj.getString(COMMENT));

        JSONObject userObj = new JSONObject(obj.getString(USER));
        usuario.setEmail(userObj.getString(EMAIL));
        usuario.setNombre(userObj.getString(NOMBRE));
        usuario.setApellido(userObj.getString(APELLIDO));
        usuario.setEdad(userObj.getInt(EDAD));
        usuario.setTelefono(userObj.getString(TELEFONO));
        usuario.setDescripcion(userObj.getString(DESCRIPCION));
        usuario.setFoto(userObj.getString(FOTO));
        comment.setUser(usuario);

        return comment;
    }
}
