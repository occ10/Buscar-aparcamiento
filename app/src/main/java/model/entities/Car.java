package model.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by walid on 07/04/2018.
 */

public class Car implements Parcelable {

    public static final String REGISTRATION = "registration";
    public static final String MODEL = "model";
    public static final String COLOR = "color";
    public static final String SEATING = "seating";
    public static final String CARUSER = "user";
    public static final String BRAND = "brand";
    public static final String CATEGORY = "category";
    public static final String IMAGE = "image";

    private String registration;
    private String model;
    private String color;
    private int seating;//acientos
    private String user;
    private String brand;//marca
    private String category;
    private String image;

    public Car() {}

    public Car(String registration, String model, String color, int seating, String user, String brand, String category, String image) {
        this.registration = registration;
        this.model = model;
        this.color = color;
        this.seating = seating;
        this.user = user;
        this.brand = brand;
        this.category = category;
        this.image = image;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSeating() {
        return seating;
    }

    public void setSeating(int seating) {
        this.seating = seating;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(registration);
        dest.writeString(model);
        dest.writeString(color);
        dest.writeInt(seating);
        dest.writeValue(user);
        dest.writeString(brand);
        dest.writeString(category);
        dest.writeString(image);
    }


    public Car(Parcel in) {
        registration = in.readString();
        model = in.readString();
        color = in.readString();
        seating = in.readInt();
        user = in.readString();
        //  sexo = in.readInt();
        brand = in.readString();
        category = in.readString();
        image = in.readString();
    }

    public static final Parcelable.Creator<Car> CREATOR
            = new Parcelable.Creator<Car>() {
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        public Car[] newArray(int size) {
            return new Car[size];
        }
    };
}
