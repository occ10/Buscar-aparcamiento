package application;

import android.app.Application;

import model.Apua;

/**
 * Created by walid on 27/01/2018.
 */

public class MyAppContext extends Application {

    private Apua apua;

    @Override
    public void onCreate() {
        super.onCreate();
        apua = new Apua(this);
    }

    public Apua getApuaInstance() {
        return apua;
    }
}