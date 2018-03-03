package model.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by walid on 21/01/2018.
 */

public class Usuario implements Parcelable {

    public static final String EMAIL = "correo";
    public static final String NOMBRE = "nombre";
    public static final String APELLIDO = "apellido";
   // public static final String PASSWORD = "password";
    public static final String EDAD = "edad";
    public static final String TELEFONO = "telefono";
    public static final String DESCRIPCION = "detalles";
    public static final String FOTO = "foto";
    public static final String CONFIRMADO = "confirmado";



    private String email;
    private String nombre;
    private String apellido;
   // private String password;
    private int edad;
    private String telefono;
    private String descripcion;
    private String foto;
    private String confirmado;

    public String getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(String confirmado) {
        this.confirmado = confirmado;
    }

    public Usuario() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String email) {
        this.foto = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    //public String getPassword() {
       // return password;
    //}

   // public void setPassword(String password) {
       // this.password = password;
    //}

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(nombre);
        dest.writeString(apellido);

        //dest.writeString(password);
        dest.writeInt(edad);
        dest.writeString(telefono);
        dest.writeString(descripcion);
        dest.writeString(foto);
        dest.writeString(confirmado);
    }


    public Usuario(Parcel in) {
        email = in.readString();
        nombre = in.readString();
        apellido = in.readString();
        //password = in.readString();
        edad = in.readInt();
        telefono = in.readString();
        //  sexo = in.readInt();
        descripcion = in.readString();
        foto = in.readString();
        confirmado = in.readString();
    }

    public static final Parcelable.Creator<Usuario> CREATOR
            = new Parcelable.Creator<Usuario>() {
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };



}


