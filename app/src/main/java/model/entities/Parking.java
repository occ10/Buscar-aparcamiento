package model.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by walid on 03/03/2018.
 */

public class Parking implements Parcelable {

    public static final String ID = "id";
    public static final String CODIGO = "codigo";
    public static final String IDPARKING = "idParking";
    public static final String PLAZAS = "plazas";
    public static final String SUPERFICIE = "superficie";
    public static final String LON = "lon";
    public static final String LAT = "lat";

    private int id;
    private String codigo;
    private int idparking;
    private int plazas;
    private double superficie;
    private double lon;
    private double lat;

    public Parking(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getIdparking() {
        return idparking;
    }

    public void setIdparking(int idparking) {
        this.idparking = idparking;
    }

    public int getPlazas() {
        return plazas;
    }

    public void setPlazas(int plazas) {
        this.plazas = plazas;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
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
    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(codigo);
        dest.writeInt(idparking);
        dest.writeInt(plazas);
        dest.writeDouble(superficie);
        dest.writeDouble(lon);
        dest.writeDouble(lat);
    }

    public Parking(Parcel in) {
        id = in.readInt();
        codigo = in.readString();
        idparking = in.readInt();
        plazas = in.readInt();
        superficie = in.readDouble();
        lon = in.readDouble();
        lat = in.readDouble();
    }

    public static final Parcelable.Creator<Parking> CREATOR
            = new Parcelable.Creator<Parking>() {
        public Parking createFromParcel(Parcel in) {
            return new Parking(in);
        }

        public Parking[] newArray(int size) {
            return new Parking[size];
        }
    };
}
