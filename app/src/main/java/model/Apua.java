package model;

/**
 * Created by walid on 27/01/2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import model.parsers.impl.*;
import server.ServerAgent;


public class Apua {

    public class Parser {

        public UsuarioParser usuario;

    }

    public Parser parser;
    public ServerAgent serverAgent;


    public Apua(Context appContext) {

        parser = new Parser();
        parser.usuario = new UsuarioParser();
        serverAgent = new ServerAgent(appContext);
    }
}
