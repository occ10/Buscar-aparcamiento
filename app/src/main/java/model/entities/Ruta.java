package model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by walid on 18/02/2018.
 */

public class Ruta implements Parcelable {

    public static final String ID = "id";
    public static final String PLAZAS = "plazas";
    public static final String PLAZASOCUPADAS = "plazasOcupadas";
    public static final String ORIGEN = "origen";
    public static final String DETALLES = "detalles";
    public static final String PRECIO = "precio";
    public static final String FECHAPUBLICACION = "fechaPublicacion";
    public static final String OPCION = "opcion";
    public static final String USER = "user";

    private int id;
    private int plazas;
    private int plazasOcupadas;
    private String origen;
    private String detalles;
    private double precio;
    private java.util.Date fechaPublicacion;
    private int opcion;
    private Usuario user;

    public Ruta(){}
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public static String getID() {
        return ID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlazas() {
        return plazas;
    }

    public void setPlazas(int plazas) {
        this.plazas = plazas;
    }

    public int getPlazasOcupadas() {
        return plazasOcupadas;
    }

    public void setPlazasOcupadas(int plazasOcupadas) {
        this.plazasOcupadas = plazasOcupadas;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getOpcion() {
        return opcion;
    }

    public void setOpcion(int opcion) {
        this.opcion = opcion;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(plazas);
        dest.writeInt(plazasOcupadas);
        dest.writeString(origen);
        dest.writeString(detalles);
        dest.writeDouble(precio);
        dest.writeValue(fechaPublicacion);
        dest.writeInt(opcion);
        dest.writeValue(user);


    }
    public Ruta(Parcel in) {
        id = in.readInt();
        plazas = in.readInt();
        plazasOcupadas = in.readInt();
        origen = in.readString();
        detalles = in.readString();
        precio = in.readDouble();
        fechaPublicacion = (java.sql.Date) in.readSerializable();;
        opcion = in.readInt();
        user = (Usuario) in.readSerializable();;
    }

    public static final Parcelable.Creator<Ruta> CREATOR
            = new Parcelable.Creator<Ruta>() {
        public Ruta createFromParcel(Parcel in) {
            return new Ruta(in);
        }

        public Ruta[] newArray(int size) {
            return new Ruta[size];
        }
    };
}
