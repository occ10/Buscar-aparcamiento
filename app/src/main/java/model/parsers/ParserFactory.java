package model.parsers;

import model.parsers.impl.ParkingParser;
import model.parsers.impl.RutaParser;
import model.parsers.impl.UsuarioParser;
import model.parsers.impl.ZonaParser;

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
    public IParser getParkingParser() {
        return new ParkingParser();
    }

    public IParser getZonaParser() {
        return new ZonaParser();
    }
   // public IParser getOpinionParser() { return new OpinionParser(); }


    public static ParserFactory newInstance() {
        return new ParserFactory();
    }
}
