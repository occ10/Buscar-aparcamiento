package model.parsers;

import model.parsers.impl.RutaParser;
import model.parsers.impl.UsuarioParser;

/**
 * Created by walid on 17/02/2018.
 */

public class ParserFactory {

    private ParserFactory() { }

    public IParser getRutaParser() {
        return new RutaParser();
    }
    public IParser getUsuarioParser() {
        return new UsuarioParser();
    }
   /* public IParser getPuntoParser() {
        return new PuntoParser();
    }*/
   // public IParser getOpinionParser() { return new OpinionParser(); }


    public static ParserFactory newInstance() {
        return new ParserFactory();
    }
}
