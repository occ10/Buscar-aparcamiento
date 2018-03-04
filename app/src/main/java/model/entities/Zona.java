package model.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by walid on 04/03/2018.
 */

public class Zona implements Parcelable{

    public static final String ID = "id";
    public static final String LON = "lon";
    public static final String LAT = "lat";
    public static final String APARCAMIENTO = "aparcamiento";
    public static final String OCUPADA = "ocupada";


    private int id;
    private double lon;
    private double lat;
    private String aparcamiento;
    private int ocupada;

    public Zona(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getAparcamiento() {
        return aparcamiento;
    }

    public void setAparcamiento(String aparcamiento) {
        this.aparcamiento = aparcamiento;
    }

    public int getOcupada() {
        return ocupada;
    }

    public void setOcupada(int ocupada) {
        this.ocupada = ocupada;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeDouble(lon);
        dest.writeDouble(lat);
        dest.writeString(aparcamiento);
        dest.writeInt(ocupada);

    }

    public Zona(Parcel in) {
        id = in.readInt();
        lon = in.readDouble();
        lat = in.readDouble();
        aparcamiento = in.readString();
        ocupada = in.readInt();

    }

    public static final Parcelable.Creator<Zona> CREATOR
            = new Parcelable.Creator<Zona>() {
        public Zona createFromParcel(Parcel in) {
            return new Zona(in);
        }

        public Zona[] newArray(int size) {
            return new Zona[size];
        }
    };
}
